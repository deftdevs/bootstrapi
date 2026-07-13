package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Functional tests for the {@code PUT /} (apply complete configuration) endpoint.
 * <p>
 * The happy-path test only submits the general settings sub-field, since that
 * is the only configuration that can be applied safely and deterministically
 * on all product test instances. The per-sub-field status aggregation and
 * partial-failure behavior are covered by unit tests.
 */
public abstract class _AbstractAllResourceFuncTest {

    /** The _all endpoint is the REST root itself. */
    private static final String ALL_PATH = "";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSetAllAppliesGeneralSettings() throws Exception {
        final String settingsGeneralKey = getSettingsGeneralStatusKey();

        final HttpResponse<String> allResponse = HttpRequestHelper.builder(ALL_PATH)
                .request(HttpMethod.PUT, getExampleAllModel());
        assertEquals(200, allResponse.statusCode());

        final JsonNode allNode = objectMapper.readTree(allResponse.body());

        final JsonNode generalStatusNode = allNode.path("status").path(settingsGeneralKey);
        assertFalse(generalStatusNode.isMissingNode(),
                "expected a '" + settingsGeneralKey + "' entry in the status map, got: " + allNode.path("status"));
        assertEquals(200, generalStatusNode.path("status").asInt());

        // the status key mirrors the request's field path, so it also locates the model in the body
        JsonNode generalNode = allNode;
        for (final String segment : settingsGeneralKey.split("/")) {
            generalNode = generalNode.path(segment);
        }
        assertEquals(getExampleSettingsGeneralModel().getTitle(), generalNode.path("title").asText());
    }

    @Test
    void testSetAllWithoutBodyIsBadRequest() throws Exception {
        final HttpResponse<String> allResponse = HttpRequestHelper.builder(ALL_PATH)
                .request(HttpMethod.PUT, null);
        assertEquals(400, allResponse.statusCode());
    }

    @Test
    void testSetAllEmptyModelIsOkWithEmptyStatus() throws Exception {
        final HttpResponse<String> allResponse = HttpRequestHelper.builder(ALL_PATH)
                .request(HttpMethod.PUT, Collections.emptyMap());
        assertEquals(200, allResponse.statusCode());

        final JsonNode allNode = objectMapper.readTree(allResponse.body());
        assertTrue(allNode.path("status").isEmpty(),
                "expected an empty status map, got: " + allNode.path("status"));
    }

    /**
     * PUTs the given payload to the _all endpoint, expects an overall 200 and
     * a {@code status} map containing exactly one success entry per given key.
     * Keys are usually derived with {@code ServiceResultUtil} so tests share
     * the production key-derivation logic instead of repeating literals.
     */
    protected void assertSetAllApplied(
            final Object allModel,
            final Collection<String> expectedSuccessStatusKeys) throws Exception {

        final HttpResponse<String> allResponse = HttpRequestHelper.builder(ALL_PATH)
                .request(HttpMethod.PUT, allModel);
        assertEquals(200, allResponse.statusCode(), allResponse.body());

        final JsonNode statusNode = objectMapper.readTree(allResponse.body()).path("status");

        final Set<String> actualKeys = new TreeSet<>();
        statusNode.fieldNames().forEachRemaining(actualKeys::add);
        assertEquals(new TreeSet<>(expectedSuccessStatusKeys), actualKeys,
                "status map keys do not match the submitted sub-fields");

        for (final String key : expectedSuccessStatusKeys) {
            assertEquals(200, statusNode.path(key).path("status").asInt(),
                    "expected a success entry for '" + key + "' in the status map, got: " + statusNode);
        }
    }

    @Test
    void testSetAllUnauthenticated() throws Exception {
        final HttpResponse<String> allResponse = HttpRequestHelper.builder(ALL_PATH)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, getExampleAllModel());
        assertEquals(401, allResponse.statusCode());
    }

    @Test
    void testSetAllUnauthorized() throws Exception {
        final HttpResponse<String> allResponse = HttpRequestHelper.builder(ALL_PATH)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, getExampleAllModel());
        assertEquals(403, allResponse.statusCode());
    }

    /**
     * The product-specific {@code _AllModel} payload to apply. Must contain
     * (at least) the general settings returned by {@link #getExampleSettingsGeneralModel()}.
     */
    protected abstract Object getExampleAllModel();

    /**
     * The status-map key of the general settings, derived from the product's
     * models (e.g. {@code FieldNames.pathOf(_AllModel.class, SettingsGeneralModel.class)}).
     */
    protected abstract String getSettingsGeneralStatusKey();

    protected SettingsGeneralModel getExampleSettingsGeneralModel() {
        return SettingsGeneralModel.EXAMPLE_1;
    }
}
