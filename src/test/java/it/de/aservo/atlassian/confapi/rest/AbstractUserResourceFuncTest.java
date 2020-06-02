package it.de.aservo.atlassian.confapi.rest;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.UserBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractUserResourceFuncTest {

    private static final String PARAM_USERNAME = "userName";

    @Test
    public void testGetUser() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean)).build();

        ClientResponse clientResponse = usersResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        UserBean userBean = clientResponse.getEntity(UserBean.class);
        assertNotNull(userBean);
    }

    @Test
    public void testSetUserEmailAddress() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean)).build();

        ClientResponse clientResponse = usersResource.put(exampleBean);
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        UserBean userBean = clientResponse.getEntity(UserBean.class);
        assertEquals(exampleBean, userBean);
    }

    @Test
    public void testSetUserPassword() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder
                .builder(ConfAPI.USERS + "/" + ConfAPI.USER_PASSWORD + getUserNameQueryParam(exampleBean))
                .contentMediaType(MediaType.TEXT_PLAIN)
                .build();

        ClientResponse clientResponse = usersResource.put(exampleBean.getPassword());
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        UserBean userBean = clientResponse.getEntity(UserBean.class);
        assertEquals(exampleBean, userBean);
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testGetUserUnauthenticated() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("wrong")
                .password("password")
                .build();
        usersResource.get();
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testSetUserEmailAddressUnauthenticated() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("wrong")
                .password("password")
                .build();
        usersResource.put(exampleBean);
    }

    @Test
    @Ignore("cannot be executed because there is no default user with restricted access rights")
    public void testGetUserUnauthorized() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("user")
                .password("user")
                .build();
        usersResource.get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), usersResource.put(getExampleBean()).getStatusCode());
    }

    @Test
    @Ignore("cannot be executed because there is no default user with restricted access rights")
    public void testSetUserEmailAddressUnauthorized() {
        UserBean exampleBean = getExampleBean();
        Resource usersResource = ResourceBuilder.builder(ConfAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("user")
                .password("user")
                .build();
        usersResource.put(exampleBean);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), usersResource.put(getExampleBean()).getStatusCode());
    }

    protected String getUserNameQueryParam(UserBean userBean) {
        return "?" + PARAM_USERNAME + "=" + userBean.getUserName();
    }

    protected UserBean getExampleBean() {
        return UserBean.EXAMPLE_3_ADMIN;
    }
}
