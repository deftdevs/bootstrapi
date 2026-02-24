package it.com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SettingsBannerResourceFuncTest {

    private static final String SETTINGS_BANNER_PATH = BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BANNER;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetBanner() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_BANNER_PATH)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());

        final SettingsBannerModel bannerModel = objectMapper.readValue(response.body(), SettingsBannerModel.class);
        assertNotNull(bannerModel);
    }

    @Test
    void testSetBanner() throws Exception {
        final SettingsBannerModel exampleModel = SettingsBannerModel.EXAMPLE_1;

        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_BANNER_PATH)
                .request(HttpMethod.PUT, exampleModel);
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());

        final SettingsBannerModel bannerModel = objectMapper.readValue(response.body(), SettingsBannerModel.class);
        assertEquals(exampleModel, bannerModel);
    }

    @Test
    void testGetBannerUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_BANNER_PATH)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetBannerUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_BANNER_PATH)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, SettingsBannerModel.EXAMPLE_1);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testGetBannerUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_BANNER_PATH)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetBannerUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(SETTINGS_BANNER_PATH)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, SettingsBannerModel.EXAMPLE_1);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }
}
