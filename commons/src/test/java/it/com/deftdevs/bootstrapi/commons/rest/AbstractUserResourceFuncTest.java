package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractUserResourceFuncTest {

    private static final String PARAM_USERNAME = "username";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetUser() throws Exception {
        final UserModel exampleModel = getExampleModel();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USER + getUserNameQueryParam(exampleModel))
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), usersResponse.statusCode());

        final UserModel userModel = objectMapper.readValue(usersResponse.body(), UserModel.class);
        assertNotNull(userModel);
    }

    @Test
    void testSetUserEmailAddress() throws Exception {
        final UserModel exampleModel = getExampleModel();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USER + getUserNameQueryParam(exampleModel))
                .request(HttpMethod.PUT, exampleModel);
        assertEquals(Response.Status.OK.getStatusCode(), usersResponse.statusCode());

        final UserModel userModel = objectMapper.readValue(usersResponse.body(), UserModel.class);
        assertEquals(exampleModel.getEmail(), userModel.getEmail());
    }

    @Test
    void testSetUserPassword() throws Exception {
        final UserModel exampleModel = getExampleModel();
        final HttpResponse<String> usersResponse = HttpRequestHelper
                .builder(BootstrAPI.USER + "/" + BootstrAPI.USER_PASSWORD + getUserNameQueryParam(exampleModel))
                .contentMediaType(MediaType.TEXT_PLAIN)
                .request(HttpMethod.PUT, exampleModel.getPassword());
        assertEquals(Response.Status.OK.getStatusCode(), usersResponse.statusCode());
    }

    @Test
    public void testGetUserUnauthenticated() throws Exception {
        final UserModel exampleModel = getExampleModel();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USER + getUserNameQueryParam(exampleModel))
                .username("wrong")
                .password("password")
                .request();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), usersResponse.statusCode());
    }

    @Test
    public void testSetUserEmailAddressUnauthenticated() throws Exception {
        final UserModel exampleModel = getExampleModel();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USER + getUserNameQueryParam(exampleModel))
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, exampleModel);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), usersResponse.statusCode());
    }

    @Test
    void testGetUserUnauthorized() throws Exception {
        final UserModel exampleModel = getExampleModel();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USER + getUserNameQueryParam(exampleModel))
                .username("user")
                .password("user")
                .request();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), usersResponse.statusCode());
    }

    @Test
    void testSetUserEmailAddressUnauthorized() throws Exception {
        final UserModel exampleModel = getExampleModel();
        final HttpResponse<String> usersResponse = HttpRequestHelper.builder(BootstrAPI.USER + getUserNameQueryParam(exampleModel))
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, exampleModel);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), usersResponse.statusCode());
    }

    protected String getUserNameQueryParam(UserModel userModel) {
        return "?" + PARAM_USERNAME + "=" + userModel.getUsername();
    }

    protected UserModel getExampleModel() {
        return UserModel.EXAMPLE_3_ADMIN;
    }
}
