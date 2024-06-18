package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractUserResourceFuncTest {

    private static final String PARAM_USERNAME = "username";

    @Test
    void testGetUser() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean)).build();

        ClientResponse clientResponse = usersResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        UserBean userBean = clientResponse.getEntity(UserBean.class);
        assertNotNull(userBean);
    }

    @Test
    void testSetUserEmailAddress() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean)).build();

        ClientResponse clientResponse = usersResource.put(exampleBean);
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        UserBean userBean = clientResponse.getEntity(UserBean.class);
        assertEquals(exampleBean.getEmail(), userBean.getEmail());
    }

    @Test
    void testSetUserPassword() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder
                .builder(ConfAPI.USERS + "/" + ConfAPI.USER_PASSWORD + getUserNameQueryParam(exampleBean))
                .contentMediaType(MediaType.TEXT_PLAIN)
                .build();

        ClientResponse clientResponse = usersResource.put(exampleBean.getPassword());
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());
    }

    @Test
    public void testGetUserUnauthenticated() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("wrong")
                .password("password")
                .build();

        assertThrows(ClientAuthenticationException.class, usersResource::get);
    }

    @Test
    public void testSetUserEmailAddressUnauthenticated() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("wrong")
                .password("password")
                .build();

        assertThrows(ClientAuthenticationException.class, () -> {
            usersResource.put(exampleBean);
        });
    }

    @Test
    void testGetUserUnauthorized() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("user")
                .password("user")
                .build();
        usersResource.get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), usersResource.put(getExampleBean()).getStatusCode());
    }

    @Test
    void testSetUserEmailAddressUnauthorized() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("user")
                .password("user")
                .build();
        usersResource.put(exampleBean);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), usersResource.put(getExampleBean()).getStatusCode());
    }

    protected String getUserNameQueryParam(UserBean userBean) {
        return "?" + PARAM_USERNAME + "=" + userBean.getUsername();
    }

    protected UserBean getExampleBean() {
        return UserBean.EXAMPLE_3_ADMIN;
    }
}
