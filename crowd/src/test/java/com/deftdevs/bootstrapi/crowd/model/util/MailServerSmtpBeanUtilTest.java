package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MockMailConfiguration;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.internet.AddressException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MailServerSmtpBeanUtilTest {

    @Test
    public void testToMailServerSmtpBean() throws AddressException {
        final MailConfiguration mailConfiguration = new MockMailConfiguration();
        final MailServerSmtpBean mailServerSmtpBean = MailServerSmtpBeanUtil.toMailServerSmtpBean(mailConfiguration);

        assertNotNull(mailServerSmtpBean);
        assertEquals(mailConfiguration.getNotificationEmails().get(0), mailServerSmtpBean.getAdminContact());
        assertEquals(mailConfiguration.getSmtpServer().getFrom().toString(), mailServerSmtpBean.getFrom());
        assertEquals(mailConfiguration.getSmtpServer().getPrefix(), mailServerSmtpBean.getPrefix());
        assertEquals(mailConfiguration.getSmtpServer().getHost(), mailServerSmtpBean.getHost());
    }

    @Test
    public void testToMailServerSmtpBeanWithNull() {
        assertNull(MailServerSmtpBeanUtil.toMailServerSmtpBean(null));
    }

    @Test
    @Disabled("Running in IntelliJ, failing in Maven!?")
    public void testToMailConfiguration() throws AddressException {
        final MailServerSmtpBean mailServerSmtpBean = MailServerSmtpBean.EXAMPLE_1;

        final MailConfiguration mailConfiguration = MailServerSmtpBeanUtil.toMailConfiguration(mailServerSmtpBean);

        assertNotNull(mailConfiguration);
        assertEquals(mailServerSmtpBean.getAdminContact(), mailConfiguration.getNotificationEmails().get(0));
        assertEquals(mailServerSmtpBean.getFrom(), mailConfiguration.getSmtpServer().getFrom().toString());
        assertEquals(mailServerSmtpBean.getPrefix(), mailConfiguration.getSmtpServer().getPrefix());
        assertEquals(mailServerSmtpBean.getHost(), mailConfiguration.getSmtpServer().getHost());
    }

    @Test
    public void testToMailConfigurationWithNull() throws AddressException {
        assertNull(MailServerSmtpBeanUtil.toMailConfiguration(null));
    }

}
