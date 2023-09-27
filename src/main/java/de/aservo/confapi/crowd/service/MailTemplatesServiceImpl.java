package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.exception.ObjectNotFoundException;
import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.crowd.model.MailTemplatesBean;
import de.aservo.confapi.crowd.service.api.MailTemplatesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.atlassian.crowd.model.property.Property.*;

@Component
@ExportAsService(MailTemplatesService.class)
public class MailTemplatesServiceImpl implements MailTemplatesService {

    private final PropertyManager propertyManager;

    @Inject
    public MailTemplatesServiceImpl(
            final PropertyManager propertyManager) {

        this.propertyManager = propertyManager;
    }

    @Override
    public MailTemplatesBean getMailTemplates() {
        final MailTemplatesBean mailTemplatesBean = new MailTemplatesBean();

        try {
            mailTemplatesBean.setForgottenPassword(propertyManager.getProperty(FORGOTTEN_PASSWORD_EMAIL_TEMPLATE));
            mailTemplatesBean.setForgottenUsername(propertyManager.getProperty(FORGOTTEN_USERNAME_EMAIL_TEMPLATE));
            mailTemplatesBean.setPasswordExpirationReminder(propertyManager.getProperty(PASSWORD_EXPIRATION_REMINDER_EMAIL_TEMPLATE));
            mailTemplatesBean.setEmailChangeValidation(propertyManager.getProperty(EMAIL_CHANGE_VALIDATION_EMAIL_TEMPLATE));
            mailTemplatesBean.setEmailChangeInfo(propertyManager.getProperty(EMAIL_CHANGE_INFO_EMAIL_TEMPLATE));
        } catch (final ObjectNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }

        return mailTemplatesBean;
    }

    @Override
    public MailTemplatesBean setMailTemplates(
            final MailTemplatesBean mailTemplatesBean) {

        if (mailTemplatesBean.getForgottenPassword() != null) {
            propertyManager.setProperty(FORGOTTEN_PASSWORD_EMAIL_TEMPLATE, mailTemplatesBean.getForgottenPassword());
        }

        if (mailTemplatesBean.getForgottenUsername() != null) {
            propertyManager.setProperty(FORGOTTEN_USERNAME_EMAIL_TEMPLATE, mailTemplatesBean.getForgottenUsername());
        }

        if (mailTemplatesBean.getPasswordExpirationReminder() != null) {
            propertyManager.setProperty(PASSWORD_EXPIRATION_REMINDER_EMAIL_TEMPLATE, mailTemplatesBean.getPasswordExpirationReminder());
        }

        if (mailTemplatesBean.getEmailChangeValidation() != null) {
            propertyManager.setProperty(EMAIL_CHANGE_VALIDATION_EMAIL_TEMPLATE, mailTemplatesBean.getEmailChangeValidation());
        }

        if (mailTemplatesBean.getEmailChangeInfo() != null) {
            propertyManager.setProperty(EMAIL_CHANGE_INFO_EMAIL_TEMPLATE, mailTemplatesBean.getEmailChangeInfo());
        }

        return getMailTemplates();
    }
}
