package de.aservo.atlassian.crowd.confapi.rest;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MailManager;
import com.atlassian.crowd.util.mail.SMTPServer;
import de.aservo.atlassian.confapi.model.MailServerSmtpBean;
import de.aservo.atlassian.crowd.confapi.helper.CrowdWebAuthenticationHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class MailServerSmtpResourceTest {

    private MailConfiguration getDefaultMailConfiguration() throws AddressException {
        return MailConfiguration.builder()
                .setServerAlertAddress("alert@aservo.com")
                .setSmtpServer(SMTPServer.builder()
                        .setPrefix("[ASERVO]")
                        .setFrom(new InternetAddress("mail@aservo.com"))
                        .setHost("host.aservo.com")
                        .build())
                .build();
    }

    @Mock
    private MailManager mailManager;

    @Mock
    private CrowdWebAuthenticationHelper crowdWebAuthenticationHelper;

    @Test
    public void testGetSmtpMailServerWithSuccess() throws AddressException {
        MailConfiguration mailConfiguration = this.getDefaultMailConfiguration();
        doReturn(this.getDefaultMailConfiguration()).when(mailManager).getMailConfiguration();

        final MailConfigurationResource resource = new MailConfigurationResource(mailManager, crowdWebAuthenticationHelper);
        final Response response = resource.getMailServerSmtp();
        final MailServerSmtpBean bean = (MailServerSmtpBean) response.getEntity();

        assertEquals(OK.getStatusCode(), response.getStatus());
        assertEquals(mailConfiguration.getServerAlertAddress(), bean.getAdminContact());
        assertEquals(mailConfiguration.getSmtpServer().getFrom().toString(), bean.getFrom());
        assertEquals(mailConfiguration.getSmtpServer().getPrefix(), bean.getPrefix());
        assertEquals(mailConfiguration.getSmtpServer().getHost(), bean.getHost());
    }

    @Test
    public void testGetSmtpMailServerWithFailure() throws AddressException {
        doReturn(null).when(mailManager).getMailConfiguration();

        final MailConfigurationResource resource = new MailConfigurationResource(mailManager, crowdWebAuthenticationHelper);
        final Response response = resource.getMailServerSmtp();

        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testPutSmtpMailServerWithSuccess() throws AddressException {
        MailConfiguration mailConfiguration = this.getDefaultMailConfiguration();
        doReturn(this.getDefaultMailConfiguration()).when(mailManager).getMailConfiguration();

        final MailConfigurationResource resource = new MailConfigurationResource(mailManager, crowdWebAuthenticationHelper);
        final MailServerSmtpBean requestBean = new MailServerSmtpBean(
                mailConfiguration.getServerAlertAddress(),
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
        doReturn(null).when(mailManager).getMailConfiguration();

        final MailConfigurationResource resource = new MailConfigurationResource(mailManager, crowdWebAuthenticationHelper);
        final MailServerSmtpBean requestBean = new MailServerSmtpBean(
                mailConfiguration.getServerAlertAddress(),
                mailConfiguration.getSmtpServer().getFrom().toString(),
                mailConfiguration.getSmtpServer().getPrefix(),
                mailConfiguration.getSmtpServer().getHost()
        );
        final Response response = resource.putMailServerSmtp(requestBean);

        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
    }

}
