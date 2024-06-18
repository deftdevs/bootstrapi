package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.plugins.authentication.api.config.IdpConfig;
import com.atlassian.plugins.authentication.api.config.SsoType;
import com.atlassian.plugins.authentication.api.config.oidc.OidcConfig;
import com.atlassian.plugins.authentication.api.config.saml.SamlConfig;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpOidcBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpSamlBean;

public class AuthenticationIdpBeanUtil {

    public static IdpConfig toIdpConfig(
            final AbstractAuthenticationIdpBean authenticationIdpBean) {

        return toIdpConfig(authenticationIdpBean, null);
    }

    public static IdpConfig toIdpConfig(
            final AbstractAuthenticationIdpBean authenticationIdpBean,
            final IdpConfig existingIdpConfig) {

        if (authenticationIdpBean instanceof AuthenticationIdpOidcBean) {
            return toOidcConfig((AuthenticationIdpOidcBean) authenticationIdpBean, existingIdpConfig);
        }

        throw new BadRequestException("IDP types other than OIDC are not (yet) supported");
    }

    private static OidcConfig toOidcConfig(
            final AuthenticationIdpOidcBean authenticationIdpOidcBean,
            final IdpConfig existingIdpConfig) {

        final OidcConfig.Builder oidcConfigBuilder;

        if (existingIdpConfig == null) {
            oidcConfigBuilder = OidcConfig.builder();
        } else {
            verifyIdAndType(authenticationIdpOidcBean, existingIdpConfig, OidcConfig.class);
            oidcConfigBuilder = OidcConfig.builder((OidcConfig) existingIdpConfig);
        }

        if (authenticationIdpOidcBean.getId() != null) {
            oidcConfigBuilder.setId(authenticationIdpOidcBean.getId());
        }
        if (authenticationIdpOidcBean.getName() != null) {
            oidcConfigBuilder.setName(authenticationIdpOidcBean.getName());
        }
        if (authenticationIdpOidcBean.getEnabled() != null) {
            oidcConfigBuilder.setEnabled(authenticationIdpOidcBean.getEnabled());
        }
        if (authenticationIdpOidcBean.getUrl() != null) {
            oidcConfigBuilder.setIssuer(authenticationIdpOidcBean.getUrl());
        }
        if (authenticationIdpOidcBean.getEnableRememberMe() != null) {
            oidcConfigBuilder.setEnableRememberMe(authenticationIdpOidcBean.getEnableRememberMe());
        }
        if (authenticationIdpOidcBean.getButtonText() != null) {
            oidcConfigBuilder.setButtonText(authenticationIdpOidcBean.getButtonText());
        }
        if (authenticationIdpOidcBean.getClientId() != null) {
            oidcConfigBuilder.setClientId(authenticationIdpOidcBean.getClientId());
        }
        if (authenticationIdpOidcBean.getClientSecret() != null) {
            oidcConfigBuilder.setClientSecret(authenticationIdpOidcBean.getClientSecret());
        }
        if (authenticationIdpOidcBean.getUsernameClaim() != null) {
            oidcConfigBuilder.setUsernameClaim(authenticationIdpOidcBean.getUsernameClaim());
        }
        if (authenticationIdpOidcBean.getAdditionalScopes() != null) {
            oidcConfigBuilder.setAdditionalScopes(authenticationIdpOidcBean.getAdditionalScopes());
        }
        if (authenticationIdpOidcBean.getDiscoveryEnabled() != null) {
            oidcConfigBuilder.setDiscoveryEnabled(authenticationIdpOidcBean.getDiscoveryEnabled());
        }
        if (authenticationIdpOidcBean.getAuthorizationEndpoint() != null) {
            oidcConfigBuilder.setAuthorizationEndpoint(authenticationIdpOidcBean.getAuthorizationEndpoint());
        }
        if (authenticationIdpOidcBean.getTokenEndpoint() != null) {
            oidcConfigBuilder.setTokenEndpoint(authenticationIdpOidcBean.getTokenEndpoint());
        }
        if (authenticationIdpOidcBean.getUserInfoEndpoint() != null) {
            oidcConfigBuilder.setUserInfoEndpoint(authenticationIdpOidcBean.getUserInfoEndpoint());
        }

        return oidcConfigBuilder.build();
    }

