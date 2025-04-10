package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.plugins.authentication.api.config.*;
import com.atlassian.plugins.authentication.api.config.oidc.OidcConfig;
import com.atlassian.plugins.authentication.api.config.saml.SamlConfig;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpOidcBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.confluence.model.util.AuthenticationIdpBeanUtil;
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

        final List<AbstractAuthenticationIdpBean> authenticationIdpBeans = authenticationService.getAuthenticationIdps();
        final List<String> names = authenticationIdpBeans.stream()
                .map(AbstractAuthenticationIdpBean::getName)
                .collect(Collectors.toList());
        assertTrue(names.contains(oidcConfig.getName()));
        assertTrue(names.contains(samlConfig.getName()));
    }

    @Test
    void testSetAuthenticationIdpsWithCreate() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = AuthenticationIdpOidcBean.EXAMPLE_1;
        final List<AbstractAuthenticationIdpBean> authenticationIdpBeans = Collections.singletonList(authenticationIdpOidcBean);
        doAnswer(invocation -> invocation.getArgument(0)).when(idpConfigService).addIdpConfig(any());

        final List<AbstractAuthenticationIdpBean> resultAuthenticationIdpBeans = authenticationService.setAuthenticationIdps(authenticationIdpBeans);
        verify(idpConfigService, times(1)).addIdpConfig(any());
        assertEquals(authenticationIdpOidcBean.getId(), resultAuthenticationIdpBeans.iterator().next().getId());
        assertEquals(authenticationIdpOidcBean.getName(), resultAuthenticationIdpBeans.iterator().next().getName());
    }

    @Test
    void testSetAuthenticationIdpsWithUpdate() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = AuthenticationIdpOidcBean.EXAMPLE_1;
        final List<AbstractAuthenticationIdpBean> authenticationIdpBeans = Collections.singletonList(authenticationIdpOidcBean);
        final IdpConfig idpConfig = AuthenticationIdpBeanUtil.toIdpConfig(authenticationIdpOidcBean);
        doReturn(Collections.singletonList(idpConfig)).when(idpConfigService).getIdpConfigs();
        doAnswer(invocation -> invocation.getArgument(0)).when(idpConfigService).updateIdpConfig(any());

        final List<AbstractAuthenticationIdpBean> resultAuthenticationIdpBeans = authenticationService.setAuthenticationIdps(authenticationIdpBeans);
        verify(idpConfigService, times(1)).updateIdpConfig(any());
        assertEquals(authenticationIdpOidcBean.getId(), resultAuthenticationIdpBeans.iterator().next().getId());
        assertEquals(authenticationIdpOidcBean.getName(), resultAuthenticationIdpBeans.iterator().next().getName());
    }

    @Test
    void testSetAuthenticationIdpsNameNull() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = new AuthenticationIdpOidcBean();
        final List<AbstractAuthenticationIdpBean> authenticationIdpBeans = Collections.singletonList(authenticationIdpOidcBean);

        assertThrows(BadRequestException.class, () -> {
            authenticationService.setAuthenticationIdps(authenticationIdpBeans);
        });
    }

    @Test
    void testSetAuthenticationIdpsNameEmpty() {
        final AuthenticationIdpOidcBean authenticationIdpOidcBean = new AuthenticationIdpOidcBean();
        authenticationIdpOidcBean.setName("");
        final List<AbstractAuthenticationIdpBean> authenticationIdpBeans = Collections.singletonList(authenticationIdpOidcBean);

        assertThrows(BadRequestException.class, () -> {
            authenticationService.setAuthenticationIdps(authenticationIdpBeans);
        });
    }

    @Test
    void testGetAuthenticationSso() {
        final SsoConfig ssoConfig = ImmutableSsoConfig.builder()
                .setShowLoginForm(true)
                .setEnableAuthenticationFallback(true)
                .build();
        doReturn(ssoConfig).when(ssoConfigService).getSsoConfig();

        final AuthenticationSsoBean authenticationSsoBean = authenticationService.getAuthenticationSso();
        assertEquals(ssoConfig.getShowLoginForm(), authenticationSsoBean.getShowOnLogin());
        assertEquals(ssoConfig.enableAuthenticationFallback(), authenticationSsoBean.getEnableAuthenticationFallback());
    }

    @Test
    void testSetAuthenticationSso() {
        final AuthenticationSsoBean authenticationSsoBean = AuthenticationSsoBean.EXAMPLE_1;
        final SsoConfig ssoConfig = ImmutableSsoConfig.builder()
                .setShowLoginForm(authenticationSsoBean.getShowOnLogin())
                .setEnableAuthenticationFallback(authenticationSsoBean.getEnableAuthenticationFallback())
                .build();
        doReturn(ssoConfig).when(ssoConfigService).updateSsoConfig(ssoConfig);

        final AuthenticationSsoBean resultAuthenticationSsoBean = authenticationService.setAuthenticationSso(authenticationSsoBean);
        verify(ssoConfigService, times(1)).updateSsoConfig(ssoConfig);
        assertEquals(authenticationSsoBean.getShowOnLogin(), resultAuthenticationSsoBean.getShowOnLogin());
        assertEquals(authenticationSsoBean.getEnableAuthenticationFallback(), resultAuthenticationSsoBean.getEnableAuthenticationFallback());
    }

}
