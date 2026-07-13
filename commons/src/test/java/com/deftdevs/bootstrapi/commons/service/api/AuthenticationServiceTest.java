package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    private AbstractAuthenticationIdpModel idpModel;
    private AuthenticationSsoModel ssoModel;
    private Map<String, AbstractAuthenticationIdpModel> idpModels;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        authenticationService = mock(AuthenticationService.class, CALLS_REAL_METHODS);

        idpModel = mock(AbstractAuthenticationIdpModel.class);
        ssoModel = new AuthenticationSsoModel();
        idpModels = Collections.singletonMap("idp", idpModel);
    }

    @Test
    void testGetAuthentication() {
        doReturn(idpModels).when(authenticationService).getAuthenticationIdps();
        doReturn(ssoModel).when(authenticationService).getAuthenticationSso();

        final AuthenticationModel authenticationModel = authenticationService.getAuthentication();

        assertEquals(idpModels, authenticationModel.getIdps());
        assertEquals(ssoModel, authenticationModel.getSso());
    }

    @Test
    void testSetAuthenticationAppliesIdpsAndSso() {
        doReturn(idpModels).when(authenticationService).setAuthenticationIdps(idpModels);
        doReturn(ssoModel).when(authenticationService).setAuthenticationSso(ssoModel);

        final ServiceResult<AuthenticationModel> result = authenticationService.setAuthentication(
                new AuthenticationModel(idpModels, ssoModel));

        assertEquals(idpModels, result.getModel().getIdps());
        assertEquals(ssoModel, result.getModel().getSso());
        assertEquals(200, result.getStatus().get(FieldNames.of(AuthenticationModel.class, AbstractAuthenticationIdpModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.of(AuthenticationModel.class, AuthenticationSsoModel.class)).getStatus());
    }

    @Test
    void testSetAuthenticationSkipsNullSubFields() {
        final ServiceResult<AuthenticationModel> result =
                authenticationService.setAuthentication(new AuthenticationModel());

        assertTrue(result.getStatus().isEmpty());
    }

    @Test
    void testSetAuthenticationRecordsPerSubFieldFailure() {
        doReturn(idpModels).when(authenticationService).setAuthenticationIdps(idpModels);
        doThrow(new BadRequestException("invalid sso config"))
                .when(authenticationService).setAuthenticationSso(ssoModel);

        final ServiceResult<AuthenticationModel> result = authenticationService.setAuthentication(
                new AuthenticationModel(idpModels, ssoModel));

        assertEquals(idpModels, result.getModel().getIdps());
        assertNull(result.getModel().getSso());
        assertEquals(200, result.getStatus().get(FieldNames.of(AuthenticationModel.class, AbstractAuthenticationIdpModel.class)).getStatus());
        assertEquals(400, result.getStatus().get(FieldNames.of(AuthenticationModel.class, AuthenticationSsoModel.class)).getStatus());
    }
}
