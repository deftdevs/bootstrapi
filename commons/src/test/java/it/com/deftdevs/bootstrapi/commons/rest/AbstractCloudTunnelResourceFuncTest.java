package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The cloud application tunnel is provided by the Secure Tunnel Client plugin,
 * which is not bundled with every product. Without it, the endpoint answers
 * with 503. With it, a tunnel is configured from a well-formed key and deleted
 * again. The tunnel cannot connect with that key, which does not affect
 * configuring it.
 */
public abstract class AbstractCloudTunnelResourceFuncTest {

    private static final String PATH = BootstrAPI.CLOUD + "/" + BootstrAPI.CLOUD_TUNNEL;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // the tunnel the key below describes
    private static final CloudTunnelModel EXPECTED_TUNNEL = CloudTunnelModel.EXAMPLE_1;

    // only part of the key, not of the model
    private static final String UPSTREAM_DNS_URL = "https://func-test-upstream.example.com";
    private static final String TOKEN = "func-test-token";

    /**
     * @return whether the product bundles the Secure Tunnel Client plugin
     */
    protected abstract boolean isTunnelPluginInstalled();

    @Test
    void testGetCloudTunnelNotConfigured() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(PATH).request();
        assertEquals(expectedStatus(Response.Status.NOT_FOUND), response.statusCode(), response.body());
    }

    @Test
    void testSetCloudTunnelWithoutKey() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(PATH)
                .request(HttpMethod.PUT, CloudTunnelModel.builder().build());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.statusCode(), response.body());
    }

    @Test
    void testSetCloudTunnelWithInvalidKey() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(PATH)
                .request(HttpMethod.PUT, CloudTunnelModel.builder().key("invalid").build());
        assertEquals(expectedStatus(Response.Status.BAD_REQUEST), response.statusCode(), response.body());
    }

    @Test
    void testSetGetAndDeleteCloudTunnel() throws Exception {
        final HttpResponse<String> setResponse = HttpRequestHelper.builder(PATH)
                .request(HttpMethod.PUT, CloudTunnelModel.builder().key(key()).build());
        assertEquals(expectedStatus(Response.Status.OK), setResponse.statusCode(), setResponse.body());

        if (!isTunnelPluginInstalled()) {
            return;
        }

        try {
            final CloudTunnelModel setModel = OBJECT_MAPPER.readValue(setResponse.body(), CloudTunnelModel.class);
            assertEquals(EXPECTED_TUNNEL.getName(), setModel.getName());
            assertEquals(EXPECTED_TUNNEL.getOrganizationName(), setModel.getOrganizationName());
            assertEquals(EXPECTED_TUNNEL.getCloudPageUrl(), setModel.getCloudPageUrl());
            assertNull(setModel.getKey());
            assertFalse(setResponse.body().contains(TOKEN));

            // applying the same key again is a no-op
            final HttpResponse<String> setAgainResponse = HttpRequestHelper.builder(PATH)
                    .request(HttpMethod.PUT, CloudTunnelModel.builder().key(key()).build());
            assertEquals(Response.Status.OK.getStatusCode(), setAgainResponse.statusCode(), setAgainResponse.body());

            final HttpResponse<String> getResponse = HttpRequestHelper.builder(PATH).request();
            assertEquals(Response.Status.OK.getStatusCode(), getResponse.statusCode(), getResponse.body());
            assertEquals(EXPECTED_TUNNEL.getName(), OBJECT_MAPPER.readValue(getResponse.body(), CloudTunnelModel.class).getName());
        } finally {
            final HttpResponse<String> deleteResponse = HttpRequestHelper.builder(PATH)
                    .request(HttpMethod.DELETE, null);
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), deleteResponse.statusCode(), deleteResponse.body());
        }

        final HttpResponse<String> getDeletedResponse = HttpRequestHelper.builder(PATH).request();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), getDeletedResponse.statusCode(), getDeletedResponse.body());
    }

    @Test
    void testDeleteCloudTunnelNotConfigured() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(PATH)
                .request(HttpMethod.DELETE, null);
        assertEquals(expectedStatus(Response.Status.NOT_FOUND), response.statusCode(), response.body());
    }

    @Test
    void testGetCloudTunnelUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(PATH)
                .username("user")
                .password("user")
                .request();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }

    private int expectedStatus(
            final Response.Status statusWithPlugin) {

        return isTunnelPluginInstalled()
                ? statusWithPlugin.getStatusCode()
                : Response.Status.SERVICE_UNAVAILABLE.getStatusCode();
    }

    // a well-formed key in the format generated when creating the tunnel in the cloud:
    // the Base64-encoded JSON of the plugin's TunnelConfigurationKey
    private static String key() throws JsonProcessingException {
        final Map<String, String> tunnelConfigurationKey = Map.of(
                "name", EXPECTED_TUNNEL.getName(),
                "cloudOrgName", EXPECTED_TUNNEL.getOrganizationName(),
                "cloudPageUrl", EXPECTED_TUNNEL.getCloudPageUrl(),
                "upstreamDnsUrl", UPSTREAM_DNS_URL,
                "token", TOKEN);

        return Base64.getEncoder().encodeToString(OBJECT_MAPPER.writeValueAsBytes(tunnelConfigurationKey));
    }

}
