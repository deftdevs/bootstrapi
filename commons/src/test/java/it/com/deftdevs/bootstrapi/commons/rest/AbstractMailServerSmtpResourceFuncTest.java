package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractMailServerSmtpResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetMailServerImap() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), mailServerSmtpResponse.statusCode());

        final MailServerSmtpModel mailServerSmtpModel = objectMapper.readValue(mailServerSmtpResponse.body(), MailServerSmtpModel.class);
        assertNotNull(mailServerSmtpModel);
    }

    @Test
    void testSetMailServerImap() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .request(HttpMethod.PUT, getExampleModel());
        assertEquals(Response.Status.OK.getStatusCode(), mailServerSmtpResponse.statusCode());

        final MailServerSmtpModel mailServerSmtpModel = objectMapper.readValue(mailServerSmtpResponse.body(), MailServerSmtpModel.class);
        assertMailServerModelAgainstExample(mailServerSmtpModel);
    }

    @Test
    public void testGetMailServerSmtpUnauthenticated() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), mailServerSmtpResponse.statusCode());
    }

    @Test
    public void testSetMailServerSmtpUnauthenticated() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, getExampleModel());

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), mailServerSmtpResponse.statusCode());
    }

    @Test
    public void testGetMailServerSmtpUnauthorized() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailServerSmtpResponse.statusCode());
    }

    @Test
    public void testSetMailServerSmtpUnauthorized() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, getExampleModel());

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailServerSmtpResponse.statusCode());
    }

    protected void assertMailServerModelAgainstExample(MailServerSmtpModel bean) {
        MailServerSmtpModel exampleModel = getExampleModel();
        //although field 'password' in 'AbstractMailServerProtocolModel' is annotated with '@EqualsExclude' equals still yields false if
        //not the same. Thus, we need to reset the example password manually
        exampleModel.setPassword(null);
        assertEquals(exampleModel, bean);
    }

    protected MailServerSmtpModel getExampleModel() {
        return MailServerSmtpModel.EXAMPLE_2;
    }
}
