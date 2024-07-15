package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractUsersResourceFuncTest {

    private static final String PARAM_USERNAME = "username";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetUser() throws Exception {
        final UserBean exampleBean = getExampleBean();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USERS + getUserNameQueryParam(exampleBean))
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), usersResponse.statusCode());

        final UserBean userBean = objectMapper.readValue(usersResponse.body(), UserBean.class);
        assertNotNull(userBean);
    }

    @Test
    void testSetUserEmailAddress() throws Exception {
        final UserBean exampleBean = getExampleBean();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USERS + getUserNameQueryParam(exampleBean))
                .request(HttpMethod.PUT, exampleBean);
        assertEquals(Response.Status.OK.getStatusCode(), usersResponse.statusCode());
        
        final UserBean userBean = objectMapper.readValue(usersResponse.body(), UserBean.class);
        assertEquals(exampleBean.getEmail(), userBean.getEmail());
    }

    @Test
    void testSetUserPassword() throws Exception {
        final UserBean exampleBean = getExampleBean();
        final HttpResponse<String> usersResponse = HttpRequestHelper
                .builder(BootstrAPI.USERS + "/" + BootstrAPI.USER_PASSWORD + getUserNameQueryParam(exampleBean))
                .contentMediaType(MediaType.TEXT_PLAIN)
                .request(HttpMethod.PUT, exampleBean.getPassword());
        assertEquals(Response.Status.OK.getStatusCode(), usersResponse.statusCode());
    }

    @Test
    public void testGetUserUnauthenticated() throws Exception {
        final UserBean exampleBean = getExampleBean();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("wrong")
                .password("password")
                .request();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), usersResponse.statusCode());
    }

    @Test
    public void testSetUserEmailAddressUnauthenticated() throws Exception {
        final UserBean exampleBean = getExampleBean();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, exampleBean);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), usersResponse.statusCode());
    }

    @Test
    void testGetUserUnauthorized() throws Exception {
        final UserBean exampleBean = getExampleBean();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("user")
                .password("user")
                .request();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), usersResponse.statusCode());
    }

    @Test
    void testSetUserEmailAddressUnauthorized() throws Exception {
        final UserBean exampleBean = getExampleBean();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USERS + getUserNameQueryParam(exampleBean))
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, exampleBean);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), usersResponse.statusCode());
    }

    protected String getUserNameQueryParam(UserBean userBean) {
        return "?" + PARAM_USERNAME + "=" + userBean.getUsername();
    }

    protected UserBean getExampleBean() {
        return UserBean.EXAMPLE_3_ADMIN;
    }
}
