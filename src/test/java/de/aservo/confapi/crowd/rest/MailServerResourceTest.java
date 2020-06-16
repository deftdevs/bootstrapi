package de.aservo.confapi.crowd.rest;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MailConfigurationService;
import com.atlassian.crowd.util.mail.SMTPServer;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MailServerResourceTest {

    private MailConfiguration getDefaultMailConfiguration() throws AddressException {
        return MailConfiguration.builder()
                .setNotificationEmails(Collections.singletonList("alert@aservo.com"))
                .setSmtpServer(SMTPServer.builder()
                        .setPrefix("[ASERVO]")
                        .setFrom(new InternetAddress("mail@aservo.com"))
                        .setHost("host.aservo.com")
                        .build())
                .build();
    }

    @Mock
    private MailConfigurationService mailConfigurationService;

    private MailServerResource resource;

    @Before
    public void setup() {
        resource = new MailServerResource(mailConfigurationService);
    }

    @Test
    public void testGetSmtpMailServerWithSuccess() throws AddressException {
        MailConfiguration mailConfiguration = this.getDefaultMailConfiguration();
        doReturn(this.getDefaultMailConfiguration()).when(mailConfigurationService).getMailConfiguration();
        final Response response = resource.getMailServerSmtp();
        final MailServerSmtpBean bean = (MailServerSmtpBean) response.getEntity();

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(mailConfiguration.getNotificationEmails().iterator().next(), bean.getAdminContact());
        assertEquals(mailConfiguration.getSmtpServer().getFrom().toString(), bean.getFrom());
        assertEquals(mailConfiguration.getSmtpServer().getPrefix(), bean.getPrefix());
        assertEquals(mailConfiguration.getSmtpServer().getHost(), bean.getHost());
    }

    @Test
    public void testGetSmtpMailServerWithFailure() throws AddressException {
        doReturn(null).when(mailConfigurationService).getMailConfiguration();

        final Response response = resource.getMailServerSmtp();

        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPutSmtpMailServerWithSuccess() throws AddressException {
        MailConfiguration mailConfiguration = this.getDefaultMailConfiguration();
        doReturn(this.getDefaultMailConfiguration()).when(mailConfigurationService).getMailConfiguration();

        final MailServerSmtpBean requestBean = new MailServerSmtpBean(
                mailConfiguration.getNotificationEmails().iterator().next(),
                mailConfiguration.getSmtpServer().getFrom().toString(),
                mailConfiguration.getSmtpServer().getPrefix(),
                mailConfiguration.getSmtpServer().getHost()
        );
        final Response response = resource.putMailServerSmtp(requestBean);
        final MailServerSmtpBean responseBean = (MailServerSmtpBean) response.getEntity();

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(requestBean, responseBean);
    }

    @Test
    public void testSetHostWithFailure() throws AddressException {
        MailConfiguration mailConfiguration = this.getDefaultMailConfiguration();
        doReturn(null).when(mailConfigurationService).getMailConfiguration();

        final MailServerSmtpBean requestBean = new MailServerSmtpBean(
                mailConfiguration.getNotificationEmails().iterator().next(),
                mailConfiguration.getSmtpServer().getFrom().toString(),
                mailConfiguration.getSmtpServer().getPrefix(),
                mailConfiguration.getSmtpServer().getHost()
        );
        final Response response = resource.putMailServerSmtp(requestBean);

        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}
