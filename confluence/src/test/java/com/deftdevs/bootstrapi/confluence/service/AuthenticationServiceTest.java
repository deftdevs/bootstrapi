package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.plugins.authentication.api.config.*;
import com.atlassian.plugins.authentication.api.config.oidc.OidcConfig;
import com.atlassian.plugins.authentication.api.config.saml.SamlConfig;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpOidcModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.confluence.model.util.AuthenticationIdpModelUtil;
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

        final List<AbstractAuthenticationIdpModel> authenticationIdpModels = authenticationService.getAuthenticationIdps();
        final List<String> names = authenticationIdpModels.stream()
                .map(AbstractAuthenticationIdpModel::getName)
                .collect(Collectors.toList());
        assertTrue(names.contains(oidcConfig.getName()));
        assertTrue(names.contains(samlConfig.getName()));
    }

    @Test
    void testSetAuthenticationIdpsWithCreate() {
        final AuthenticationIdpOidcModel authenticationIdpOidcModel = AuthenticationIdpOidcModel.EXAMPLE_1;
        final List<AbstractAuthenticationIdpModel> authenticationIdpModels = Collections.singletonList(authenticationIdpOidcModel);
        doAnswer(invocation -> invocation.getArgument(0)).when(idpConfigService).addIdpConfig(any());

        final List<AbstractAuthenticationIdpModel> resultAuthenticationIdpModels = authenticationService.setAuthenticationIdps(authenticationIdpModels);
        verify(idpConfigService, times(1)).addIdpConfig(any());
        assertEquals(authenticationIdpOidcModel.getId(), resultAuthenticationIdpModels.iterator().next().getId());
        assertEquals(authenticationIdpOidcModel.getName(), resultAuthenticationIdpModels.iterator().next().getName());
    }

    @Test
    void testSetAuthenticationIdpsWithUpdate() {
        final AuthenticationIdpOidcModel authenticationIdpOidcModel = AuthenticationIdpOidcModel.EXAMPLE_1;
        final List<AbstractAuthenticationIdpModel> authenticationIdpModels = Collections.singletonList(authenticationIdpOidcModel);
        final IdpConfig idpConfig = AuthenticationIdpModelUtil.toIdpConfig(authenticationIdpOidcModel);
        doReturn(Collections.singletonList(idpConfig)).when(idpConfigService).getIdpConfigs();
        doAnswer(invocation -> invocation.getArgument(0)).when(idpConfigService).updateIdpConfig(any());

        final List<AbstractAuthenticationIdpModel> resultAuthenticationIdpModels = authenticationService.setAuthenticationIdps(authenticationIdpModels);
        verify(idpConfigService, times(1)).updateIdpConfig(any());
        assertEquals(authenticationIdpOidcModel.getId(), resultAuthenticationIdpModels.iterator().next().getId());
        assertEquals(authenticationIdpOidcModel.getName(), resultAuthenticationIdpModels.iterator().next().getName());
    }

    @Test
    void testSetAuthenticationIdpsNameNull() {
        final AuthenticationIdpOidcModel authenticationIdpOidcModel = new AuthenticationIdpOidcModel();
        final List<AbstractAuthenticationIdpModel> authenticationIdpModels = Collections.singletonList(authenticationIdpOidcModel);

        assertThrows(BadRequestException.class, () -> {
            authenticationService.setAuthenticationIdps(authenticationIdpModels);
        });
    }

    @Test
    void testSetAuthenticationIdpsNameEmpty() {
        final AuthenticationIdpOidcModel authenticationIdpOidcModel = new AuthenticationIdpOidcModel();
        authenticationIdpOidcModel.setName("");
        final List<AbstractAuthenticationIdpModel> authenticationIdpModels = Collections.singletonList(authenticationIdpOidcModel);

        assertThrows(BadRequestException.class, () -> {
            authenticationService.setAuthenticationIdps(authenticationIdpModels);
        });
    }

    @Test
    void testGetAuthenticationSso() {
        final SsoConfig ssoConfig = ImmutableSsoConfig.builder()
                .setShowLoginForm(true)
                .setEnableAuthenticationFallback(true)
                .build();
        doReturn(ssoConfig).when(ssoConfigService).getSsoConfig();

        final AuthenticationSsoModel authenticationSsoModel = authenticationService.getAuthenticationSso();
        assertEquals(ssoConfig.getShowLoginForm(), authenticationSsoModel.getShowOnLogin());
        assertEquals(ssoConfig.enableAuthenticationFallback(), authenticationSsoModel.getEnableAuthenticationFallback());
    }

    @Test
    void testSetAuthenticationSso() {
        final AuthenticationSsoModel authenticationSsoModel = AuthenticationSsoModel.EXAMPLE_1;
        final SsoConfig ssoConfig = ImmutableSsoConfig.builder()
                .setShowLoginForm(authenticationSsoModel.getShowOnLogin())
                .setEnableAuthenticationFallback(authenticationSsoModel.getEnableAuthenticationFallback())
                .build();
        doReturn(ssoConfig).when(ssoConfigService).updateSsoConfig(ssoConfig);

        final AuthenticationSsoModel resultAuthenticationSsoModel = authenticationService.setAuthenticationSso(authenticationSsoModel);
        verify(ssoConfigService, times(1)).updateSsoConfig(ssoConfig);
        assertEquals(authenticationSsoModel.getShowOnLogin(), resultAuthenticationSsoModel.getShowOnLogin());
        assertEquals(authenticationSsoModel.getEnableAuthenticationFallback(), resultAuthenticationSsoModel.getEnableAuthenticationFallback());
    }

}
