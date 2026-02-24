package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.crowd.rest.api.TrustedProxiesResource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrustedProxiesResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetTrustedProxies() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());

        final List<String> trustedProxies = objectMapper.readValue(response.body(), new TypeReference<List<String>>() {});
        assertNotNull(trustedProxies);
    }

    @Test
    void testSetTrustedProxies() throws Exception {
        final List<String> proxies = List.of("10.0.0.1", "10.0.0.2");

        final HttpResponse<String> response = HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .request(HttpMethod.PUT, proxies);
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());

        final List<String> resultProxies = objectMapper.readValue(response.body(), new TypeReference<List<String>>() {});
        assertEquals(proxies, resultProxies);
    }

    @Test
    void testAddAndRemoveTrustedProxy() throws Exception {
        // Set empty list first
        HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .request(HttpMethod.PUT, List.of());

        // Add a proxy
        final HttpResponse<String> addResponse = HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .contentMediaType(MediaType.TEXT_PLAIN)
                .request(HttpMethod.POST, "192.168.1.1");
        assertEquals(Response.Status.OK.getStatusCode(), addResponse.statusCode());

        List<String> proxies = objectMapper.readValue(addResponse.body(), new TypeReference<List<String>>() {});
        assertTrue(proxies.contains("192.168.1.1"));

        // Remove the proxy
        final HttpResponse<String> removeResponse = HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .contentMediaType(MediaType.TEXT_PLAIN)
                .request(HttpMethod.DELETE, "192.168.1.1");
        assertEquals(Response.Status.OK.getStatusCode(), removeResponse.statusCode());

        proxies = objectMapper.readValue(removeResponse.body(), new TypeReference<List<String>>() {});
        assertFalse(proxies.contains("192.168.1.1"));
    }

    @Test
    void testGetTrustedProxiesUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .username("wrong")
                .password("password")
                .request();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetTrustedProxiesUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, List.of("10.0.0.1"));
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testGetTrustedProxiesUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .username("user")
                .password("user")
                .request();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetTrustedProxiesUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(TrustedProxiesResource.TRUSTED_PROXIES)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, List.of("10.0.0.1"));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }
}
