package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractApplicationLinksResourceFuncTest {

    @Test
    void testGetApplicationLinks() throws Exception {
        final HttpResponse<String> applicationLinksResource = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS).request();
        assertEquals(Response.Status.OK.getStatusCode(), applicationLinksResource.statusCode());
    }

    @Test
    public void testGetApplicationLinksUnauthenticated() throws Exception {
        final HttpResponse<String> applicationLinksResource = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS)
                .username("wrong")
                .password("password")
                .request();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), applicationLinksResource.statusCode());
    }

    @Test
    void testGetApplicationLinksUnauthorized() throws Exception {
        final HttpResponse<String> applicationLinksResource = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS)
                .username("user")
                .password("user")
                .request();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), applicationLinksResource.statusCode());
    }
}
