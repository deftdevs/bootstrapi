package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.plugins.authentication.api.config.IdpConfig;
import com.atlassian.plugins.authentication.api.config.SsoType;
import com.atlassian.plugins.authentication.api.config.oidc.OidcConfig;
import com.atlassian.plugins.authentication.api.config.saml.SamlConfig;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpOidcModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpSamlModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationIdpModelUtilTest {

    @Test
    void testToIdpConfigOidc() {
        final AuthenticationIdpOidcModel authenticationIdpOidcModel = AuthenticationIdpOidcModel.EXAMPLE_1;
        final IdpConfig idpConfig = AuthenticationIdpModelUtil.toIdpConfig(authenticationIdpOidcModel);
        assertEquals(SsoType.OIDC, idpConfig.getSsoType());
    }

    @Test
    void testToIdpConfigOidcExistingIdDoesNotMatch() {
        final AuthenticationIdpOidcModel authenticationIdpOidcModel = AuthenticationIdpOidcModel.EXAMPLE_1;
        final IdpConfig existingIdpConfig = OidcConfig.builder().setId(authenticationIdpOidcModel.getId() + 1).build();

        assertThrows(BadRequestException.class, () -> {
            AuthenticationIdpModelUtil.toIdpConfig(authenticationIdpOidcModel, existingIdpConfig);
        });
    }

    @Test
    void testToIdpConfigOidcExistingTypeDoesNotMatch() {
        final AuthenticationIdpOidcModel authenticationIdpOidcModel = AuthenticationIdpOidcModel.EXAMPLE_1;
        final IdpConfig existingIdpConfig = SamlConfig.builder().build();

        assertThrows(BadRequestException.class, () -> {
            AuthenticationIdpModelUtil.toIdpConfig(authenticationIdpOidcModel, existingIdpConfig);
        });
    }

    @Test
    void testToAuthenticationIdpModelOidcType() {
        final IdpConfig idpConfig = OidcConfig.builder().setName("oidc").build();
        final AbstractAuthenticationIdpModel authenticationIdpModel = AuthenticationIdpModelUtil.toAuthenticationIdpModel(idpConfig);
        assertTrue(authenticationIdpModel.getClass().isAssignableFrom(AuthenticationIdpOidcModel.class));
    }

    @Test
    void testToAuthenticationIdpModelSamlType() {
        final IdpConfig idpConfig = SamlConfig.builder().setName("saml").build();
        final AbstractAuthenticationIdpModel authenticationIdpModel = AuthenticationIdpModelUtil.toAuthenticationIdpModel(idpConfig);
        assertTrue(authenticationIdpModel.getClass().isAssignableFrom(AuthenticationIdpSamlModel.class));
    }

}
