package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.mail.server.PopMailServer;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;

import javax.annotation.Nullable;

public class MailServerPopModelUtil {

    @Nullable
    public static MailServerPopModel toMailServerPopModel(
            @Nullable final PopMailServer popMailServer) {

        if (popMailServer == null) {
            return null;
        }

        final MailServerPopModel mailServerPopModel = MailServerPopModel.builder()
            .name(popMailServer.getName())
            .description(popMailServer.getDescription())
            .protocol(popMailServer.getMailProtocol() != null ? popMailServer.getMailProtocol().getProtocol() : null)
            .host(popMailServer.getHostname())
            .port(popMailServer.getPort() != null ? Integer.valueOf(popMailServer.getPort()) : null)
            .timeout(popMailServer.getTimeout())
            .username(popMailServer.getUsername())
            .build();
        return mailServerPopModel;
    }

    private MailServerPopModelUtil() {
    }

}
