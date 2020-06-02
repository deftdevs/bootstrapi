package it.de.aservo.atlassian.confapi.rest;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.MailServerPopBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractMailServerPopResourceFuncTest {

    @Test
    public void testGetMailserverPop() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP).build();

        ClientResponse clientResponse = mailserverResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        MailServerPopBean mailServerPopBean = clientResponse.getEntity(MailServerPopBean.class);
        assertNotNull(mailServerPopBean);
    }

    @Test
    public void testSetMailserverPop() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP).build();

        ClientResponse clientResponse = mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        MailServerPopBean mailServerPopBean = clientResponse.getEntity(MailServerPopBean.class);
        assertMailserverBeanAgainstExample(mailServerPopBean);
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testGetMailserverPopUnauthenticated() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP)
                .username("wrong")
                .password("password")
                .build();
        mailserverResource.get();
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testSetMailserverPopUnauthenticated() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP)
                .username("wrong")
                .password("password")
                .build();
        mailserverResource.put(getExampleBean());
    }

    @Test
    @Ignore("cannot be executed because there is no default user with restricted access rights")
    public void testGetMailserverPopUnauthorized() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP)
                .username("user")
                .password("user")
                .build();
        mailserverResource.get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailserverResource.put(getExampleBean()).getStatusCode());
    }

    @Test
    @Ignore("cannot be executed because there is no default user with restricted access rights")
    public void testSetMailserverPopUnauthorized() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP)
                .username("user")
                .password("user")
                .build();
        mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), mailserverResource.put(getExampleBean()).getStatusCode());
    }

    protected void assertMailserverBeanAgainstExample(MailServerPopBean bean) {
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
