package it.de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractMailServerSmtpResourceFuncTest {

    @Test
    void testGetMailServerImap() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP).build();

        ClientResponse clientResponse = mailserverResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        MailServerSmtpBean mailServerSmtpBean = clientResponse.getEntity(MailServerSmtpBean.class);
        assertNotNull(mailServerSmtpBean);
    }

    @Test
    void testSetMailServerImap() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP).build();

        ClientResponse clientResponse = mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        MailServerSmtpBean mailServerSmtpBean = clientResponse.getEntity(MailServerSmtpBean.class);
        assertMailServerBeanAgainstExample(mailServerSmtpBean);
    }

    @Test
    public void testGetMailServerImapUnauthenticated() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP)
                .username("wrong")
                .password("password")
                .build();

        assertThrows(ClientAuthenticationException.class, mailserverResource::get);
    }

    @Test
    public void testSetMailServerImapUnauthenticated() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP)
                .username("wrong")
                .password("password")
                .build();

        assertThrows(ClientAuthenticationException.class, () -> {
            mailserverResource.put(getExampleBean());
        });
    }

    @Test
    void testGetMailServerImapUnauthorized() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP)
                .username("user")
                .password("user")
                .build();

        ClientResponse response = mailserverResource.get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatusCode());
    }

    @Test
    void testSetMailServerImapUnauthorized() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP)
                .username("user")
                .password("user")
                .build();

        ClientResponse response = mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatusCode());
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
