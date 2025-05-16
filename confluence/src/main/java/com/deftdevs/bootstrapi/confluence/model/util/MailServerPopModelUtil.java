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

        final MailServerPopModel mailServerPopModel = new MailServerPopModel();
        mailServerPopModel.setName(popMailServer.getName());
        mailServerPopModel.setDescription(popMailServer.getDescription());
        mailServerPopModel.setProtocol(popMailServer.getMailProtocol().getProtocol());
        mailServerPopModel.setHost(popMailServer.getHostname());
        mailServerPopModel.setPort(popMailServer.getPort());
        mailServerPopModel.setTimeout(popMailServer.getTimeout());
        mailServerPopModel.setUsername(popMailServer.getUsername());
        return mailServerPopModel;
    }

    private MailServerPopModelUtil() {
    }

}
