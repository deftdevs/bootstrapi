package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractApplicationLinksResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetApplicationLinks() throws Exception {
        final HttpResponse<String> applicationLinksResource = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS).request();
        assertEquals(Response.Status.OK.getStatusCode(), applicationLinksResource.statusCode());
    }

    @Test
    void testSetApplicationLinks() throws Exception {
        final ApplicationLinkModel applicationLinkModel = getExampleModel();

        final HttpResponse<String> applicationLinksResponse = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS)
                .request(HttpMethod.PUT, Collections.singletonMap(applicationLinkModel.getName(), applicationLinkModel));
        assertEquals(Response.Status.OK.getStatusCode(), applicationLinksResponse.statusCode(), applicationLinksResponse.body());

        final Map<String, ApplicationLinkModel> applicationLinkModels = objectMapper.readValue(applicationLinksResponse.body(), new TypeReference<Map<String, ApplicationLinkModel>>(){});
        assertTrue(applicationLinkModels.containsKey(applicationLinkModel.getName()));

        final ApplicationLinkModel responseApplicationLinkModel = applicationLinkModels.get(applicationLinkModel.getName());
        assertEquals(applicationLinkModel.getRpcUrl(), responseApplicationLinkModel.getRpcUrl());
    }

    @Test
    public void testGetApplicationLinksUnauthenticated() throws Exception {
        final HttpResponse<String> applicationLinksResource = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS)
                .username("wrong")
                .password("password")
                .request();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), applicationLinksResource.statusCode());
    }

    @Test
    void testGetApplicationLinksUnauthorized() throws Exception {
        final HttpResponse<String> applicationLinksResource = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS)
                .username("user")
                .password("user")
                .request();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), applicationLinksResource.statusCode());
    }

    protected ApplicationLinkModel getExampleModel() {
        return ApplicationLinkModel.builder()
                .name(ApplicationLinkModel.EXAMPLE_1.getName())
                .displayUrl(ApplicationLinkModel.EXAMPLE_1.getDisplayUrl())
                .rpcUrl(ApplicationLinkModel.EXAMPLE_1.getRpcUrl())
                .outgoingAuthType(ApplicationLinkModel.EXAMPLE_1.getOutgoingAuthType())
                .incomingAuthType(ApplicationLinkModel.EXAMPLE_1.getIncomingAuthType())
                .primary(ApplicationLinkModel.EXAMPLE_1.getPrimary())
                .type(ApplicationLinkModel.EXAMPLE_1.getType())
                .ignoreSetupErrors(true)
                .build();
    }

}
