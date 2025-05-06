package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractMailServerPopResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetMailServerPop() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), mailServerPopResponse.statusCode());

        final MailServerPopModel mailServerPopModel = objectMapper.readValue(mailServerPopResponse.body(), MailServerPopModel.class);
        assertNotNull(mailServerPopModel);
    }

    @Test
    void testSetMailServerPop() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .request(HttpMethod.PUT, getExampleModel());
        assertEquals(Response.Status.OK.getStatusCode(), mailServerPopResponse.statusCode());

        final MailServerPopModel mailServerPopModel = objectMapper.readValue(mailServerPopResponse.body(), MailServerPopModel.class);
        assertMailServerModelAgainstExample(mailServerPopModel);
    }

    @Test
    public void testGetMailServerPopUnauthenticated() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .username("wrong")
                .password("password")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), mailServerPopResponse.statusCode());
    }

    @Test
    public void testSetMailServerPopUnauthenticated() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .username("wrong")
                .password("password")
                .request(HttpMethod.PUT, getExampleModel());

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), mailServerPopResponse.statusCode());
    }

    @Test
    void testGetMailServerPopUnauthorized() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailServerPopResponse.statusCode());
    }

    @Test
    void testSetMailServerPopUnauthorized() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, getExampleModel());

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailServerPopResponse.statusCode());
    }

    protected void assertMailServerModelAgainstExample(MailServerPopModel bean) {
        MailServerPopModel exampleModel = getExampleModel();
        //although field 'password' in 'AbstractMailServerProtocolModel' is annotated with '@EqualsExclude' equals still yields false if
        //not the same. Thus, we need to reset the example password manually
        exampleModel.setPassword(null);
        assertEquals(exampleModel, bean);
    }

    protected MailServerPopModel getExampleModel() {
        return MailServerPopModel.EXAMPLE_2;
    }
}
