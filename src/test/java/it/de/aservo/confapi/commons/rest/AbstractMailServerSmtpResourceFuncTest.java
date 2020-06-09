package it.de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractMailServerSmtpResourceFuncTest {

    @Test
    public void testGetMailserverImap() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP).build();

        ClientResponse clientResponse = mailserverResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        MailServerSmtpBean mailServerSmtpBean = clientResponse.getEntity(MailServerSmtpBean.class);
        assertNotNull(mailServerSmtpBean);
    }

    @Test
    public void testSetMailserverImap() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP).build();

        ClientResponse clientResponse = mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        MailServerSmtpBean mailServerSmtpBean = clientResponse.getEntity(MailServerSmtpBean.class);
        assertMailserverBeanAgainstExample(mailServerSmtpBean);
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testGetMailserverImapUnauthenticated() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP)
                .username("wrong")
                .password("password")
                .build();
        mailserverResource.get();
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testSetMailserverImapUnauthenticated() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP)
                .username("wrong")
                .password("password")
                .build();
        mailserverResource.put(getExampleBean());
    }

    @Test
    public void testGetMailserverImapUnauthorized() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP)
                .username("user")
                .password("user")
                .build();
        mailserverResource.get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailserverResource.put(getExampleBean()).getStatusCode());
    }

    @Test
    public void testSetMailserverImapUnauthorized() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_SMTP)
                .username("user")
                .password("user")
                .build();
        mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailserverResource.put(getExampleBean()).getStatusCode());
    }

    protected void assertMailserverBeanAgainstExample(MailServerSmtpBean bean) {
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
