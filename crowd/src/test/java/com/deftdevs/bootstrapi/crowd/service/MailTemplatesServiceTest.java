package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesModel;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static com.atlassian.crowd.model.property.Property.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class MailTemplatesServiceTest {

    private final Map<String, String> properties = new HashMap<>();

    @Mock
    private PropertyManager propertyManager;

    private MailTemplatesServiceImpl mailTemplateService;

    @BeforeEach
    public void setup() {
        mailTemplateService = new MailTemplatesServiceImpl(propertyManager);

        setupPropertyManager();
    }

    @SneakyThrows
    private void setupPropertyManager() {
        // mock the property manager with the properties map

        doAnswer(invocation -> {
            String key = invocation.getArgument(0, String.class);
            return properties.get(key);
        }).when(propertyManager).getProperty(anyString());

        lenient().doAnswer(invocation -> {
            String key = invocation.getArgument(0, String.class);
            String value = invocation.getArgument(1, String.class);
            properties.put(key, value);
            return null;
        }).when(propertyManager).setProperty(anyString(), anyString());

        properties.put(FORGOTTEN_PASSWORD_EMAIL_TEMPLATE, FORGOTTEN_PASSWORD_EMAIL_TEMPLATE);
        properties.put(FORGOTTEN_USERNAME_EMAIL_TEMPLATE, FORGOTTEN_USERNAME_EMAIL_TEMPLATE);
        properties.put(PASSWORD_EXPIRATION_REMINDER_EMAIL_TEMPLATE, PASSWORD_EXPIRATION_REMINDER_EMAIL_TEMPLATE);
        properties.put(EMAIL_CHANGE_VALIDATION_EMAIL_TEMPLATE, EMAIL_CHANGE_VALIDATION_EMAIL_TEMPLATE);
        properties.put(EMAIL_CHANGE_INFO_EMAIL_TEMPLATE, EMAIL_CHANGE_INFO_EMAIL_TEMPLATE);
    }

    @Test
    public void testGetMailTemplates() {
        final MailTemplatesModel mailTemplates = mailTemplateService.getMailTemplates();

        assertEquals(FORGOTTEN_PASSWORD_EMAIL_TEMPLATE, mailTemplates.getForgottenPassword());
        assertEquals(FORGOTTEN_USERNAME_EMAIL_TEMPLATE, mailTemplates.getForgottenUsername());
        assertEquals(PASSWORD_EXPIRATION_REMINDER_EMAIL_TEMPLATE, mailTemplates.getPasswordExpirationReminder());
        assertEquals(EMAIL_CHANGE_VALIDATION_EMAIL_TEMPLATE, mailTemplates.getEmailChangeValidation());
        assertEquals(EMAIL_CHANGE_INFO_EMAIL_TEMPLATE, mailTemplates.getEmailChangeInfo());
    }

    @Test
    public void testSetMailTemplates() {
        final MailTemplatesModel inputMailTemplatesModel = new MailTemplatesModel();
        inputMailTemplatesModel.setForgottenPassword("1");
        inputMailTemplatesModel.setForgottenUsername("2");
        inputMailTemplatesModel.setPasswordExpirationReminder("3");
        inputMailTemplatesModel.setEmailChangeValidation("4");
        inputMailTemplatesModel.setEmailChangeInfo("5");

        final MailTemplatesModel outputMailTemplatesModel = mailTemplateService.setMailTemplates(inputMailTemplatesModel);

        assertEquals(inputMailTemplatesModel, outputMailTemplatesModel);
    }
}
