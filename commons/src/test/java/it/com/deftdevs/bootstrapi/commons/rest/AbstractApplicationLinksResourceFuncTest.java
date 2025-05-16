package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
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
        final ApplicationLinkBean applicationLinkBean = getExampleBean();

        final HttpResponse<String> applicationLinksResponse = HttpRequestHelper.builder(BootstrAPI.APPLICATION_LINKS + "?" + "ignoreSetupErrors")
                .request(HttpMethod.PUT, Collections.singletonList(applicationLinkBean));
        assertEquals(Response.Status.OK.getStatusCode(), applicationLinksResponse.statusCode());

        final List<ApplicationLinkBean> applicationLinkBeans = objectMapper.readValue(applicationLinksResponse.body(), new TypeReference<List<ApplicationLinkBean>>(){});
        assertEquals(1, applicationLinkBeans.size());

        final ApplicationLinkBean responseApplicationLinkBean = applicationLinkBeans.iterator().next();
        assertEquals(applicationLinkBean.getRpcUrl(), responseApplicationLinkBean.getRpcUrl());
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

    protected ApplicationLinkBean getExampleBean() {
        return ApplicationLinkBean.EXAMPLE_1;
    }

}
