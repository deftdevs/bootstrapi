package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MailConfigurationService;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import com.deftdevs.bootstrapi.commons.service.api.MailServerService;
import com.deftdevs.bootstrapi.crowd.model.util.MailServerSmtpModelUtil;

public class MailServerServiceImpl implements MailServerService {

    private final MailConfigurationService mailConfigurationService;

    public MailServerServiceImpl(
            final MailConfigurationService mailConfigurationService) {

        this.mailConfigurationService = mailConfigurationService;
    }

    @Override
    public MailServerSmtpModel getMailServerSmtp() {
        if (mailConfigurationService.isConfigured()) {
            return MailServerSmtpModelUtil.toMailServerSmtpModel(mailConfigurationService.getMailConfiguration());
        }

        return null;
    }

    @Override
    public MailServerSmtpModel setMailServerSmtp(
            final MailServerSmtpModel mailServerSmtpModel) {

        final MailConfiguration mailConfiguration = mailConfigurationService.getMailConfiguration();
        mailConfigurationService.saveConfiguration(MailServerSmtpModelUtil.toMailConfiguration(mailServerSmtpModel, mailConfiguration));
        return getMailServerSmtp();
    }

    @Override
    public MailServerPopModel getMailServerPop() {
        throw new UnsupportedOperationException("Getting POP mail server is not supported by Crowd");
    }

    @Override
    public MailServerPopModel setMailServerPop(
            final MailServerPopModel mailServerPopModel) {

        throw new UnsupportedOperationException("Setting POP mail server is not supported by Crowd");
    }
}
