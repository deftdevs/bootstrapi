package de.aservo.confapi.crowd.bean;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.util.mail.SMTPServer;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)

public class MailServerSmtpBeanTest {

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

    @Test
    public void testDefaultConstructor() {
        final MailServerSmtpBean bean = new MailServerSmtpBean();

        assertNull(bean.getAdminContact());
        assertNull(bean.getFrom());
        assertNull(bean.getPrefix());
        assertNull(bean.getHost());
    }

    @Test
    public void testConstructor() throws AddressException {
        final MailServerSmtpBean bean = new MailServerSmtpBean(
                getDefaultMailConfiguration().getNotificationEmails().iterator().next(),
                getDefaultMailConfiguration().getSmtpServer().getFrom().toString(),
                getDefaultMailConfiguration().getSmtpServer().getPrefix(),
                getDefaultMailConfiguration().getSmtpServer().getHost()
        );
        assertEquals(bean.getAdminContact(), getDefaultMailConfiguration().getNotificationEmails().iterator().next());
        assertEquals(bean.getFrom(), getDefaultMailConfiguration().getSmtpServer().getFrom().toString());
        assertEquals(bean.getPrefix(), getDefaultMailConfiguration().getSmtpServer().getPrefix());
        assertEquals(bean.getHost(), getDefaultMailConfiguration().getSmtpServer().getHost());
    }

    @Test
    public void testEqualsAndHashCode() throws Exception {
        final MailServerSmtpBean bean1 = new MailServerSmtpBean(
                getDefaultMailConfiguration().getNotificationEmails().iterator().next(),
                getDefaultMailConfiguration().getSmtpServer().getFrom().toString(),
                getDefaultMailConfiguration().getSmtpServer().getPrefix(),
                getDefaultMailConfiguration().getSmtpServer().getHost()
        );

        final MailServerSmtpBean bean2 = new MailServerSmtpBean(
                getDefaultMailConfiguration().getNotificationEmails().iterator().next(),
                getDefaultMailConfiguration().getSmtpServer().getFrom().toString(),
                getDefaultMailConfiguration().getSmtpServer().getPrefix(),
                getDefaultMailConfiguration().getSmtpServer().getHost()
        );

        assertEquals(bean1, bean2);
        assertEquals(bean1.hashCode(), bean2.hashCode());
    }

    @Test
    public void testEqualsSameInstance() {
        final MailServerSmtpBean bean = new MailServerSmtpBean();
        assertEquals(bean, bean);
    }

    @Test
    public void testEqualsNull() {
        final MailServerSmtpBean bean = new MailServerSmtpBean();
        assertNotEquals(null, bean);
    }

    @Test
    public void testEqualsOtherType() {
        final MailServerSmtpBean bean = new MailServerSmtpBean();
        assertNotEquals(bean, new Object());
    }

}
