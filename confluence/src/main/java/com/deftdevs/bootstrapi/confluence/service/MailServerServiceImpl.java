package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.jmx.JmxSMTPMailServer;
import com.atlassian.mail.MailException;
import com.atlassian.mail.MailProtocol;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.PopMailServer;
import com.atlassian.mail.server.SMTPMailServer;
import com.atlassian.mail.server.impl.PopMailServerImpl;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.MailServerPopBean;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpBean;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.confluence.model.util.MailServerPopBeanUtil;
import com.deftdevs.bootstrapi.confluence.model.util.MailServerSmtpBeanUtil;
import com.deftdevs.bootstrapi.confluence.util.MailProtocolUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@ExportAsService(MailServerService.class)
public class MailServerServiceImpl implements MailServerService {

    private final MailServerManager mailServerManager;

    @Inject
    public MailServerServiceImpl(@ComponentImport final MailServerManager mailServerManager) {
        this.mailServerManager = mailServerManager;
    }

    @Override
    public MailServerSmtpBean getMailServerSmtp() {
        final SMTPMailServer smtpMailServer = mailServerManager.getDefaultSMTPMailServer();
        return MailServerSmtpBeanUtil.toMailServerSmtpBean(smtpMailServer);
    }

    @Override
    public MailServerSmtpBean setMailServerSmtp(MailServerSmtpBean mailServerSmtpBean) {
        final SMTPMailServer smtpMailServer = mailServerManager.isDefaultSMTPMailServerDefined()
                ? mailServerManager.getDefaultSMTPMailServer()
                : new JmxSMTPMailServer();

        assert smtpMailServer != null;

        if (mailServerSmtpBean.getName() != null) {
            smtpMailServer.setName(mailServerSmtpBean.getName());
        }

        if (mailServerSmtpBean.getDescription() != null) {
            smtpMailServer.setDescription(mailServerSmtpBean.getDescription());
        }

        if (mailServerSmtpBean.getFrom() != null) {
            smtpMailServer.setDefaultFrom(mailServerSmtpBean.getFrom());
        }

        if (mailServerSmtpBean.getPrefix() != null) {
            smtpMailServer.setPrefix(mailServerSmtpBean.getPrefix());
        }

        smtpMailServer.setMailProtocol(MailProtocolUtil.find(mailServerSmtpBean.getProtocol(), MailProtocol.SMTP));

        if (mailServerSmtpBean.getHost() != null) {
            smtpMailServer.setHostname(mailServerSmtpBean.getHost());
        }

        if (mailServerSmtpBean.getPort() != null) {
            smtpMailServer.setPort(String.valueOf(mailServerSmtpBean.getPort()));
        } else {
            smtpMailServer.setPort(smtpMailServer.getMailProtocol().getDefaultPort());
        }

        smtpMailServer.setTlsRequired(mailServerSmtpBean.getUseTls());

        if (mailServerSmtpBean.getUsername() != null) {
            smtpMailServer.setUsername(mailServerSmtpBean.getUsername());
        }

        smtpMailServer.setTimeout(mailServerSmtpBean.getTimeout());

        try {
            if (mailServerManager.isDefaultSMTPMailServerDefined()) {
                mailServerManager.update(smtpMailServer);
            } else {
                smtpMailServer.setId(mailServerManager.create(smtpMailServer));
            }
        } catch (MailException e) {
            throw new BadRequestException(e);
        }

        return getMailServerSmtp();
    }

    @Override
    public MailServerPopBean getMailServerPop() {
        final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer();
        return MailServerPopBeanUtil.toMailServerPopBean(popMailServer);
    }

    @Override
    public MailServerPopBean setMailServerPop(MailServerPopBean mailServerPopBean) {
        final PopMailServer popMailServer = mailServerManager.getDefaultPopMailServer() != null
                ? mailServerManager.getDefaultPopMailServer()
                : new PopMailServerImpl();

        assert popMailServer != null;

        if (mailServerPopBean.getName() != null) {
            popMailServer.setName(mailServerPopBean.getName());
        }

        if (mailServerPopBean.getDescription() != null) {
            popMailServer.setDescription(mailServerPopBean.getDescription());
        }

        popMailServer.setMailProtocol(MailProtocolUtil.find(mailServerPopBean.getProtocol(), MailProtocol.POP));

        if (mailServerPopBean.getHost() != null) {
            popMailServer.setHostname(mailServerPopBean.getHost());
        }

        if (mailServerPopBean.getPort() != null) {
            popMailServer.setPort(String.valueOf(mailServerPopBean.getPort()));
        } else {
            popMailServer.setPort(popMailServer.getMailProtocol().getDefaultPort());
        }

        if (mailServerPopBean.getUsername() != null) {
            popMailServer.setUsername(mailServerPopBean.getUsername());
        }

        popMailServer.setTimeout(mailServerPopBean.getTimeout());

        try {
            if (mailServerManager.getDefaultPopMailServer() != null) {
                mailServerManager.update(popMailServer);
            } else {
                popMailServer.setId(mailServerManager.create(popMailServer));
            }
        } catch (MailException e) {
            throw new BadRequestException(e);
        }

        return getMailServerPop();
    }
}
