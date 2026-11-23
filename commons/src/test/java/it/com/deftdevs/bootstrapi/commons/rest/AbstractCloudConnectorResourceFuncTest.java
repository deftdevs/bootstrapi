package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.CloudConnectorModel;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Creating a working cloud connector requires credentials generated in
 * Atlassian Administration for the base URL of the test instance, so these
 * tests cover the paths that reach the applinks plugin without them. A 400
 * from the plugin's own credential validation (instead of a 503) proves that
 * the plugin's backbone connection service is found and invoked.
 */
public abstract class AbstractCloudConnectorResourceFuncTest {

    @Test
    void testGetCloudConnectorNotConfigured() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.CLOUD_CONNECTOR).request();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.statusCode(), response.body());
    }

    @Test
    void testSetCloudConnectorWithInvalidCredentials() throws Exception {
        final CloudConnectorModel cloudConnectorModel = CloudConnectorModel.builder()
                .credentials("invalid")
                .build();

        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.CLOUD_CONNECTOR)
                .request(HttpMethod.PUT, cloudConnectorModel);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.statusCode(), response.body());
    }

    @Test
    void testSetCloudConnectorWithCredentialsForOtherBaseUrl() throws Exception {
        final CloudConnectorModel cloudConnectorModel = CloudConnectorModel.builder()
                .credentials(credentials("https://other.example.com"))
                .build();

        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.CLOUD_CONNECTOR)
                .request(HttpMethod.PUT, cloudConnectorModel);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.statusCode(), response.body());

        // the failed attempt must not have created a connector
        final HttpResponse<String> getResponse = HttpRequestHelper.builder(BootstrAPI.CLOUD_CONNECTOR).request();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), getResponse.statusCode(), getResponse.body());
    }

    @Test
    void testDeleteCloudConnectorNotConfigured() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.CLOUD_CONNECTOR)
                .request(HttpMethod.DELETE, null);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.statusCode(), response.body());
    }

    @Test
    void testGetCloudConnectorUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.CLOUD_CONNECTOR)
                .username("user")
                .password("user")
                .request();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }

    // well-formed credentials in the format generated in Atlassian Administration
    private static String credentials(
            final String baseUrl) {

        final String json = "{"
                + "\"linkId\":\"func-test-link\","
                + "\"orgId\":\"func-test-org\","
                + "\"orgName\":\"Func Test Organization\","
                + "\"name\":\"Func Test Connector\","
                + "\"baseUrl\":\"" + baseUrl + "\","
                + "\"oAuth2Credentials\":{"
                + "\"clientId\":\"func-test-client\","
                + "\"clientSecret\":\"func-test-secret\","
                + "\"tokenEndpoint\":\"https://auth.atlassian.com/oauth/token\","
                + "\"scopes\":[]"
                + "}}";

        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

}
