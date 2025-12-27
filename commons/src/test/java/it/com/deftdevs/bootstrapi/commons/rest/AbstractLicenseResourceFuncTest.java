package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractLicenseResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetLicenses() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), licensesResponse.statusCode());

        final List<LicenseModel> licenseModels = objectMapper.readValue(licensesResponse.body(), new TypeReference<List<LicenseModel>>(){});
        assertNotNull(licenseModels);
        assertNotEquals(0, licenseModels.size());
        assertNotNull(licenseModels.iterator().next().getOrganization());
    }

    @Test
    void testSetLicenses() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .request(HttpMethod.PUT, getExampleModel());
        assertEquals(Response.Status.OK.getStatusCode(), licensesResponse.statusCode());

        final List<LicenseModel> licenseModels = objectMapper.readValue(licensesResponse.body(), new TypeReference<List<LicenseModel>>(){});
        assertEquals(getExampleModel().iterator().next().getDescription(), licenseModels.iterator().next().getDescription());
    }

    @Test
    void testAddLicenses() throws Exception {
        final LicenseModel licenseModel = getExampleModel().iterator().next();
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .request(HttpMethod.POST, licenseModel);
        assertEquals(Response.Status.OK.getStatusCode(), licensesResponse.statusCode());

        final List<LicenseModel> licenseModels = objectMapper.readValue(licensesResponse.body(), new TypeReference<List<LicenseModel>>(){});
        assertEquals(licenseModel.getDescription(), licenseModels.iterator().next().getDescription());
    }

    @Test
    public void testGetLicensesUnauthenticated() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), licensesResponse.statusCode());
    }

    @Test
    public void testSetLicensesUnauthenticated() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, getExampleModel());

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

    @Test
    void testSetLicensesUnauthorized() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, getExampleModel());

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), licensesResponse.statusCode());
    }

    protected List<LicenseModel> getExampleModel() {
        return Collections.singletonList(LicenseModel.EXAMPLE_2_DEVELOPER_LICENSE);
    }
}
