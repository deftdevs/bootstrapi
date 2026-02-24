package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.crowd.model.SessionConfigModel;
import com.deftdevs.bootstrapi.crowd.rest.api.SessionConfigResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SessionConfigResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetSessionConfig() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SessionConfigResource.SESSION_CONFIG)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());

        final SessionConfigModel sessionConfigModel = objectMapper.readValue(response.body(), SessionConfigModel.class);
        assertNotNull(sessionConfigModel);
        assertNotNull(sessionConfigModel.getSessionTimeoutInMinutes());
        assertNotNull(sessionConfigModel.getRequireConsistentClientIP());
    }

    @Test
    void testSetSessionConfig() throws Exception {
        final SessionConfigModel exampleModel = SessionConfigModel.EXAMPLE_2;

        final HttpResponse<String> response = HttpRequestHelper.builder(SessionConfigResource.SESSION_CONFIG)
                .request(HttpMethod.PUT, exampleModel);
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());

        final SessionConfigModel sessionConfigModel = objectMapper.readValue(response.body(), SessionConfigModel.class);
        assertEquals(exampleModel.getSessionTimeoutInMinutes(), sessionConfigModel.getSessionTimeoutInMinutes());
        assertEquals(exampleModel.getRequireConsistentClientIP(), sessionConfigModel.getRequireConsistentClientIP());
    }

    @Test
    void testGetSessionConfigUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SessionConfigResource.SESSION_CONFIG)
                .username("wrong")
                .password("password")
                .request();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetSessionConfigUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SessionConfigResource.SESSION_CONFIG)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, SessionConfigModel.EXAMPLE_2);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testGetSessionConfigUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SessionConfigResource.SESSION_CONFIG)
                .username("user")
                .password("user")
                .request();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetSessionConfigUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SessionConfigResource.SESSION_CONFIG)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, SessionConfigModel.EXAMPLE_2);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }
}
