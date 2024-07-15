package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerPopBean;
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

        final MailServerPopBean mailServerPopBean = objectMapper.readValue(mailServerPopResponse.body(), MailServerPopBean.class);
        assertNotNull(mailServerPopBean);
    }

    @Test
    void testSetMailServerPop() throws Exception {
        final HttpResponse<String> mailServerPopResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_POP)
                .request(HttpMethod.PUT, getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), mailServerPopResponse.statusCode());

        final MailServerPopBean mailServerPopBean = objectMapper.readValue(mailServerPopResponse.body(), MailServerPopBean.class);
        assertMailServerBeanAgainstExample(mailServerPopBean);
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
                .request(HttpMethod.PUT, getExampleBean());

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
                .request(HttpMethod.PUT, getExampleBean());

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailServerPopResponse.statusCode());
    }

    protected void assertMailServerBeanAgainstExample(MailServerPopBean bean) {
        MailServerPopBean exampleBean = getExampleBean();
        //although field 'password' in 'AbstractMailServerProtocolBean' is annotated with '@EqualsExclude' equals still yields false if
        //not the same. Thus, we need to reset the example password manually
        exampleBean.setPassword(null);
        assertEquals(exampleBean, bean);
    }

    protected MailServerPopBean getExampleBean() {
        return MailServerPopBean.EXAMPLE_2;
    }
}
