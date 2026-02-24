package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LicensesResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetLicenses() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), licensesResponse.statusCode(), licensesResponse.body());

        final List<LicenseModel> licenseModels = objectMapper.readValue(licensesResponse.body(), new TypeReference<List<LicenseModel>>(){});
        assertNotNull(licenseModels);
        assertFalse(licenseModels.isEmpty());
        assertNotNull(licenseModels.iterator().next().getOrganization());
    }

    @Test
    void testGetLicensesUnauthenticated() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), licensesResponse.statusCode());
    }

    @Test
    void testGetLicensesUnauthorized() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), licensesResponse.statusCode());
    }
}
