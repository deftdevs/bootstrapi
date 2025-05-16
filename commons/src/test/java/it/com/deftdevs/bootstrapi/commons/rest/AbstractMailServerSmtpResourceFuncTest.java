package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;
import org.codehaus.jackson.map.ObjectMapper;
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

        final MailServerSmtpBean mailServerSmtpBean = objectMapper.readValue(mailServerSmtpResponse.body(), MailServerSmtpBean.class);
        assertNotNull(mailServerSmtpBean);
    }

    @Test
    void testSetMailServerImap() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .request(HttpMethod.PUT, getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), mailServerSmtpResponse.statusCode());

        final MailServerSmtpBean mailServerSmtpBean = objectMapper.readValue(mailServerSmtpResponse.body(), MailServerSmtpBean.class);
        assertMailServerBeanAgainstExample(mailServerSmtpBean);
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
                .request(HttpMethod.PUT, getExampleBean());

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), mailServerSmtpResponse.statusCode());
    }

    @Test
    public void testGetMailServerSmtpUnauthorized() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .username("user")
                .password("user")
                .request();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), mailServerSmtpResponse.statusCode());
    }

    @Test
    public void testSetMailServerSmtpUnauthorized() throws Exception {
        final HttpResponse<String> mailServerSmtpResponse = HttpRequestHelper.builder(BootstrAPI.MAIL_SERVER + "/" + BootstrAPI.MAIL_SERVER_SMTP)
                .username("user")
                .password("user")
                .request(HttpMethod.PUT, getExampleBean());

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), mailServerSmtpResponse.statusCode());
    }

    protected void assertMailServerBeanAgainstExample(MailServerSmtpBean bean) {
        MailServerSmtpBean exampleBean = getExampleBean();
        //although field 'password' in 'AbstractMailServerProtocolBean' is annotated with '@EqualsExclude' equals still yields false if
        //not the same. Thus, we need to reset the example password manually
        exampleBean.setPassword(null);
        assertEquals(exampleBean, bean);
    }

    protected MailServerSmtpBean getExampleBean() {
        return MailServerSmtpBean.EXAMPLE_2;
    }
}
