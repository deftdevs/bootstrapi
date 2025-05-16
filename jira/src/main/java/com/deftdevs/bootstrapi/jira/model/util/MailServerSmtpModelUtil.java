package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.mail.server.SMTPMailServer;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;

import javax.annotation.Nullable;

public class MailServerSmtpModelUtil {

    @Nullable
    public static MailServerSmtpModel toMailServerSmtpModel(
            @Nullable final SMTPMailServer smtpMailServer) {

        if (smtpMailServer == null) {
            return null;
        }

        final MailServerSmtpModel mailServerSmtpModel = new MailServerSmtpModel();
        mailServerSmtpModel.setName(smtpMailServer.getName());
        mailServerSmtpModel.setDescription(smtpMailServer.getDescription());
        mailServerSmtpModel.setFrom(smtpMailServer.getDefaultFrom());
        mailServerSmtpModel.setPrefix(smtpMailServer.getPrefix());
        mailServerSmtpModel.setProtocol(smtpMailServer.getMailProtocol().getProtocol());
        mailServerSmtpModel.setHost(smtpMailServer.getHostname());
        mailServerSmtpModel.setPort(smtpMailServer.getPort());
        mailServerSmtpModel.setUseTls(smtpMailServer.isTlsRequired());
        mailServerSmtpModel.setTimeout(smtpMailServer.getTimeout());
        mailServerSmtpModel.setUsername(smtpMailServer.getUsername());
        return mailServerSmtpModel;
    }

    private MailServerSmtpModelUtil() {
    }

}
