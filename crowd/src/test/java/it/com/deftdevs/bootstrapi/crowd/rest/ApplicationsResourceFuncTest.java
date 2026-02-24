package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationsResourceFuncTest {

    @Test
    void testGetApplications() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.APPLICATIONS)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());
    }

    @Test
    void testGetApplicationsUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.APPLICATIONS)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testGetApplicationsUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(BootstrAPI.APPLICATIONS)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }
}
