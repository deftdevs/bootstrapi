package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MailConfigurationService;
import com.atlassian.crowd.manager.mail.MockMailConfiguration;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.crowd.model.util.MailServerSmtpModelUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.internet.AddressException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MailServerServiceTest {

    @Mock
    private MailConfigurationService mailConfigurationService;

    private MailServerServiceImpl mailServerService;

    @BeforeEach
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

        final MailServerSmtpModel mailServerSmtpModel = new MailServerSmtpModel();
        mailServerService.setMailServerSmtp(mailServerSmtpModel);

        final ArgumentCaptor<MailConfiguration> mailConfigurationCaptor = ArgumentCaptor.forClass(MailConfiguration.class);
        verify(mailConfigurationService).saveConfiguration(mailConfigurationCaptor.capture());
        final MailServerSmtpModel updatedMailServerSmtpModel = MailServerSmtpModelUtil.toMailServerSmtpModel(mailConfigurationCaptor.getValue());

        assertEquals(mailServerSmtpModel.getAdminContact(), updatedMailServerSmtpModel.getAdminContact());
        assertEquals(mailServerSmtpModel.getFrom(), updatedMailServerSmtpModel.getFrom());
        assertEquals(mailServerSmtpModel.getPrefix(), updatedMailServerSmtpModel.getPrefix());
        assertEquals(mailServerSmtpModel.getHost(), updatedMailServerSmtpModel.getHost());
    }

    @Test
    public void testSetMailServerSmtp() {
        final MailConfiguration emptyMailConfiguration = MailConfiguration.builder().build();
        doReturn(emptyMailConfiguration).when(mailConfigurationService).getMailConfiguration();

        final MailServerSmtpModel mailServerSmtpModel = MailServerSmtpModel.EXAMPLE_1;

        mailServerService.setMailServerSmtp(mailServerSmtpModel);

        final ArgumentCaptor<MailConfiguration> mailConfigurationCaptor = ArgumentCaptor.forClass(MailConfiguration.class);
        verify(mailConfigurationService).saveConfiguration(mailConfigurationCaptor.capture());
        final MailServerSmtpModel updatedMailServerSmtpModel = MailServerSmtpModelUtil.toMailServerSmtpModel(mailConfigurationCaptor.getValue());

        assertEquals(mailServerSmtpModel.getFrom(), updatedMailServerSmtpModel.getFrom());
        assertEquals(mailServerSmtpModel.getPrefix(), updatedMailServerSmtpModel.getPrefix());
        assertEquals(mailServerSmtpModel.getHost(), updatedMailServerSmtpModel.getHost());
    }

    @Test
    public void testSetMailServerSmtpWithException() {
        final MailConfiguration emptyMailConfiguration = MailConfiguration.builder().build();
        doReturn(emptyMailConfiguration).when(mailConfigurationService).getMailConfiguration();

        final MailServerSmtpModel mailServerSmtpModel = MailServerSmtpModel.EXAMPLE_1;
        mailServerSmtpModel.setFrom("@wrong@format@");

        assertThrows(BadRequestException.class, () -> {
            mailServerService.setMailServerSmtp(mailServerSmtpModel);
        });
    }

    @Test
    public void testGetMailServerPop() {
        assertThrows(UnsupportedOperationException.class, () -> {
            mailServerService.getMailServerPop();
        });
    }

    @Test
    public void testSetMailServerPop() {
        assertThrows(UnsupportedOperationException.class, () -> {
            mailServerService.setMailServerPop(null);
        });
    }
}
