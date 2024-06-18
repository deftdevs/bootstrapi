package it.de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.MailServerPopBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractMailServerPopResourceFuncTest {

    @Test
    void testGetMailServerPop() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP).build();

        ClientResponse clientResponse = mailserverResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        MailServerPopBean mailServerPopBean = clientResponse.getEntity(MailServerPopBean.class);
        assertNotNull(mailServerPopBean);
    }

    @Test
    void testSetMailServerPop() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP).build();

        ClientResponse clientResponse = mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        MailServerPopBean mailServerPopBean = clientResponse.getEntity(MailServerPopBean.class);
        assertMailServerBeanAgainstExample(mailServerPopBean);
    }

    @Test
    public void testGetMailServerPopUnauthenticated() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP)
                .username("wrong")
                .password("password")
                .build();


        assertThrows(ClientAuthenticationException.class, mailserverResource::get);
    }

    @Test
    public void testSetMailServerPopUnauthenticated() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP)
                .username("wrong")
                .password("password")
                .build();

        assertThrows(ClientAuthenticationException.class, () -> {
            mailserverResource.put(getExampleBean());
        });
    }

    @Test
    void testGetMailServerPopUnauthorized() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP)
                .username("user")
                .password("user")
                .build();

        ClientResponse response = mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatusCode());
    }

    @Test
    void testSetMailServerPopUnauthorized() {
        Resource mailserverResource = ResourceBuilder.builder(ConfAPI.MAIL_SERVER + "/" + ConfAPI.MAIL_SERVER_POP)
                .username("user")
                .password("user")
                .build();

        ClientResponse response = mailserverResource.put(getExampleBean());
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatusCode());
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
