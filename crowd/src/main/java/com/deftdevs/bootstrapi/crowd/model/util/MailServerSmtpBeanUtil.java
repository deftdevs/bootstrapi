package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.util.mail.SMTPServer;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;

import javax.annotation.Nullable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Collections;

public class MailServerSmtpBeanUtil {

    @Nullable
    public static MailServerSmtpBean toMailServerSmtpBean(
            @Nullable final MailConfiguration mailConfiguration) {

        if (mailConfiguration == null || mailConfiguration.getSmtpServer() == null) {
            return null;
        }

        final MailServerSmtpBean mailServerSmtpBean = new MailServerSmtpBean();

        if (mailConfiguration.getNotificationEmails() != null && !mailConfiguration.getNotificationEmails().isEmpty()) {
            mailServerSmtpBean.setAdminContact(mailConfiguration.getNotificationEmails().get(0));
        }

        final SMTPServer mailConfigurationSmtp = mailConfiguration.getSmtpServer();

        if (mailConfigurationSmtp.getFrom() != null) {
            mailServerSmtpBean.setFrom(mailConfigurationSmtp.getFrom().toString());
        }

        mailServerSmtpBean.setPrefix(mailConfigurationSmtp.getPrefix());
        mailServerSmtpBean.setHost(mailConfigurationSmtp.getHost());
        mailServerSmtpBean.setPort(mailConfigurationSmtp.getPort());
        mailServerSmtpBean.setUsername(mailConfigurationSmtp.getUsername());
        // don't return password here
        mailServerSmtpBean.setTimeout((long) mailConfigurationSmtp.getTimeout());

        // TODO: After the build blocker in ConfAPI commons has been removed,
        //  implement proper distinguishing between useTls and startTls,
        //  see https://github.com/aservo/confapi-commons/issues/153

        return mailServerSmtpBean;
    }

    @Nullable
    public static MailConfiguration toMailConfiguration(
            @Nullable final MailServerSmtpBean mailServerSmtpBean) {

        return toMailConfiguration(mailServerSmtpBean, null);
    }

    @Nullable
    public static MailConfiguration toMailConfiguration(
            @Nullable final MailServerSmtpBean mailServerSmtpBean,
            @Nullable final MailConfiguration mailConfiguration) {

        if (mailServerSmtpBean == null) {
            return null;
        }

        final MailConfiguration.Builder mailConfigurationBuilder;
        final SMTPServer.Builder smtpServerBuilder;

        if (mailConfiguration != null) {
            mailConfigurationBuilder = MailConfiguration.builder(mailConfiguration);

            if (mailConfiguration.getSmtpServer() != null) {
                smtpServerBuilder = SMTPServer.builder(mailConfiguration.getSmtpServer());
            } else {
                smtpServerBuilder = SMTPServer.builder();
            }
        } else {
            mailConfigurationBuilder = MailConfiguration.builder();
            smtpServerBuilder = SMTPServer.builder();
        }

        if (mailServerSmtpBean.getAdminContact() != null) {
            mailConfigurationBuilder.setNotificationEmails(Collections.singletonList(mailServerSmtpBean.getAdminContact()));
        }

        if (mailServerSmtpBean.getFrom() != null) {
            try {
                smtpServerBuilder.setFrom(new InternetAddress(mailServerSmtpBean.getFrom()));
            } catch (AddressException e) {
                throw new BadRequestException(e.getMessage());
            }
        }

        if (mailServerSmtpBean.getPrefix() != null) {
            smtpServerBuilder.setPrefix(mailServerSmtpBean.getPrefix());
        }

        if (mailServerSmtpBean.getHost() != null) {
            smtpServerBuilder.setHost(mailServerSmtpBean.getHost());
        }

        if (mailServerSmtpBean.getPort() != null) {
            smtpServerBuilder.setPort(mailServerSmtpBean.getPort());
        }

        if (mailServerSmtpBean.getUsername() != null) {
            smtpServerBuilder.setUsername(mailServerSmtpBean.getUsername());
        }

        if (mailServerSmtpBean.getPassword() != null) {
            smtpServerBuilder.setPassword(mailServerSmtpBean.getPassword());
        }

        if (mailServerSmtpBean.getTimeout() != null) {
            smtpServerBuilder.setTimeout(mailServerSmtpBean.getTimeout().intValue());
        }

        // TODO: After the build blocker in ConfAPI commons has been removed,
        //  implement proper distinguishing between useTls and startTls,
        //  see https://github.com/aservo/confapi-commons/issues/153
        smtpServerBuilder.setStartTLS(mailServerSmtpBean.getUseTls());

        mailConfigurationBuilder.setSmtpServer(smtpServerBuilder.build());

        return mailConfigurationBuilder.build();
    }

    private MailServerSmtpBeanUtil() {
    }

}
