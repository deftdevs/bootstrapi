package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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

        final List<LicenseBean> licenseBeans = objectMapper.readValue(licensesResponse.body(), new TypeReference<List<LicenseBean>>(){});
        assertNotNull(licenseBeans);
        assertNotEquals(0, ((List<LicenseBean>) licenseBeans).size());
        assertNotNull(((List<LicenseBean>) licenseBeans).iterator().next().getOrganization());
    }

    @Test
    void testSetLicenses() throws Exception {
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .request(HttpMethod.PUT, getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), licensesResponse.statusCode());

        final List<LicenseBean> licenseBeans = objectMapper.readValue(licensesResponse.body(), new TypeReference<List<LicenseBean>>(){});
        assertEquals(getExampleBean().iterator().next().getDescription(), licenseBeans.iterator().next().getDescription());
    }

    @Test
    void testAddLicenses() throws Exception {
        final LicenseBean licenseBean = getExampleBean().iterator().next();
        final HttpResponse<String> licensesResponse = HttpRequestHelper.builder(BootstrAPI.LICENSES)
                .request(HttpMethod.POST, licenseBean);
        assertEquals(Response.Status.OK.getStatusCode(), licensesResponse.statusCode());

        final List<LicenseBean> licenseBeans = objectMapper.readValue(licensesResponse.body(), new TypeReference<List<LicenseBean>>(){});
        assertEquals(licenseBean.getDescription(), licenseBeans.iterator().next().getDescription());
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
                .request(HttpMethod.PUT, getExampleBean());

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
                .request(HttpMethod.PUT, getExampleBean());

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), licensesResponse.statusCode());
    }

    protected List<LicenseBean> getExampleBean() {
        return Collections.singletonList(LicenseBean.EXAMPLE_2_DEVELOPER_LICENSE);
    }
}
