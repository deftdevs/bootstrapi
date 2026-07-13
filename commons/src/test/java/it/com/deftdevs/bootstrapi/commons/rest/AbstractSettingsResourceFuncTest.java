package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import java.net.http.HttpResponse;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Functional tests for the composite {@code /settings} endpoint. Like the
 * _all endpoint tests, the happy path only submits the general settings,
 * since that is the only group that can be applied safely and
 * deterministically on all product test instances.
 */
public abstract class AbstractSettingsResourceFuncTest {

    private static final String SETTINGS_PATH = BootstrAPI.SETTINGS;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetSettingsContainsGeneral() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(SETTINGS_PATH)
                .request();
        assertEquals(200, settingsResponse.statusCode());

        final JsonNode settingsNode = objectMapper.readTree(settingsResponse.body());
        assertFalse(settingsNode.path(FieldNames.of(SettingsGeneralModel.class)).isMissingNode(),
                "expected a general settings group, got: " + settingsNode);
    }

    @Test
    void testSetSettingsAppliesGeneral() throws Exception {
        final String generalKey = FieldNames.of(SettingsGeneralModel.class);

        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(SETTINGS_PATH)
                .request(HttpMethod.PUT, Collections.singletonMap(generalKey, getExampleSettingsGeneralModel()));
        assertEquals(200, settingsResponse.statusCode(), settingsResponse.body());

        final JsonNode settingsNode = objectMapper.readTree(settingsResponse.body());
        assertEquals(200, settingsNode.path("status").path(generalKey).path("status").asInt(),
                "expected a success entry for '" + generalKey + "' in the status map, got: " + settingsNode.path("status"));
        assertEquals(getExampleSettingsGeneralModel().getTitle(),
                settingsNode.path(generalKey).path("title").asText());
    }

    @Test
    void testSetSettingsWithoutBodyIsBadRequest() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(SETTINGS_PATH)
                .request(HttpMethod.PUT, null);
        assertEquals(400, settingsResponse.statusCode());
    }

    @Test
    void testSetSettingsEmptyModelIsOkWithEmptyStatus() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(SETTINGS_PATH)
                .request(HttpMethod.PUT, Collections.emptyMap());
        assertEquals(200, settingsResponse.statusCode());

        final JsonNode settingsNode = objectMapper.readTree(settingsResponse.body());
        assertTrue(settingsNode.path("status").isEmpty(),
                "expected an empty status map, got: " + settingsNode.path("status"));
    }

    @Test
    void testSetSettingsUnauthenticated() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(SETTINGS_PATH)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, Collections.emptyMap());
        assertEquals(401, settingsResponse.statusCode());
    }

    @Test
    void testSetSettingsUnauthorized() throws Exception {
        final HttpResponse<String> settingsResponse = HttpRequestHelper.builder(SETTINGS_PATH)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, Collections.emptyMap());
        assertEquals(403, settingsResponse.statusCode());
    }

    protected SettingsGeneralModel getExampleSettingsGeneralModel() {
        return SettingsGeneralModel.EXAMPLE_1;
    }
}
