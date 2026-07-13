package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Response;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractMailServerPopResourceFuncTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    protected void testGetMailServerPopNotConfigured() throws Exception {
        // delete any existing configuration first so this test does not depend
        // on the state left behind by other test classes
        final HttpResponse<String> deleteResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .request(HttpMethod.DELETE, null);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), deleteResponse.statusCode());

        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .request();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), mailServerPopResponse.statusCode());
    }

    @Test
    @Order(2)
    void testSetMailServerPop() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .request(HttpMethod.PUT, getExampleModel());
        assertEquals(Response.Status.OK.getStatusCode(), mailServerPopResponse.statusCode(), mailServerPopResponse.body());

        final MailServerPopModel mailServerPopModel = objectMapper.readValue(mailServerPopResponse.body(), MailServerPopModel.class);
        assertMailServerModelAgainstExample(mailServerPopModel);
    }

    @Test
    @Order(3)
    void testGetMailServerPop() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .request();
        assertEquals(Response.Status.OK.getStatusCode(), mailServerPopResponse.statusCode(), mailServerPopResponse.body());

        final MailServerPopModel mailServerPopModel = objectMapper.readValue(mailServerPopResponse.body(), MailServerPopModel.class);
        assertNotNull(mailServerPopModel);
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
    public void testGetMailServerPopUnauthorized() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailServerPopResponse.statusCode());
    }

    @Test
    public void testSetMailServerPopUnauthorized() throws Exception {
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
