package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.plugins.authentication.api.config.ImmutableSsoConfig;
import com.atlassian.plugins.authentication.api.config.SsoConfig;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;

public class AuthenticationSsoModelUtil {

    public static SsoConfig toSsoConfig(
            final AuthenticationSsoModel authenticationSsoModel) {

        return toSsoConfig(authenticationSsoModel, null);
    }

    public static SsoConfig toSsoConfig(
            final AuthenticationSsoModel authenticationSsoModel,
            final SsoConfig existingSsoConfig) {

        final ImmutableSsoConfig.Builder ssoConfigBuilder;

        if (existingSsoConfig != null) {
            ssoConfigBuilder = ImmutableSsoConfig.toBuilder(existingSsoConfig);
        } else {
            ssoConfigBuilder = ImmutableSsoConfig.builder();
        }

        if (authenticationSsoModel.getShowOnLogin() != null) {
            ssoConfigBuilder.setShowLoginForm(authenticationSsoModel.getShowOnLogin());
        }

        if (authenticationSsoModel.getEnableAuthenticationFallback() != null) {
            ssoConfigBuilder.setEnableAuthenticationFallback(authenticationSsoModel.getEnableAuthenticationFallback());
        }

        return ssoConfigBuilder.build();
    }

    public static AuthenticationSsoModel toAuthenticationSsoModel(
            final SsoConfig ssoConfig) {

        return AuthenticationSsoModel.builder()
                .showOnLogin(ssoConfig.getShowLoginForm())
                .enableAuthenticationFallback(ssoConfig.enableAuthenticationFallback())
                .build();
    }

    private AuthenticationSsoModelUtil() {
    }

}