    public static AbstractAuthenticationIdpBean toAuthenticationIdpBean(
            final IdpConfig idpConfig) {

        if (idpConfig.getSsoType().equals(SsoType.OIDC)) {
            return toAuthenticationIdpOidcBean(idpConfig);
        } else if (idpConfig.getSsoType().equals(SsoType.SAML)) {
            return toAuthenticationIdpSamlBean(idpConfig);
        }

        throw new UnsupportedOperationException("The IDP type cannot be NONE");
    }

    private static AuthenticationIdpOidcBean toAuthenticationIdpOidcBean(
            final IdpConfig idpConfig) {

        if (!(idpConfig instanceof OidcConfig)) {
            throw new InternalServerErrorException("The class of the IDP config is not OIDC");
        }

        final OidcConfig oidcConfig = (OidcConfig) idpConfig;

        final AuthenticationIdpOidcBean authenticationIdpOidcBean = new AuthenticationIdpOidcBean();
        authenticationIdpOidcBean.setId(oidcConfig.getId());
        authenticationIdpOidcBean.setName(oidcConfig.getName());
        authenticationIdpOidcBean.setEnabled(oidcConfig.isEnabled());
        authenticationIdpOidcBean.setUrl(oidcConfig.getIssuer());
        authenticationIdpOidcBean.setEnableRememberMe(oidcConfig.isEnableRememberMe());
        authenticationIdpOidcBean.setButtonText(oidcConfig.getButtonText());
        authenticationIdpOidcBean.setClientId(oidcConfig.getClientId());
        authenticationIdpOidcBean.setUsernameClaim(oidcConfig.getUsernameClaim());
        authenticationIdpOidcBean.setAdditionalScopes(oidcConfig.getAdditionalScopes());
        authenticationIdpOidcBean.setDiscoveryEnabled(oidcConfig.isDiscoveryEnabled());
        authenticationIdpOidcBean.setAuthorizationEndpoint(oidcConfig.getAuthorizationEndpoint());
        authenticationIdpOidcBean.setTokenEndpoint(oidcConfig.getTokenEndpoint());
        authenticationIdpOidcBean.setUserInfoEndpoint(oidcConfig.getUserInfoEndpoint());

        return authenticationIdpOidcBean;
    }

    private static AuthenticationIdpSamlBean toAuthenticationIdpSamlBean(
            final IdpConfig idpConfig) {

        if (!(idpConfig instanceof SamlConfig)) {
            throw new InternalServerErrorException("The class of the IDP config is not SAML");
        }

        final SamlConfig samlConfig = (SamlConfig) idpConfig;

        final AuthenticationIdpSamlBean authenticationIdpSamlBean = new AuthenticationIdpSamlBean();
        authenticationIdpSamlBean.setId(samlConfig.getId());
        authenticationIdpSamlBean.setName(samlConfig.getName());
        authenticationIdpSamlBean.setEnabled(samlConfig.isEnabled());
        authenticationIdpSamlBean.setUrl(samlConfig.getIssuer());
        authenticationIdpSamlBean.setEnableRememberMe(samlConfig.isEnableRememberMe());
        authenticationIdpSamlBean.setButtonText(samlConfig.getButtonText());
        // is it wanted to return the certificate here?
        authenticationIdpSamlBean.setUsernameAttribute(samlConfig.getUsernameAttribute());

        return authenticationIdpSamlBean;
    }

    private static void verifyIdAndType(
            final AbstractAuthenticationIdpBean authenticationIdpBean,
            final IdpConfig existingIdpConfig,
            final Class<? extends IdpConfig> clazz) {

        if (authenticationIdpBean.getId() != null && !authenticationIdpBean.getId().equals(existingIdpConfig.getId())) {
            throw new BadRequestException("An ID has been passed but it does not match the ID of the existing IDP with the same name");
        }

        if (!clazz.isAssignableFrom(existingIdpConfig.getClass())) {
            throw new BadRequestException("The existing IDP config with the same name is not of type OIDC");
        }
    }

    private AuthenticationIdpBeanUtil() {
    }

}
