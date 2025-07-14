package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.plugins.authentication.api.config.IdpConfig;
import com.atlassian.plugins.authentication.api.config.IdpConfigService;
import com.atlassian.plugins.authentication.api.config.SsoConfig;
import com.atlassian.plugins.authentication.api.config.SsoConfigService;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.jira.model.util.AuthenticationIdpModelUtil;
import com.deftdevs.bootstrapi.jira.model.util.AuthenticationSsoModelUtil;
import com.deftdevs.bootstrapi.jira.service.api.JiraAuthenticationService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AuthenticationServiceImpl implements JiraAuthenticationService {

    private final IdpConfigService idpConfigService;
    private final SsoConfigService ssoConfigService;

    public AuthenticationServiceImpl(
            final IdpConfigService idpConfigService,
            final SsoConfigService ssoConfigService) {

        this.idpConfigService = idpConfigService;
        this.ssoConfigService = ssoConfigService;
    }

    @Override
    public Map<String, ? extends AbstractAuthenticationIdpModel> getAuthenticationIdps() {
        return idpConfigService.getIdpConfigs().stream()
                .map(AuthenticationIdpModelUtil::toAuthenticationIdpModel)
                .collect(Collectors.toMap(AbstractAuthenticationIdpModel::getName, Function.identity()));
    }

    @Override
    public Map<String, ? extends AbstractAuthenticationIdpModel> setAuthenticationIdps(
            final Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModels) {

        return authenticationIdpModels.values().stream()
                .map(this::setAuthenticationIdp)
                .collect(Collectors.toMap(AbstractAuthenticationIdpModel::getName, Function.identity()));
    }

    public AbstractAuthenticationIdpModel setAuthenticationIdp(
            final AbstractAuthenticationIdpModel authenticationIdpModel) {

        if (authenticationIdpModel.getName() == null || authenticationIdpModel.getName().trim().isEmpty()) {
            throw new BadRequestException("The name cannot be empty");
        }

        final IdpConfig existingIdpConfig = findIdpConfigByName(authenticationIdpModel.getName());

        if (existingIdpConfig == null) {
            final IdpConfig idpConfig = AuthenticationIdpModelUtil.toIdpConfig(authenticationIdpModel);
            final IdpConfig addedIdpConfig = idpConfigService.addIdpConfig(idpConfig);
            return AuthenticationIdpModelUtil.toAuthenticationIdpModel(addedIdpConfig);
        }

        final IdpConfig idpConfig = AuthenticationIdpModelUtil.toIdpConfig(authenticationIdpModel, existingIdpConfig);
        final IdpConfig updatedIdpConfig = idpConfigService.updateIdpConfig(idpConfig);
        return AuthenticationIdpModelUtil.toAuthenticationIdpModel(updatedIdpConfig);
    }

    @Override
    public AuthenticationSsoModel getAuthenticationSso() {
        return AuthenticationSsoModelUtil.toAuthenticationSsoModel(ssoConfigService.getSsoConfig());
    }

    @Override
    public AuthenticationSsoModel setAuthenticationSso(AuthenticationSsoModel authenticationSsoModel) {
        final SsoConfig existingSsoConfig = ssoConfigService.getSsoConfig();
        final SsoConfig ssoConfig = AuthenticationSsoModelUtil.toSsoConfig(authenticationSsoModel, existingSsoConfig);
        return AuthenticationSsoModelUtil.toAuthenticationSsoModel(ssoConfigService.updateSsoConfig(ssoConfig));
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

}
