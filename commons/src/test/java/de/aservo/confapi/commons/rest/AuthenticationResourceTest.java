package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.*;
import de.aservo.confapi.commons.rest.impl.TestAuthenticationResourceImpl;
import de.aservo.confapi.commons.service.api.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;

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
        final Collection<AbstractAuthenticationIdpBean> authenticationIdpBeans = Arrays.asList(
                AuthenticationIdpOidcBean.EXAMPLE_1,
                AuthenticationIdpSamlBean.EXAMPLE_1
        );
        final AuthenticationIdpsBean authenticationIdpsBean = new AuthenticationIdpsBean(authenticationIdpBeans);
        doReturn(authenticationIdpsBean).when(authenticationService).getAuthenticationIdps();

        final Response response = resource.getAuthenticationIdps();
        assertEquals(200, response.getStatus());

        final AuthenticationIdpsBean authenticationIdpsBeanResponse = (AuthenticationIdpsBean) response.getEntity();
        assertEquals(authenticationIdpsBean, authenticationIdpsBeanResponse);
    }

    @Test
    void testSetAuthenticationIdps() {
        final Collection<AbstractAuthenticationIdpBean> authenticationIdpBeans = Arrays.asList(
                AuthenticationIdpOidcBean.EXAMPLE_1,
                AuthenticationIdpSamlBean.EXAMPLE_1
        );
        final AuthenticationIdpsBean authenticationIdpsBean = new AuthenticationIdpsBean(authenticationIdpBeans);
        doReturn(authenticationIdpsBean).when(authenticationService).setAuthenticationIdps(authenticationIdpsBean);

        final Response response = resource.setAuthenticationIdps(authenticationIdpsBean);
        assertEquals(200, response.getStatus());

        final AuthenticationIdpsBean authenticationIdpsBeanResponse = (AuthenticationIdpsBean) response.getEntity();
        assertEquals(authenticationIdpsBean, authenticationIdpsBeanResponse);
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
