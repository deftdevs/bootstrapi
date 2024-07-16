package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpOidcBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpSamlBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
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
        final List<AbstractAuthenticationIdpBean> authenticationIdpBeans = Arrays.asList(
                AuthenticationIdpOidcBean.EXAMPLE_1,
                AuthenticationIdpSamlBean.EXAMPLE_1
        );
        doReturn(authenticationIdpBeans).when(authenticationService).getAuthenticationIdps();

        final Response response = resource.getAuthenticationIdps();
        assertEquals(200, response.getStatus());

        final List<AbstractAuthenticationIdpBean> authenticationIdpBeansResponse = (List<AbstractAuthenticationIdpBean>) response.getEntity();
        assertEquals(authenticationIdpBeans, authenticationIdpBeansResponse);
    }

    @Test
    void testSetAuthenticationIdps() {
        final List<AbstractAuthenticationIdpBean> authenticationIdpBeans = Arrays.asList(
                AuthenticationIdpOidcBean.EXAMPLE_1,
                AuthenticationIdpSamlBean.EXAMPLE_1
        );
        doReturn(authenticationIdpBeans).when(authenticationService).setAuthenticationIdps(authenticationIdpBeans);

        final Response response = resource.setAuthenticationIdps(authenticationIdpBeans);
        assertEquals(200, response.getStatus());

        final List<AbstractAuthenticationIdpBean> authenticationIdpBeansResponse = (List<AbstractAuthenticationIdpBean>) response.getEntity();
        assertEquals(authenticationIdpBeans, authenticationIdpBeansResponse);
    }

    @Test
    void testGetAuthenticationSso() {
        final AuthenticationSsoBean authenticationSsoBean = AuthenticationSsoBean.EXAMPLE_1;
        doReturn(authenticationSsoBean).when(authenticationService).getAuthenticationSso();

        final Response response = resource.getAuthenticationSso();
        assertEquals(200, response.getStatus());

        final AuthenticationSsoBean authenticationSsoBeanResponse = (AuthenticationSsoBean) response.getEntity();
        assertEquals(authenticationSsoBean, authenticationSsoBeanResponse);
    }

    @Test
    void testSetAuthenticationSso() {
        final AuthenticationSsoBean authenticationSsoBean = AuthenticationSsoBean.EXAMPLE_1;
        doReturn(authenticationSsoBean).when(authenticationService).setAuthenticationSso(authenticationSsoBean);

        final Response response = resource.setAuthenticationSso(authenticationSsoBean);
        assertEquals(200, response.getStatus());

        final AuthenticationSsoBean authenticationSsoBeanResponse = (AuthenticationSsoBean) response.getEntity();
        assertEquals(authenticationSsoBean, authenticationSsoBeanResponse);
    }

}
