package de.aservo.confapi.confluence.service;

import com.atlassian.plugins.authentication.api.config.*;
import com.atlassian.plugins.authentication.api.config.oidc.OidcConfig;
import com.atlassian.plugins.authentication.api.config.saml.SamlConfig;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.AbstractAuthenticationIdpBean;
import de.aservo.confapi.commons.model.AuthenticationIdpOidcBean;
import de.aservo.confapi.commons.model.AuthenticationIdpsBean;
import de.aservo.confapi.commons.model.AuthenticationSsoBean;
import de.aservo.confapi.confluence.model.util.AuthenticationIdpBeanUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private IdpConfigService idpConfigService;

    @Mock
    private SsoConfigService ssoConfigService;

    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    public void setup() {
        authenticationService = new AuthenticationServiceImpl(idpConfigService, ssoConfigService);
    }

    @Test
    void testGetAuthenticationIdps() {
        final OidcConfig oidcConfig = OidcConfig.builder().setName("oidc").build();
        final SamlConfig samlConfig = SamlConfig.builder().setName("saml").build();
        doReturn(Arrays.asList(oidcConfig, samlConfig)).when(idpConfigService).getIdpConfigs();

        final AuthenticationIdpsBean authenticationIdpsBean = authenticationService.getAuthenticationIdps();
        final List<String> names = authenticationIdpsBean.getAuthenticationIdpBeans().stream()
                .map(AbstractAuthenticationIdpBean::getName)
                .collect(Collectors.toList());
        assertTrue(names.contains(oidcConfig.getName()));
        assertTrue(names.contains(samlConfig.getName()));
    }

    @Test
    void testSetAuthenticationIdpsWithCreate() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = AuthenticationIdpOidcBean.EXAMPLE_1;
        final AuthenticationIdpsBean authenticationIdpsBean = new AuthenticationIdpsBean(Collections.singletonList(authenticationIdpOidcBean));
        doAnswer(invocation -> invocation.getArgument(0)).when(idpConfigService).addIdpConfig(any());

        final AuthenticationIdpsBean resultAuthenticationIdpsBean = authenticationService.setAuthenticationIdps(authenticationIdpsBean);
        verify(idpConfigService, times(1)).addIdpConfig(any());
        assertEquals(authenticationIdpOidcBean.getId(), resultAuthenticationIdpsBean.getAuthenticationIdpBeans().iterator().next().getId());
        assertEquals(authenticationIdpOidcBean.getName(), resultAuthenticationIdpsBean.getAuthenticationIdpBeans().iterator().next().getName());
    }

    @Test
    void testSetAuthenticationIdpsWithUpdate() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = AuthenticationIdpOidcBean.EXAMPLE_1;
        final AuthenticationIdpsBean authenticationIdpsBean = new AuthenticationIdpsBean(Collections.singletonList(authenticationIdpOidcBean));
        final IdpConfig idpConfig = AuthenticationIdpBeanUtil.toIdpConfig(authenticationIdpOidcBean);
        doReturn(Collections.singletonList(idpConfig)).when(idpConfigService).getIdpConfigs();
        doAnswer(invocation -> invocation.getArgument(0)).when(idpConfigService).updateIdpConfig(any());

        final AuthenticationIdpsBean resultAuthenticationIdpsBean = authenticationService.setAuthenticationIdps(authenticationIdpsBean);
        verify(idpConfigService, times(1)).updateIdpConfig(any());
        assertEquals(authenticationIdpOidcBean.getId(), resultAuthenticationIdpsBean.getAuthenticationIdpBeans().iterator().next().getId());
        assertEquals(authenticationIdpOidcBean.getName(), resultAuthenticationIdpsBean.getAuthenticationIdpBeans().iterator().next().getName());
    }

    @Test
    void testSetAuthenticationIdpsNameNull() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = new AuthenticationIdpOidcBean();
        final AuthenticationIdpsBean authenticationIdpsBean = new AuthenticationIdpsBean(Collections.singletonList(authenticationIdpOidcBean));

        assertThrows(BadRequestException.class, () -> {
            authenticationService.setAuthenticationIdps(authenticationIdpsBean);
        });
    }

    @Test
    void testSetAuthenticationIdpsNameEmpty() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = new AuthenticationIdpOidcBean();
        authenticationIdpOidcBean.setName("");
        final AuthenticationIdpsBean authenticationIdpsBean = new AuthenticationIdpsBean(Collections.singletonList(authenticationIdpOidcBean));

        assertThrows(BadRequestException.class, () -> {
            authenticationService.setAuthenticationIdps(authenticationIdpsBean);
        });
    }

    @Test
    void testGetAuthenticationSso() {
        final SsoConfig ssoConfig = ImmutableSsoConfig.builder().setShowLoginForm(true).build();
        doReturn(ssoConfig).when(ssoConfigService).getSsoConfig();

        final AuthenticationSsoBean authenticationSsoBean = authenticationService.getAuthenticationSso();
        assertEquals(ssoConfig.getShowLoginForm(), authenticationSsoBean.getShowOnLogin());
    }

    @Test
    void testSetAuthenticationSso() {
        final AuthenticationSsoBean authenticationSsoBean = AuthenticationSsoBean.EXAMPLE_1;
        final SsoConfig ssoConfig = ImmutableSsoConfig.builder().setShowLoginForm(authenticationSsoBean.getShowOnLogin()).build();
        doReturn(ssoConfig).when(ssoConfigService).updateSsoConfig(ssoConfig);

        final AuthenticationSsoBean resultAuthenticationSsoBean = authenticationService.setAuthenticationSso(authenticationSsoBean);
        verify(ssoConfigService, times(1)).updateSsoConfig(ssoConfig);
        assertEquals(authenticationSsoBean.getShowOnLogin(), resultAuthenticationSsoBean.getShowOnLogin());
    }

}
