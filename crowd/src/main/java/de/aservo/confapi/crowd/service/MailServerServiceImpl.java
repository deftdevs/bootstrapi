package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.manager.mail.MailConfiguration;
import com.atlassian.crowd.manager.mail.MailConfigurationService;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.model.MailServerPopBean;
import de.aservo.confapi.commons.model.MailServerSmtpBean;
import de.aservo.confapi.commons.service.api.MailServerService;
import de.aservo.confapi.crowd.model.util.MailServerSmtpBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@ExportAsService(MailServerService.class)
public class MailServerServiceImpl implements MailServerService {

    @ComponentImport
    private final MailConfigurationService mailConfigurationService;

    @Inject
    public MailServerServiceImpl(
            final MailConfigurationService mailConfigurationService) {

        this.mailConfigurationService = mailConfigurationService;
    }

    @Override
    public MailServerSmtpBean getMailServerSmtp() {
        if (mailConfigurationService.isConfigured()) {
            return MailServerSmtpBeanUtil.toMailServerSmtpBean(mailConfigurationService.getMailConfiguration());
        }

        return null;
    }

    @Override
    public MailServerSmtpBean setMailServerSmtp(
            final MailServerSmtpBean mailServerSmtpBean) {

        final MailConfiguration mailConfiguration = mailConfigurationService.getMailConfiguration();
        mailConfigurationService.saveConfiguration(MailServerSmtpBeanUtil.toMailConfiguration(mailServerSmtpBean, mailConfiguration));
        return getMailServerSmtp();
    }

    @Override
    public MailServerPopBean getMailServerPop() {
        throw new UnsupportedOperationException("Getting POP mail server is not supported by Crowd");
    }

    @Override
    public MailServerPopBean setMailServerPop(
            final MailServerPopBean mailServerPopBean) {

        throw new UnsupportedOperationException("Setting POP mail server is not supported by Crowd");
    }
}
