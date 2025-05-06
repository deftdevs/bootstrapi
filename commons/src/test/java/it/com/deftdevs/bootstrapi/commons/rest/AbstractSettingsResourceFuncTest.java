package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractSettingsResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetSettings() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), settingsResponse.statusCode());

        final SettingsModel settingsModel = objectMapper.readValue(settingsResponse.body(), SettingsModel.class);
        assertNotNull(settingsModel.getTitle());
    }

    @Test
    void testSetSettings() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS)
                .request(HttpMethod.PUT, getExampleModel());
        assertEquals(Response.Status.OK.getStatusCode(), settingsResponse.statusCode());

        final SettingsModel settingsModel = objectMapper.readValue(settingsResponse.body(), SettingsModel.class);
        assertEquals(getExampleModel(), settingsModel);
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
                .request(HttpMethod.PUT, getExampleModel());
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
                .request(HttpMethod.PUT, getExampleModel());

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), settingsResponse.statusCode());
    }

    protected SettingsModel getExampleModel() {
        return SettingsModel.EXAMPLE_1;
    }
}
