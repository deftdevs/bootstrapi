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

        return MailServerSmtpModel.builder()
                .name(smtpMailServer.getName())
                .description(smtpMailServer.getDescription())
                .from(smtpMailServer.getDefaultFrom())
                .prefix(smtpMailServer.getPrefix())
                .protocol(smtpMailServer.getMailProtocol().getProtocol())
                .host(smtpMailServer.getHostname())
                .port(smtpMailServer.getPort() == null ? null : Integer.valueOf(smtpMailServer.getPort()))
                .useTls(smtpMailServer.isTlsRequired())
                .timeout(smtpMailServer.getTimeout())
                .username(smtpMailServer.getUsername())
                .build();
    }

    private MailServerSmtpModelUtil() {
    }

}
