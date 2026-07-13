package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.plugins.authentication.api.config.IdpConfig;
import com.atlassian.plugins.authentication.api.config.IdpConfigService;
import com.atlassian.plugins.authentication.api.config.SsoConfig;
import com.atlassian.plugins.authentication.api.config.SsoConfigService;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractAuthenticationService implements AuthenticationService {

    protected final IdpConfigService idpConfigService;
    protected final SsoConfigService ssoConfigService;

    protected AbstractAuthenticationService(
            final IdpConfigService idpConfigService,
            final SsoConfigService ssoConfigService) {

        this.idpConfigService = idpConfigService;
        this.ssoConfigService = ssoConfigService;
    }

    @Override
    public Map<String, ? extends AbstractAuthenticationIdpModel> getAuthenticationIdps() {
        return idpConfigService.getIdpConfigs().stream()
                .map(this::toAuthenticationIdpModel)
                .collect(Collectors.toMap(AbstractAuthenticationIdpModel::getName, Function.identity(), (existing, replacement) -> {
                    throw new IllegalStateException("Duplicate name key found: " + existing.getName());
                }));
    }

    @Override
    public Map<String, ? extends AbstractAuthenticationIdpModel> setAuthenticationIdps(
            final Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModels) {

        final Map<String, AbstractAuthenticationIdpModel> resultIdpModels = new LinkedHashMap<>();

        for (Map.Entry<String, ? extends AbstractAuthenticationIdpModel> entry : authenticationIdpModels.entrySet()) {
            final String idpName = entry.getKey();
            final AbstractAuthenticationIdpModel idpModel = entry.getValue();

            if (idpModel == null) {
                // declarative no-op: null model + existing entity → return as-is;
                // null model + missing entity → nothing to do
                final IdpConfig existingIdpConfig = findIdpConfigByName(idpName);
                if (existingIdpConfig != null) {
                    resultIdpModels.put(idpName, toAuthenticationIdpModel(existingIdpConfig));
                }
                continue;
            }

            if (idpModel.getName() == null) {
                idpModel.setName(idpName);
            }

            final AbstractAuthenticationIdpModel resultIdpModel = setAuthenticationIdp(idpModel);
            resultIdpModels.put(resultIdpModel.getName(), resultIdpModel);
        }

        return resultIdpModels;
    }

    @Override
    public AbstractAuthenticationIdpModel setAuthenticationIdp(
            final AbstractAuthenticationIdpModel authenticationIdpModel) {

        if (authenticationIdpModel.getName() == null || authenticationIdpModel.getName().trim().isEmpty()) {
            throw new BadRequestException("The name cannot be empty");
        }

        final IdpConfig existingIdpConfig = findIdpConfigByName(authenticationIdpModel.getName());

        if (existingIdpConfig == null) {
            final IdpConfig idpConfig = toIdpConfig(authenticationIdpModel);
            final IdpConfig addedIdpConfig = idpConfigService.addIdpConfig(idpConfig);
            return toAuthenticationIdpModel(addedIdpConfig);
        }

        final IdpConfig idpConfig = toIdpConfig(authenticationIdpModel, existingIdpConfig);
        final IdpConfig updatedIdpConfig = idpConfigService.updateIdpConfig(idpConfig);
        return toAuthenticationIdpModel(updatedIdpConfig);
    }

    @Override
    public AuthenticationSsoModel getAuthenticationSso() {
        return toAuthenticationSsoModel(ssoConfigService.getSsoConfig());
    }

    @Override
    public AuthenticationSsoModel setAuthenticationSso(
            final AuthenticationSsoModel authenticationSsoModel) {

        final SsoConfig existingSsoConfig = ssoConfigService.getSsoConfig();
        final SsoConfig ssoConfig = toSsoConfig(authenticationSsoModel, existingSsoConfig);
        return toAuthenticationSsoModel(ssoConfigService.updateSsoConfig(ssoConfig));
    }

    IdpConfig findIdpConfigByName(
            final String name) {

        final Map<String, IdpConfig> idpConfigsByName = idpConfigService.getIdpConfigs().stream().collect(Collectors.toMap(
                IdpConfig::getName, Function.identity(), (existing, replacement) -> {
                    throw new IllegalStateException("Duplicate name key found: " + existing.getName());
                }
        ));

        return idpConfigsByName.get(name);
    }

    protected abstract AbstractAuthenticationIdpModel toAuthenticationIdpModel(IdpConfig idpConfig);

    protected abstract IdpConfig toIdpConfig(AbstractAuthenticationIdpModel authenticationIdpModel);

    protected abstract IdpConfig toIdpConfig(AbstractAuthenticationIdpModel authenticationIdpModel, IdpConfig existingIdpConfig);

    protected abstract AuthenticationSsoModel toAuthenticationSsoModel(SsoConfig ssoConfig);

    protected abstract SsoConfig toSsoConfig(AuthenticationSsoModel authenticationSsoModel, SsoConfig existingSsoConfig);

}
