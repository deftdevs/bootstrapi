package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import de.aservo.confapi.crowd.model.MailTemplatesBean;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static com.atlassian.crowd.model.property.Property.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class MailTemplatesServiceTest {

    private final Map<String, String> properties = new HashMap<>();

    @Mock
    private PropertyManager propertyManager;

    private MailTemplatesServiceImpl mailTemplateService;

    @Before
    public void setup() {
        mailTemplateService = new MailTemplatesServiceImpl(propertyManager);

        setupPropertyManager();
    }

    @SneakyThrows
    private void setupPropertyManager() {
        // mock the property manager with the properties map

        doAnswer(invocation -> {
            String key = invocation.getArgumentAt(0, String.class);
            return properties.get(key);
        }).when(propertyManager).getProperty(anyString());

        doAnswer(invocation -> {
            String key = invocation.getArgumentAt(0, String.class);
            String value = invocation.getArgumentAt(1, String.class);
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
        final MailTemplatesBean mailTemplates = mailTemplateService.getMailTemplates();

        assertEquals(FORGOTTEN_PASSWORD_EMAIL_TEMPLATE, mailTemplates.getForgottenPassword());
        assertEquals(FORGOTTEN_USERNAME_EMAIL_TEMPLATE, mailTemplates.getForgottenUsername());
        assertEquals(PASSWORD_EXPIRATION_REMINDER_EMAIL_TEMPLATE, mailTemplates.getPasswordExpirationReminder());
        assertEquals(EMAIL_CHANGE_VALIDATION_EMAIL_TEMPLATE, mailTemplates.getEmailChangeValidation());
        assertEquals(EMAIL_CHANGE_INFO_EMAIL_TEMPLATE, mailTemplates.getEmailChangeInfo());
    }

    @Test
    public void testSetMailTemplates() {
        final MailTemplatesBean inputMailTemplatesBean = new MailTemplatesBean();
        inputMailTemplatesBean.setForgottenPassword("1");
        inputMailTemplatesBean.setForgottenUsername("2");
        inputMailTemplatesBean.setPasswordExpirationReminder("3");
        inputMailTemplatesBean.setEmailChangeValidation("4");
        inputMailTemplatesBean.setEmailChangeInfo("5");

        final MailTemplatesBean outputMailTemplatesBean = mailTemplateService.setMailTemplates(inputMailTemplatesBean);

        assertEquals(inputMailTemplatesBean, outputMailTemplatesBean);
    }
}
