package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.plugins.authentication.api.config.IdpConfig;
import com.atlassian.plugins.authentication.api.config.SsoType;
import com.atlassian.plugins.authentication.api.config.oidc.OidcConfig;
import com.atlassian.plugins.authentication.api.config.saml.SamlConfig;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpOidcModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpSamlModel;

public class AuthenticationIdpModelUtil {

    public static IdpConfig toIdpConfig(
            final AbstractAuthenticationIdpModel authenticationIdpModel) {

        return toIdpConfig(authenticationIdpModel, null);
    }

    public static IdpConfig toIdpConfig(
            final AbstractAuthenticationIdpModel authenticationIdpModel,
            final IdpConfig existingIdpConfig) {

        if (authenticationIdpModel instanceof AuthenticationIdpOidcModel) {
            return toOidcConfig((AuthenticationIdpOidcModel) authenticationIdpModel, existingIdpConfig);
        }

        throw new BadRequestException("IDP types other than OIDC are not (yet) supported");
    }

    private static OidcConfig toOidcConfig(
            final AuthenticationIdpOidcModel authenticationIdpOidcModel,
            final IdpConfig existingIdpConfig) {

        final OidcConfig.Builder oidcConfigBuilder;

        if (existingIdpConfig == null) {
            oidcConfigBuilder = OidcConfig.builder();
        } else {
            verifyIdAndType(authenticationIdpOidcModel, existingIdpConfig, OidcConfig.class);
            oidcConfigBuilder = OidcConfig.builder((OidcConfig) existingIdpConfig);
        }

        if (authenticationIdpOidcModel.getId() != null) {
            oidcConfigBuilder.setId(authenticationIdpOidcModel.getId());
        }
        if (authenticationIdpOidcModel.getName() != null) {
            oidcConfigBuilder.setName(authenticationIdpOidcModel.getName());
        }
        if (authenticationIdpOidcModel.getEnabled() != null) {
            oidcConfigBuilder.setEnabled(authenticationIdpOidcModel.getEnabled());
        }
        if (authenticationIdpOidcModel.getUrl() != null) {
            oidcConfigBuilder.setIssuer(authenticationIdpOidcModel.getUrl());
        }
        if (authenticationIdpOidcModel.getEnableRememberMe() != null) {
            oidcConfigBuilder.setEnableRememberMe(authenticationIdpOidcModel.getEnableRememberMe());
        }
        if (authenticationIdpOidcModel.getButtonText() != null) {
            oidcConfigBuilder.setButtonText(authenticationIdpOidcModel.getButtonText());
        }
        if (authenticationIdpOidcModel.getClientId() != null) {
            oidcConfigBuilder.setClientId(authenticationIdpOidcModel.getClientId());
        }
        if (authenticationIdpOidcModel.getClientSecret() != null) {
            oidcConfigBuilder.setClientSecret(authenticationIdpOidcModel.getClientSecret());
        }
        if (authenticationIdpOidcModel.getUsernameClaim() != null) {
            oidcConfigBuilder.setUsernameClaim(authenticationIdpOidcModel.getUsernameClaim());
        }
        if (authenticationIdpOidcModel.getAdditionalScopes() != null) {
            oidcConfigBuilder.setAdditionalScopes(authenticationIdpOidcModel.getAdditionalScopes());
        }
        if (authenticationIdpOidcModel.getDiscoveryEnabled() != null) {
            oidcConfigBuilder.setDiscoveryEnabled(authenticationIdpOidcModel.getDiscoveryEnabled());
        }
        if (authenticationIdpOidcModel.getAuthorizationEndpoint() != null) {
            oidcConfigBuilder.setAuthorizationEndpoint(authenticationIdpOidcModel.getAuthorizationEndpoint());
        }
        if (authenticationIdpOidcModel.getTokenEndpoint() != null) {
            oidcConfigBuilder.setTokenEndpoint(authenticationIdpOidcModel.getTokenEndpoint());
        }
        if (authenticationIdpOidcModel.getUserInfoEndpoint() != null) {
            oidcConfigBuilder.setUserInfoEndpoint(authenticationIdpOidcModel.getUserInfoEndpoint());
        }

        return oidcConfigBuilder.build();
    }

    public static AbstractAuthenticationIdpModel toAuthenticationIdpModel(
            final IdpConfig idpConfig) {

        if (idpConfig.getSsoType().equals(SsoType.OIDC)) {
            return toAuthenticationIdpOidcModel(idpConfig);
        } else if (idpConfig.getSsoType().equals(SsoType.SAML)) {
            return toAuthenticationIdpSamlModel(idpConfig);
        }

        throw new UnsupportedOperationException("The IDP type cannot be NONE");
    }

    private static AuthenticationIdpOidcModel toAuthenticationIdpOidcModel(
            final IdpConfig idpConfig) {

        if (!(idpConfig instanceof OidcConfig)) {
            throw new InternalServerErrorException("The class of the IDP config is not OIDC");
        }

        final OidcConfig oidcConfig = (OidcConfig) idpConfig;

        return AuthenticationIdpOidcModel.builder()
                .id(oidcConfig.getId())
                .name(oidcConfig.getName())
                .enabled(oidcConfig.isEnabled())
                .url(oidcConfig.getIssuer())
                .enableRememberMe(oidcConfig.isEnableRememberMe())
                .buttonText(oidcConfig.getButtonText())
                .clientId(oidcConfig.getClientId())
                .usernameClaim(oidcConfig.getUsernameClaim())
                .additionalScopes(oidcConfig.getAdditionalScopes())
                .discoveryEnabled(oidcConfig.isDiscoveryEnabled())
                .authorizationEndpoint(oidcConfig.getAuthorizationEndpoint())
                .tokenEndpoint(oidcConfig.getTokenEndpoint())
                .userInfoEndpoint(oidcConfig.getUserInfoEndpoint())
                .build();
    }

    private static AuthenticationIdpSamlModel toAuthenticationIdpSamlModel(
            final IdpConfig idpConfig) {

        if (!(idpConfig instanceof SamlConfig)) {
            throw new InternalServerErrorException("The class of the IDP config is not SAML");
        }

        final SamlConfig samlConfig = (SamlConfig) idpConfig;

        return AuthenticationIdpSamlModel.builder()
                .id(samlConfig.getId())
                .name(samlConfig.getName())
                .enabled(samlConfig.isEnabled())
                .url(samlConfig.getIssuer())
                .enableRememberMe(samlConfig.isEnableRememberMe())
                .buttonText(samlConfig.getButtonText())
                // is it wanted to return the certificate here?
                .usernameAttribute(samlConfig.getUsernameAttribute())
                .build();
    }

    private static void verifyIdAndType(
            final AbstractAuthenticationIdpModel authenticationIdpModel,
            final IdpConfig existingIdpConfig,
            final Class<? extends IdpConfig> clazz) {

        if (authenticationIdpModel.getId() != null && !authenticationIdpModel.getId().equals(existingIdpConfig.getId())) {
            throw new BadRequestException("An ID has been passed but it does not match the ID of the existing IDP with the same name");
        }

        if (!clazz.isAssignableFrom(existingIdpConfig.getClass())) {
            throw new BadRequestException("The existing IDP config with the same name is not of type OIDC");
        }
    }

    private AuthenticationIdpModelUtil() {
    }

}
