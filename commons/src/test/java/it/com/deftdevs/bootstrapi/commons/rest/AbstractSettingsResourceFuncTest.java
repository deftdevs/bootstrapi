package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractSettingsResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetSettings() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), settingsResponse.statusCode());

        final SettingsBean settingsBean = objectMapper.readValue(settingsResponse.body(), SettingsBean.class);
        assertNotNull(settingsBean.getTitle());
    }

    @Test
    void testSetSettings() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS)
                .request(HttpMethod.PUT, getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), settingsResponse.statusCode());

        final SettingsBean settingsBean = objectMapper.readValue(settingsResponse.body(), SettingsBean.class);
        assertEquals(getExampleBean(), settingsBean);
    }

    @Test
    public void testGetSettingsUnauthenticated() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS)
                .username("wrong")
                .password("password")
                .request();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), settingsResponse.statusCode());
    }

    @Test
    public void testSetSettingsUnauthenticated() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, getExampleBean());
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), settingsResponse.statusCode());
    }

    @Test
    void testGetSettingsUnauthorized() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), settingsResponse.statusCode());
    }

    @Test
    void testSetSettingsUnauthorized() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, getExampleBean());

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), settingsResponse.statusCode());
    }

    protected SettingsBean getExampleBean() {
        return SettingsBean.EXAMPLE_1;
    }
}
