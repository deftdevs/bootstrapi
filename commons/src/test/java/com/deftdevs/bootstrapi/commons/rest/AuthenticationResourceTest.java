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

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuthenticationResourceTest {

    @Mock
    private AuthenticationService authenticationService;

    private TestAuthenticationResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestAuthenticationResourceImpl(authenticationService);
    }

    @Test
    void testGetAuthenticationIdps() {
        final List<AbstractAuthenticationIdpModel> authenticationIdpModels = Arrays.asList(
                AuthenticationIdpOidcModel.EXAMPLE_1,
                AuthenticationIdpSamlModel.EXAMPLE_1
        );
        doReturn(authenticationIdpModels).when(authenticationService).getAuthenticationIdps();

        final Response response = resource.getAuthenticationIdps();
        assertEquals(200, response.getStatus());

        final List<AbstractAuthenticationIdpModel> authenticationIdpModelsResponse = (List<AbstractAuthenticationIdpModel>) response.getEntity();
        assertEquals(authenticationIdpModels, authenticationIdpModelsResponse);
    }

    @Test
    void testSetAuthenticationIdps() {
        final List<AbstractAuthenticationIdpModel> authenticationIdpModels = Arrays.asList(
                AuthenticationIdpOidcModel.EXAMPLE_1,
                AuthenticationIdpSamlModel.EXAMPLE_1
        );
        doReturn(authenticationIdpModels).when(authenticationService).setAuthenticationIdps(authenticationIdpModels);

        final Response response = resource.setAuthenticationIdps(authenticationIdpModels);
        assertEquals(200, response.getStatus());

        final List<AbstractAuthenticationIdpModel> authenticationIdpModelsResponse = (List<AbstractAuthenticationIdpModel>) response.getEntity();
        assertEquals(authenticationIdpModels, authenticationIdpModelsResponse);
    }

    @Test
    void testGetAuthenticationSso() {
        final AuthenticationSsoModel authenticationSsoModel = AuthenticationSsoModel.EXAMPLE_1;
        doReturn(authenticationSsoModel).when(authenticationService).getAuthenticationSso();

        final Response response = resource.getAuthenticationSso();
        assertEquals(200, response.getStatus());

        final AuthenticationSsoModel authenticationSsoModelResponse = (AuthenticationSsoModel) response.getEntity();
        assertEquals(authenticationSsoModel, authenticationSsoModelResponse);
    }

    @Test
    void testSetAuthenticationSso() {
        final AuthenticationSsoModel authenticationSsoModel = AuthenticationSsoModel.EXAMPLE_1;
        doReturn(authenticationSsoModel).when(authenticationService).setAuthenticationSso(authenticationSsoModel);

        final Response response = resource.setAuthenticationSso(authenticationSsoModel);
        assertEquals(200, response.getStatus());

        final AuthenticationSsoModel authenticationSsoModelResponse = (AuthenticationSsoModel) response.getEntity();
        assertEquals(authenticationSsoModel, authenticationSsoModelResponse);
    }

}
