package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpOidcModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpSamlModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuthenticationResourceTest {

    @Mock
    private AuthenticationService<AbstractAuthenticationIdpModel, AuthenticationSsoModel> authenticationService;

    private TestAuthenticationResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestAuthenticationResourceImpl(authenticationService);
    }

    @Test
    void testGetAuthenticationIdps() {
        final Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModels = Map.of(
                AuthenticationIdpOidcModel.EXAMPLE_1.getName(), AuthenticationIdpOidcModel.EXAMPLE_1,
                AuthenticationIdpSamlModel.EXAMPLE_1.getName(), AuthenticationIdpSamlModel.EXAMPLE_1
        );
        doReturn(authenticationIdpModels).when(authenticationService).getAuthenticationIdps();

        final Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModelsResponse = resource.getAuthenticationIdps();
        assertEquals(authenticationIdpModels, authenticationIdpModelsResponse);
    }

    @Test
    void testSetAuthenticationIdps() {
        final Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModels = Map.of(
                AuthenticationIdpOidcModel.EXAMPLE_1.getName(), AuthenticationIdpOidcModel.EXAMPLE_1,
                AuthenticationIdpSamlModel.EXAMPLE_1.getName(), AuthenticationIdpSamlModel.EXAMPLE_1
        );
        doReturn(authenticationIdpModels).when(authenticationService).setAuthenticationIdps(authenticationIdpModels);

        final Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModelsResponse = resource.setAuthenticationIdps(authenticationIdpModels);
        assertEquals(authenticationIdpModels, authenticationIdpModelsResponse);
    }

    @Test
    void testGetAuthenticationSso() {
        final AuthenticationSsoModel authenticationSsoModel = AuthenticationSsoModel.EXAMPLE_1;
        doReturn(authenticationSsoModel).when(authenticationService).getAuthenticationSso();

        final AuthenticationSsoModel authenticationSsoModelResponse = resource.getAuthenticationSso();
        assertEquals(authenticationSsoModel, authenticationSsoModelResponse);
    }

    @Test
    void testSetAuthenticationSso() {
        final AuthenticationSsoModel authenticationSsoModel = AuthenticationSsoModel.EXAMPLE_1;
        doReturn(authenticationSsoModel).when(authenticationService).setAuthenticationSso(authenticationSsoModel);

        final AuthenticationSsoModel authenticationSsoModelResponse = resource.setAuthenticationSso(authenticationSsoModel);
        assertEquals(authenticationSsoModel, authenticationSsoModelResponse);
    }

}
