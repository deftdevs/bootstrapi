package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.plugins.authentication.api.config.ImmutableSsoConfig;
import com.atlassian.plugins.authentication.api.config.SsoConfig;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;

public class AuthenticationSsoBeanUtil {

    public static SsoConfig toSsoConfig(
            final AuthenticationSsoBean authenticationSsoBean) {

        return toSsoConfig(authenticationSsoBean, null);
    }

    public static SsoConfig toSsoConfig(
            final AuthenticationSsoBean authenticationSsoBean,
            final SsoConfig existingSsoConfig) {

        final ImmutableSsoConfig.Builder ssoConfigBuilder;

        if (existingSsoConfig != null) {
            ssoConfigBuilder = ImmutableSsoConfig.toBuilder(existingSsoConfig);
        } else {
            ssoConfigBuilder = ImmutableSsoConfig.builder();
        }

        if (authenticationSsoBean.getShowOnLogin() != null) {
            ssoConfigBuilder.setShowLoginForm(authenticationSsoBean.getShowOnLogin());
        }

        if (authenticationSsoBean.getEnableAuthenticationFallback() != null) {
            ssoConfigBuilder.setEnableAuthenticationFallback(authenticationSsoBean.getEnableAuthenticationFallback());
        }

        return ssoConfigBuilder.build();
    }

    public static AuthenticationSsoBean toAuthenticationSsoBean(
            final SsoConfig ssoConfig) {

        final AuthenticationSsoBean authenticationSsoBean = new AuthenticationSsoBean();
        authenticationSsoBean.setShowOnLogin(ssoConfig.getShowLoginForm());
        authenticationSsoBean.setEnableAuthenticationFallback(ssoConfig.enableAuthenticationFallback());

        return authenticationSsoBean;
    }

    private AuthenticationSsoBeanUtil() {
    }

}
