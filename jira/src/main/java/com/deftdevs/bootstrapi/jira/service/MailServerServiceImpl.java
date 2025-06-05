package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.mail.MailException;
import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.SMTPMailServer;
import com.atlassian.mail.server.impl.PopMailServerImpl;
import com.atlassian.mail.server.impl.SMTPMailServerImpl;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.jira.model.util.MailServerPopModelUtil;
import com.deftdevs.bootstrapi.jira.model.util.MailServerSmtpModelUtil;
import com.deftdevs.bootstrapi.jira.util.MailProtocolUtil;

public class MailServerServiceImpl implements MailServerService {

    private final MailServerManager mailServerManager;

    public MailServerServiceImpl(
            final MailServerManager mailServerManager) {

        this.mailServerManager = mailServerManager;
    }

    @Override
    public MailServerSmtpModel getMailServerSmtp() {
        final SMTPMailServer smtpMailServer = mailServerManager.getDefaultSMTPMailServer();
        return MailServerSmtpModelUtil.toMailServerSmtpModel(smtpMailServer);
    }

    @Override
    public MailServerSmtpModel setMailServerSmtp(
            MailServerSmtpModel mailServerSmtpModel) {

        final SMTPMailServer smtpMailServer = mailServerManager.isDefaultSMTPMailServerDefined()
                ? mailServerManager.getDefaultSMTPMailServer()
                : new SMTPMailServerImpl.Builder<>().build();

        assert smtpMailServer != null;

        if (mailServerSmtpModel.getName() != null) {
            smtpMailServer.setName(mailServerSmtpModel.getName());
        }

        if (mailServerSmtpModel.getDescription() != null) {
            smtpMailServer.setDescription(mailServerSmtpModel.getDescription());
        }

        if (mailServerSmtpModel.getFrom() != null) {
            smtpMailServer.setDefaultFrom(mailServerSmtpModel.getFrom());
        }

        if (mailServerSmtpModel.getPrefix() != null) {
            smtpMailServer.setPrefix(mailServerSmtpModel.getPrefix());
        }

        smtpMailServer.setMailProtocol(MailProtocolUtil.find(mailServerSmtpModel.getProtocol(), MailProtocol.SMTP));

        if (mailServerSmtpModel.getHost() != null) {
            smtpMailServer.setHostname(mailServerSmtpModel.getHost());
        }

        if (mailServerSmtpModel.getPort() != null) {
            smtpMailServer.setPort(String.valueOf(mailServerSmtpModel.getPort()));
        } else {
            smtpMailServer.setPort(smtpMailServer.getMailProtocol().getDefaultPort());
        }

        smtpMailServer.setTlsRequired(mailServerSmtpModel.getUseTls());

        if (mailServerSmtpModel.getUsername() != null) {
            smtpMailServer.setUsername(mailServerSmtpModel.getUsername());
        }

        smtpMailServer.setTimeout(mailServerSmtpModel.getTimeout());

        try {
            if (mailServerManager.isDefaultSMTPMailServerDefined()) {
                mailServerManager.update(smtpMailServer);
            } else {
                smtpMailServer.setId(mailServerManager.create(smtpMailServer));
            }
        } catch (MailException e) {
            throw new BadRequestException(e.getMessage());
        }

        return mailServerSmtpModel;
    }

    @Override
    public MailServerPopModel getMailServerPop() {
        final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer();
        return MailServerPopModelUtil.toMailServerPopModel(popMailServer);
    }

    @Override
    public MailServerPopModel setMailServerPop(
            MailServerPopModel mailServerPopModel) {

        final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer() != null
                ? mailServerManager.getDefaultPopMailServer()
                : new PopMailServerImpl.Builder<>().build();

        assert popMailServer != null;

        if (mailServerPopModel.getName() != null) {
            popMailServer.setName(mailServerPopModel.getName());
        }

        if (mailServerPopModel.getDescription() != null) {
            popMailServer.setDescription(mailServerPopModel.getDescription());
        }

        popMailServer.setMailProtocol(MailProtocolUtil.find(mailServerPopModel.getProtocol(), MailProtocol.POP));

        if (mailServerPopModel.getHost() != null) {
            popMailServer.setHostname(mailServerPopModel.getHost());
        }

        if (mailServerPopModel.getPort() != null) {
            popMailServer.setPort(String.valueOf(mailServerPopModel.getPort()));
        } else {
            popMailServer.setPort(popMailServer.getMailProtocol().getDefaultPort());
        }

        if (mailServerPopModel.getUsername() != null) {
            popMailServer.setUsername(mailServerPopModel.getUsername());
        }

        popMailServer.setTimeout(mailServerPopModel.getTimeout());

        try {
            if (mailServerManager.getDefaultPopMailServer() != null) {
                mailServerManager.update(popMailServer);
            } else {
                popMailServer.setId(mailServerManager.create(popMailServer));
            }
        } catch (MailException e) {
            throw new BadRequestException(e.getMessage());
        }

        return mailServerPopModel;
    }

}
