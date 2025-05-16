package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MockMailConfiguration;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.internet.AddressException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MailServerSmtpModelUtilTest {

    @Test
    public void testToMailServerSmtpModel() throws AddressException {
        final MailConfiguration mailConfiguration = new MockMailConfiguration();
        final MailServerSmtpModel mailServerSmtpModel = MailServerSmtpModelUtil.toMailServerSmtpModel(mailConfiguration);

        assertNotNull(mailServerSmtpModel);
        assertEquals(mailConfiguration.getNotificationEmails().get(0), mailServerSmtpModel.getAdminContact());
        assertEquals(mailConfiguration.getSmtpServer().getFrom().toString(), mailServerSmtpModel.getFrom());
        assertEquals(mailConfiguration.getSmtpServer().getPrefix(), mailServerSmtpModel.getPrefix());
        assertEquals(mailConfiguration.getSmtpServer().getHost(), mailServerSmtpModel.getHost());
    }

    @Test
    public void testToMailServerSmtpModelWithNull() {
        assertNull(MailServerSmtpModelUtil.toMailServerSmtpModel(null));
    }

    @Test
    @Disabled("Running in IntelliJ, failing in Maven!?")
    public void testToMailConfiguration() throws AddressException {
        final MailServerSmtpModel mailServerSmtpModel = MailServerSmtpModel.EXAMPLE_1;

        final MailConfiguration mailConfiguration = MailServerSmtpModelUtil.toMailConfiguration(mailServerSmtpModel);

        assertNotNull(mailConfiguration);
        assertEquals(mailServerSmtpModel.getAdminContact(), mailConfiguration.getNotificationEmails().get(0));
        assertEquals(mailServerSmtpModel.getFrom(), mailConfiguration.getSmtpServer().getFrom().toString());
        assertEquals(mailServerSmtpModel.getPrefix(), mailConfiguration.getSmtpServer().getPrefix());
        assertEquals(mailServerSmtpModel.getHost(), mailConfiguration.getSmtpServer().getHost());
    }

    @Test
    public void testToMailConfigurationWithNull() throws AddressException {
        assertNull(MailServerSmtpModelUtil.toMailConfiguration(null));
    }

}
