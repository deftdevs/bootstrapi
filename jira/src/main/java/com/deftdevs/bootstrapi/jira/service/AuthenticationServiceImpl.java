package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.plugins.authentication.api.config.IdpConfig;
import com.atlassian.plugins.authentication.api.config.IdpConfigService;
import com.atlassian.plugins.authentication.api.config.SsoConfig;
import com.atlassian.plugins.authentication.api.config.SsoConfigService;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.service.AbstractAuthenticationService;
import com.deftdevs.bootstrapi.jira.model.util.AuthenticationIdpModelUtil;
import com.deftdevs.bootstrapi.jira.model.util.AuthenticationSsoModelUtil;
import com.deftdevs.bootstrapi.jira.service.api.JiraAuthenticationService;

public class AuthenticationServiceImpl
        extends AbstractAuthenticationService<AbstractAuthenticationIdpModel, AuthenticationSsoModel>
        implements JiraAuthenticationService {

    public AuthenticationServiceImpl(
            final IdpConfigService idpConfigService,
            final SsoConfigService ssoConfigService) {

        super(idpConfigService, ssoConfigService);
    }

    @Override
    protected AbstractAuthenticationIdpModel toAuthenticationIdpModel(final IdpConfig idpConfig) {
        return AuthenticationIdpModelUtil.toAuthenticationIdpModel(idpConfig);
    }

    @Override
    protected IdpConfig toIdpConfig(final AbstractAuthenticationIdpModel authenticationIdpModel) {
        return AuthenticationIdpModelUtil.toIdpConfig(authenticationIdpModel);
    }

    @Override
    protected IdpConfig toIdpConfig(final AbstractAuthenticationIdpModel authenticationIdpModel, final IdpConfig existingIdpConfig) {
        return AuthenticationIdpModelUtil.toIdpConfig(authenticationIdpModel, existingIdpConfig);
    }

    @Override
    protected AuthenticationSsoModel toAuthenticationSsoModel(final SsoConfig ssoConfig) {
        return AuthenticationSsoModelUtil.toAuthenticationSsoModel(ssoConfig);
    }

    @Override
    protected SsoConfig toSsoConfig(final AuthenticationSsoModel authenticationSsoModel, final SsoConfig existingSsoConfig) {
        return AuthenticationSsoModelUtil.toSsoConfig(authenticationSsoModel, existingSsoConfig);
    }

}
