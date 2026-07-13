package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.HttpMethod;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractSettingsGeneralResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetSettings() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_GENERAL)
                .request();
        assertEquals(200, settingsResponse.statusCode());

        final SettingsGeneralModel settingsModel = objectMapper.readValue(settingsResponse.body(), SettingsGeneralModel.class);
        assertNotNull(settingsModel.getTitle());
    }

    @Test
    void testSetSettings() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_GENERAL)
                .request(HttpMethod.PUT, getExampleModel());
        assertEquals(200, settingsResponse.statusCode());

        final SettingsGeneralModel settingsModel = objectMapper.readValue(settingsResponse.body(), SettingsGeneralModel.class);
        assertEquals(getExampleModel(), settingsModel);
    }

    @Test
    public void testGetSettingsUnauthenticated() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_GENERAL)
                .username("wrong")
                .password("password")
                .request();
        assertEquals(401, settingsResponse.statusCode());
    }

    @Test
    public void testSetSettingsUnauthenticated() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_GENERAL)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, getExampleModel());
        assertEquals(401, settingsResponse.statusCode());
    }

    @Test
    void testGetSettingsUnauthorized() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_GENERAL)
                .username("user")
                .password("user")
                .request();

        assertEquals(403, settingsResponse.statusCode());
    }

    @Test
    void testSetSettingsUnauthorized() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_GENERAL)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, getExampleModel());

        assertEquals(403, settingsResponse.statusCode());
    }

    protected SettingsGeneralModel getExampleModel() {
        return SettingsGeneralModel.EXAMPLE_1;
    }
}
