package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Guards the branding resource's DI wiring: {@code SettingsBrandingService} must resolve
 * to exactly one bean even though the composite {@code ConfluenceSettingsService} also
 * implements it (see the note in {@code ServiceConfig#confluenceSettingsService()}).
 */
public class SettingsBrandingResourceFuncTest {

    private static final String COLOR_SCHEME_PATH =
            BootstrAPI.SETTINGS + "/" + BootstrAPI.SETTINGS_BRANDING + "/" + BootstrAPI.COLOR_SCHEME;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetColourScheme() throws Exception {
        final HttpResponse<String> colourSchemeResponse = HttpRequestHelper.builder(COLOR_SCHEME_PATH)
                .request();
        assertEquals(200, colourSchemeResponse.statusCode());

        final SettingsBrandingColorSchemeModel colorSchemeModel =
                objectMapper.readValue(colourSchemeResponse.body(), SettingsBrandingColorSchemeModel.class);
        assertNotNull(colorSchemeModel);
    }

    @Test
    void testGetColourSchemeUnauthenticated() throws Exception {
        final HttpResponse<String> colourSchemeResponse = HttpRequestHelper.builder(COLOR_SCHEME_PATH)
                .username("wrong")
                .password("password")
                .request();
        assertEquals(401, colourSchemeResponse.statusCode());
    }
}
