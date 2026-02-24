package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SettingsCustomHtmlResourceFuncTest {

    private static final String SETTINGS_CUSTOM_HTML_PATH = BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_CUSTOM_HTML;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetCustomHtml() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_CUSTOM_HTML_PATH)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());

        final SettingsCustomHtmlModel model = objectMapper.readValue(response.body(), SettingsCustomHtmlModel.class);
        assertNotNull(model);
    }

    @Test
    void testSetCustomHtml() throws Exception {
        final SettingsCustomHtmlModel exampleModel = SettingsCustomHtmlModel.EXAMPLE_1;

        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_CUSTOM_HTML_PATH)
                .request(HttpMethod.PUT, exampleModel);
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());

        final SettingsCustomHtmlModel model = objectMapper.readValue(response.body(), SettingsCustomHtmlModel.class);
        assertEquals(exampleModel, model);
    }

    @Test
    void testGetCustomHtmlUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_CUSTOM_HTML_PATH)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetCustomHtmlUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_CUSTOM_HTML_PATH)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, SettingsCustomHtmlModel.EXAMPLE_1);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testGetCustomHtmlUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_CUSTOM_HTML_PATH)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetCustomHtmlUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_CUSTOM_HTML_PATH)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, SettingsCustomHtmlModel.EXAMPLE_1);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }
}
