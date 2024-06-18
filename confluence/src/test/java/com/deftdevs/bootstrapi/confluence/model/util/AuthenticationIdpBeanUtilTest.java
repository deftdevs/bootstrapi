package de.aservo.confapi.confluence.model.util;

import com.atlassian.plugins.authentication.api.config.IdpConfig;
import com.atlassian.plugins.authentication.api.config.SsoType;
import com.atlassian.plugins.authentication.api.config.oidc.OidcConfig;
import com.atlassian.plugins.authentication.api.config.saml.SamlConfig;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.AbstractAuthenticationIdpBean;
import de.aservo.confapi.commons.model.AuthenticationIdpOidcBean;
import de.aservo.confapi.commons.model.AuthenticationIdpSamlBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationIdpBeanUtilTest {

    @Test
    void testToIdpConfigOidc() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = AuthenticationIdpOidcBean.EXAMPLE_1;
        final IdpConfig idpConfig = AuthenticationIdpBeanUtil.toIdpConfig(authenticationIdpOidcBean);
        assertEquals(SsoType.OIDC, idpConfig.getSsoType());
    }

    @Test
    void testToIdpConfigOidcExistingIdDoesNotMatch() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = AuthenticationIdpOidcBean.EXAMPLE_1;
        final IdpConfig existingIdpConfig = OidcConfig.builder().setId(authenticationIdpOidcBean.getId() + 1).build();

        assertThrows(BadRequestException.class, () -> {
            AuthenticationIdpBeanUtil.toIdpConfig(authenticationIdpOidcBean, existingIdpConfig);
        });
    }

    @Test
    void testToIdpConfigOidcExistingTypeDoesNotMatch() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = AuthenticationIdpOidcBean.EXAMPLE_1;
        final IdpConfig existingIdpConfig = SamlConfig.builder().build();

        assertThrows(BadRequestException.class, () -> {
            AuthenticationIdpBeanUtil.toIdpConfig(authenticationIdpOidcBean, existingIdpConfig);
        });
    }

    @Test
    void testToAuthenticationIdpBeanOidcType() {
        final IdpConfig idpConfig = OidcConfig.builder().setName("oidc").build();
        final AbstractAuthenticationIdpBean authenticationIdpBean = AuthenticationIdpBeanUtil.toAuthenticationIdpBean(idpConfig);
        assertTrue(authenticationIdpBean.getClass().isAssignableFrom(AuthenticationIdpOidcBean.class));
    }

    @Test
    void testToAuthenticationIdpBeanSamlType() {
        final IdpConfig idpConfig = SamlConfig.builder().setName("saml").build();
        final AbstractAuthenticationIdpBean authenticationIdpBean = AuthenticationIdpBeanUtil.toAuthenticationIdpBean(idpConfig);
        assertTrue(authenticationIdpBean.getClass().isAssignableFrom(AuthenticationIdpSamlBean.class));
    }

}
