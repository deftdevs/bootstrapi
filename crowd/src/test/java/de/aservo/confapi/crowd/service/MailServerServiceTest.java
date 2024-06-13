package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MailConfigurationService;
import com.atlassian.crowd.manager.mail.MockMailConfiguration;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import de.aservo.confapi.crowd.model.util.MailServerSmtpBeanUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.mail.internet.AddressException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MailServerServiceTest {

    @Mock
    private MailConfigurationService mailConfigurationService;

    private MailServerServiceImpl mailServerService;

    @Before
    public void setup() {
        mailServerService = new MailServerServiceImpl(mailConfigurationService);
    }

    @Test
    public void testGetMailServerSmtp() throws AddressException {
        doReturn(true).when(mailConfigurationService).isConfigured();
        doReturn(new MockMailConfiguration()).when(mailConfigurationService).getMailConfiguration();
        assertNotNull(mailServerService.getMailServerSmtp());
    }

    @Test
    public void testGetMailServerSmtpNotConfigured() throws AddressException {
        assertNull(mailServerService.getMailServerSmtp());
    }

    @Test
    public void testSetMailServerSmtpDefault() {
        final MailConfiguration emptyMailConfiguration = MailConfiguration.builder().build();
        doReturn(emptyMailConfiguration).when(mailConfigurationService).getMailConfiguration();

        final MailServerSmtpBean mailServerSmtpBean = new MailServerSmtpBean();
        mailServerService.setMailServerSmtp(mailServerSmtpBean);

        final ArgumentCaptor<MailConfiguration> mailConfigurationCaptor = ArgumentCaptor.forClass(MailConfiguration.class);
        verify(mailConfigurationService).saveConfiguration(mailConfigurationCaptor.capture());
        final MailServerSmtpBean updatedMailServerSmtpBean = MailServerSmtpBeanUtil.toMailServerSmtpBean(mailConfigurationCaptor.getValue());

        assertEquals(mailServerSmtpBean.getAdminContact(), updatedMailServerSmtpBean.getAdminContact());
        assertEquals(mailServerSmtpBean.getFrom(), updatedMailServerSmtpBean.getFrom());
        assertEquals(mailServerSmtpBean.getPrefix(), updatedMailServerSmtpBean.getPrefix());
        assertEquals(mailServerSmtpBean.getHost(), updatedMailServerSmtpBean.getHost());
    }

    @Test
    public void testSetMailServerSmtp() {
        final MailConfiguration emptyMailConfiguration = MailConfiguration.builder().build();
        doReturn(emptyMailConfiguration).when(mailConfigurationService).getMailConfiguration();

        final MailServerSmtpBean mailServerSmtpBean = MailServerSmtpBean.EXAMPLE_1;

        mailServerService.setMailServerSmtp(mailServerSmtpBean);

        final ArgumentCaptor<MailConfiguration> mailConfigurationCaptor = ArgumentCaptor.forClass(MailConfiguration.class);
        verify(mailConfigurationService).saveConfiguration(mailConfigurationCaptor.capture());
        final MailServerSmtpBean updatedMailServerSmtpBean = MailServerSmtpBeanUtil.toMailServerSmtpBean(mailConfigurationCaptor.getValue());

        assertEquals(mailServerSmtpBean.getFrom(), updatedMailServerSmtpBean.getFrom());
        assertEquals(mailServerSmtpBean.getPrefix(), updatedMailServerSmtpBean.getPrefix());
        assertEquals(mailServerSmtpBean.getHost(), updatedMailServerSmtpBean.getHost());
    }

    @Test(expected = BadRequestException.class)
    public void testSetMailServerSmtpWithException() {
        final MailConfiguration emptyMailConfiguration = MailConfiguration.builder().build();
        doReturn(emptyMailConfiguration).when(mailConfigurationService).getMailConfiguration();

        final MailServerSmtpBean mailServerSmtpBean = MailServerSmtpBean.EXAMPLE_1;
        mailServerSmtpBean.setFrom("@wrong@format@");
        mailServerService.setMailServerSmtp(mailServerSmtpBean);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMailServerPop() {
        mailServerService.getMailServerPop();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMailServerPop() {
        mailServerService.setMailServerPop(null);
    }
}
