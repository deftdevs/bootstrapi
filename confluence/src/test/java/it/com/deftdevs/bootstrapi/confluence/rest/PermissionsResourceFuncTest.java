package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PermissionsResourceFuncTest {

    private static final String PERMISSIONS_GLOBAL_PATH = BootstrAPI.PERMISSIONS + "/" + BootstrAPI.PERMISSIONS_GLOBAL;

    @Test
    void testGetPermissionsGlobal() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(PERMISSIONS_GLOBAL_PATH)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());
    }

    @Test
    void testGetPermissionsGlobalUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(PERMISSIONS_GLOBAL_PATH)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testGetPermissionsGlobalUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(PERMISSIONS_GLOBAL_PATH)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }
}
