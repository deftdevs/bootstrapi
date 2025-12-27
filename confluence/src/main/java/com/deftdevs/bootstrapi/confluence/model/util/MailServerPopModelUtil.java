package com.deftdevs.bootstrapi.confluence.model.util;

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

        return MailServerPopModel.builder()
            .name(popMailServer.getName())
            .description(popMailServer.getDescription())
            .protocol(popMailServer.getMailProtocol().getProtocol())
            .host(popMailServer.getHostname())
            .port(popMailServer.getPort() == null ? null : Integer.valueOf(popMailServer.getPort()))
            .timeout(popMailServer.getTimeout())
            .username(popMailServer.getUsername())
            .build();
    }

    private MailServerPopModelUtil() {
    }

}
