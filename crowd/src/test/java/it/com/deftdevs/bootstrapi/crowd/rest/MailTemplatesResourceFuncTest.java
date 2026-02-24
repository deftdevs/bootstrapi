package it.com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.crowd.model.MailTemplatesModel;
import com.deftdevs.bootstrapi.crowd.rest.api.MailTemplateResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.com.deftdevs.bootstrapi.commons.rest.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MailTemplatesResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetMailTemplates() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(MailTemplateResource.MAIL_TEMPLATES)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());

        final MailTemplatesModel mailTemplatesModel = objectMapper.readValue(response.body(), MailTemplatesModel.class);
        assertNotNull(mailTemplatesModel);
    }

    @Test
    void testSetMailTemplates() throws Exception {
        final MailTemplatesModel exampleModel = MailTemplatesModel.EXAMPLE_1;

        final HttpResponse<String> response = HttpRequestHelper.builder(MailTemplateResource.MAIL_TEMPLATES)
                .request(HttpMethod.PUT, exampleModel);
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode(), response.body());

        final MailTemplatesModel mailTemplatesModel = objectMapper.readValue(response.body(), MailTemplatesModel.class);
        assertEquals(exampleModel, mailTemplatesModel);
    }

    @Test
    void testGetMailTemplatesUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(MailTemplateResource.MAIL_TEMPLATES)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetMailTemplatesUnauthenticated() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(MailTemplateResource.MAIL_TEMPLATES)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, MailTemplatesModel.EXAMPLE_1);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.statusCode());
    }

    @Test
    void testGetMailTemplatesUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(MailTemplateResource.MAIL_TEMPLATES)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }

    @Test
    void testSetMailTemplatesUnauthorized() throws Exception {
        final HttpResponse<String> response = HttpRequestHelper.builder(MailTemplateResource.MAIL_TEMPLATES)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, MailTemplatesModel.EXAMPLE_1);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.statusCode());
    }
}
