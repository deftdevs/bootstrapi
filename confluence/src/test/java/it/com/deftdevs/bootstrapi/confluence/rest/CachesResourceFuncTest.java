package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.CacheModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CachesResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetCaches() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.CACHES)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());

        final List<CacheModel> caches = objectMapper.readValue(response.body(), new TypeReference<List<CacheModel>>(){});
        assertNotNull(caches);
        assertFalse(caches.isEmpty());
    }

    @Test
    void testGetCachesUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.CACHES)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testGetCachesUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.CACHES)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }
}
