package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.util.mail.SMTPServer;
import de.aservo.confapi.commons.model.MailServerSmtpBean;

import javax.annotation.Nullable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Collections;

public class MailServerSmtpBeanUtil {

    @Nullable
    public static MailServerSmtpBean toMailServerSmtpBean(
            @Nullable final MailConfiguration mailConfiguration) {

        if (mailConfiguration == null) {
            return null;
        }

        return new MailServerSmtpBean(
                mailConfiguration.getNotificationEmails() == null || mailConfiguration.getNotificationEmails().isEmpty() ?
                        null : mailConfiguration.getNotificationEmails().get(0),
                mailConfiguration.getSmtpServer() == null || mailConfiguration.getSmtpServer().getFrom() == null ?
                        null : mailConfiguration.getSmtpServer().getFrom().toString(),
                mailConfiguration.getSmtpServer() == null ?
                        null : mailConfiguration.getSmtpServer().getPrefix(),
                mailConfiguration.getSmtpServer() == null ?
                        null : mailConfiguration.getSmtpServer().getHost()
        );
    }

    @Nullable
    public static MailConfiguration toMailConfiguration(
            @Nullable final MailServerSmtpBean mailServerSmtpBean) throws AddressException {

        if (mailServerSmtpBean == null) {
            return null;
        }

        return MailConfiguration.builder()
                .setNotificationEmails(Collections.singletonList(mailServerSmtpBean.getAdminContact()))
                .setSmtpServer(SMTPServer.builder()
                        .setFrom(new InternetAddress(mailServerSmtpBean.getFrom()))
                        .setPrefix(mailServerSmtpBean.getPrefix())
                        .setHost(mailServerSmtpBean.getHost())
                        .build())
                .build();
    }

    private MailServerSmtpBeanUtil() {
    }

}
