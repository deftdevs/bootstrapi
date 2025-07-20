package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.exception.ObjectNotFoundException;
import com.atlassian.crowd.manager.property.PropertyManager;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesModel;
import com.deftdevs.bootstrapi.crowd.service.api.MailTemplatesService;

import static com.atlassian.crowd.model.property.Property.*;

public class MailTemplatesServiceImpl implements MailTemplatesService {

    private final PropertyManager propertyManager;

    public MailTemplatesServiceImpl(
            final PropertyManager propertyManager) {

        this.propertyManager = propertyManager;
    }

    @Override
    public MailTemplatesModel getMailTemplates() {
        try {
            return MailTemplatesModel.builder()
                .forgottenPassword(propertyManager.getProperty(FORGOTTEN_PASSWORD_EMAIL_TEMPLATE))
                .forgottenUsername(propertyManager.getProperty(FORGOTTEN_USERNAME_EMAIL_TEMPLATE))
                .passwordExpirationReminder(propertyManager.getProperty(PASSWORD_EXPIRATION_REMINDER_EMAIL_TEMPLATE))
                .emailChangeValidation(propertyManager.getProperty(EMAIL_CHANGE_VALIDATION_EMAIL_TEMPLATE))
                .emailChangeInfo(propertyManager.getProperty(EMAIL_CHANGE_INFO_EMAIL_TEMPLATE))
                .build();
        } catch (final ObjectNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public MailTemplatesModel setMailTemplates(
            final MailTemplatesModel mailTemplatesModel) {

        if (mailTemplatesModel.getForgottenPassword() != null) {
            propertyManager.setProperty(FORGOTTEN_PASSWORD_EMAIL_TEMPLATE, mailTemplatesModel.getForgottenPassword());
        }

        if (mailTemplatesModel.getForgottenUsername() != null) {
            propertyManager.setProperty(FORGOTTEN_USERNAME_EMAIL_TEMPLATE, mailTemplatesModel.getForgottenUsername());
        }

        if (mailTemplatesModel.getPasswordExpirationReminder() != null) {
            propertyManager.setProperty(PASSWORD_EXPIRATION_REMINDER_EMAIL_TEMPLATE, mailTemplatesModel.getPasswordExpirationReminder());
        }

        if (mailTemplatesModel.getEmailChangeValidation() != null) {
            propertyManager.setProperty(EMAIL_CHANGE_VALIDATION_EMAIL_TEMPLATE, mailTemplatesModel.getEmailChangeValidation());
        }

        if (mailTemplatesModel.getEmailChangeInfo() != null) {
            propertyManager.setProperty(EMAIL_CHANGE_INFO_EMAIL_TEMPLATE, mailTemplatesModel.getEmailChangeInfo());
        }

        return getMailTemplates();
    }
}
