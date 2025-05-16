package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        final HttpResponse<String> applicationLinksResponse = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS + "?" + "ignoreSetupErrors")
                .request(HttpMethod.PUT, Collections.singletonList(applicationLinkModel));
        assertEquals(Response.Status.OK.getStatusCode(), applicationLinksResponse.statusCode());

        final List<ApplicationLinkModel> applicationLinkModels = objectMapper.readValue(applicationLinksResponse.body(), new TypeReference<List<ApplicationLinkModel>>(){});
        assertEquals(1, applicationLinkModels.size());

        final ApplicationLinkModel responseApplicationLinkModel = applicationLinkModels.iterator().next();
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
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), applicationLinksResource.statusCode());
    }

    protected ApplicationLinkModel getExampleModel() {
        return ApplicationLinkModel.EXAMPLE_1;
    }

}
