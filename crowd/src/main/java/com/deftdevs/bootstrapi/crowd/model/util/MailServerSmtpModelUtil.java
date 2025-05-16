package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.util.mail.SMTPServer;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;

import javax.annotation.Nullable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Collections;

public class MailServerSmtpModelUtil {

    @Nullable
    public static MailServerSmtpModel toMailServerSmtpModel(
            @Nullable final MailConfiguration mailConfiguration) {

        if (mailConfiguration == null || mailConfiguration.getSmtpServer() == null) {
            return null;
        }

        final MailServerSmtpModel mailServerSmtpModel = new MailServerSmtpModel();

        if (mailConfiguration.getNotificationEmails() != null && !mailConfiguration.getNotificationEmails().isEmpty()) {
            mailServerSmtpModel.setAdminContact(mailConfiguration.getNotificationEmails().get(0));
        }

        final SMTPServer mailConfigurationSmtp = mailConfiguration.getSmtpServer();

        if (mailConfigurationSmtp.getFrom() != null) {
            mailServerSmtpModel.setFrom(mailConfigurationSmtp.getFrom().toString());
        }

        mailServerSmtpModel.setPrefix(mailConfigurationSmtp.getPrefix());
        mailServerSmtpModel.setHost(mailConfigurationSmtp.getHost());
        mailServerSmtpModel.setPort(mailConfigurationSmtp.getPort());
        mailServerSmtpModel.setUsername(mailConfigurationSmtp.getUsername());
        // don't return password here
        mailServerSmtpModel.setTimeout((long) mailConfigurationSmtp.getTimeout());

        // TODO: After the build blocker in BootstrAPI commons has been removed,
        //  implement proper distinguishing between useTls and startTls,
        //  see https://github.com/deftdevs/bootstrapi-commons/issues/153

        return mailServerSmtpModel;
    }

    @Nullable
    public static MailConfiguration toMailConfiguration(
            @Nullable final MailServerSmtpModel mailServerSmtpModel) {

        return toMailConfiguration(mailServerSmtpModel, null);
    }

    @Nullable
    public static MailConfiguration toMailConfiguration(
            @Nullable final MailServerSmtpModel mailServerSmtpModel,
            @Nullable final MailConfiguration mailConfiguration) {

        if (mailServerSmtpModel == null) {
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

        if (mailServerSmtpModel.getAdminContact() != null) {
            mailConfigurationBuilder.setNotificationEmails(Collections.singletonList(mailServerSmtpModel.getAdminContact()));
        }

        if (mailServerSmtpModel.getFrom() != null) {
            try {
                smtpServerBuilder.setFrom(new InternetAddress(mailServerSmtpModel.getFrom()));
            } catch (AddressException e) {
                throw new BadRequestException(e.getMessage());
            }
        }

        if (mailServerSmtpModel.getPrefix() != null) {
            smtpServerBuilder.setPrefix(mailServerSmtpModel.getPrefix());
        }

        if (mailServerSmtpModel.getHost() != null) {
            smtpServerBuilder.setHost(mailServerSmtpModel.getHost());
        }

        if (mailServerSmtpModel.getPort() != null) {
            smtpServerBuilder.setPort(mailServerSmtpModel.getPort());
        }

        if (mailServerSmtpModel.getUsername() != null) {
            smtpServerBuilder.setUsername(mailServerSmtpModel.getUsername());
        }

        if (mailServerSmtpModel.getPassword() != null) {
            smtpServerBuilder.setPassword(mailServerSmtpModel.getPassword());
        }

        if (mailServerSmtpModel.getTimeout() != null) {
            smtpServerBuilder.setTimeout(mailServerSmtpModel.getTimeout().intValue());
        }

        // TODO: After the build blocker in BootstrAPI commons has been removed,
        //  implement proper distinguishing between useTls and startTls,
        //  see https://github.com/deftdevs/bootstrapi-commons/issues/153
        smtpServerBuilder.setStartTLS(mailServerSmtpModel.getUseTls());

        mailConfigurationBuilder.setSmtpServer(smtpServerBuilder.build());

        return mailConfigurationBuilder.build();
    }

    private MailServerSmtpModelUtil() {
    }

}
