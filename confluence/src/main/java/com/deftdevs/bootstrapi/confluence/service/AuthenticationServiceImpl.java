package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.authentication.api.config.IdpConfig;
import com.atlassian.plugins.authentication.api.config.IdpConfigService;
import com.atlassian.plugins.authentication.api.config.SsoConfig;
import com.atlassian.plugins.authentication.api.config.SsoConfigService;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpsBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;
import com.deftdevs.bootstrapi.confluence.model.util.AuthenticationIdpBeanUtil;
import com.deftdevs.bootstrapi.confluence.model.util.AuthenticationSsoBeanUtil;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ExportAsService(AuthenticationService.class)
public class AuthenticationServiceImpl implements AuthenticationService {

    @ComponentImport
    private final IdpConfigService idpConfigService;

    @ComponentImport
    private final SsoConfigService ssoConfigService;

    public AuthenticationServiceImpl(
            final IdpConfigService idpConfigService,
            final SsoConfigService ssoConfigService) {

        this.idpConfigService = idpConfigService;
        this.ssoConfigService = ssoConfigService;
    }

    @Override
    public AuthenticationIdpsBean getAuthenticationIdps() {
        return new AuthenticationIdpsBean(idpConfigService.getIdpConfigs().stream()
                .map(AuthenticationIdpBeanUtil::toAuthenticationIdpBean)
                .sorted(authenticationIdpBeanComparator)
                .collect(Collectors.toList()));
    }

    @Override
    public AuthenticationIdpsBean setAuthenticationIdps(
            final AuthenticationIdpsBean authenticationIdpsBean) {

        return new AuthenticationIdpsBean(authenticationIdpsBean.getAuthenticationIdpBeans().stream()
                .map(this::setAuthenticationIdp)
                .sorted(authenticationIdpBeanComparator)
                .collect(Collectors.toList()));
    }

    public AbstractAuthenticationIdpBean setAuthenticationIdp(
            final AbstractAuthenticationIdpBean authenticationIdpBean) {

        if (authenticationIdpBean.getName() == null || authenticationIdpBean.getName().trim().isEmpty()) {
            throw new BadRequestException("The name cannot be empty");
        }

        final IdpConfig existingIdpConfig = findIdpConfigByName(authenticationIdpBean.getName());

        if (existingIdpConfig == null) {
            final IdpConfig idpConfig = AuthenticationIdpBeanUtil.toIdpConfig(authenticationIdpBean);
            final IdpConfig addedIdpConfig = idpConfigService.addIdpConfig(idpConfig);
            return AuthenticationIdpBeanUtil.toAuthenticationIdpBean(addedIdpConfig);
        }

        final IdpConfig idpConfig = AuthenticationIdpBeanUtil.toIdpConfig(authenticationIdpBean, existingIdpConfig);
        final IdpConfig updatedIdpConfig = idpConfigService.updateIdpConfig(idpConfig);
        return AuthenticationIdpBeanUtil.toAuthenticationIdpBean(updatedIdpConfig);
    }

    @Override
    public AuthenticationSsoBean getAuthenticationSso() {
        return AuthenticationSsoBeanUtil.toAuthenticationSsoBean(ssoConfigService.getSsoConfig());
    }

    @Override
    public AuthenticationSsoBean setAuthenticationSso(AuthenticationSsoBean authenticationSsoBean) {
        final SsoConfig existingSsoConfig = ssoConfigService.getSsoConfig();
        final SsoConfig ssoConfig = AuthenticationSsoBeanUtil.toSsoConfig(authenticationSsoBean, existingSsoConfig);
        return AuthenticationSsoBeanUtil.toAuthenticationSsoBean(ssoConfigService.updateSsoConfig(ssoConfig));
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

    static Comparator<AbstractAuthenticationIdpBean> authenticationIdpBeanComparator = (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName());

}
