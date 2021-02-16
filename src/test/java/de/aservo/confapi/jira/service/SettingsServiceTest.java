package de.aservo.confapi.jira.service;

import com.atlassian.jira.config.properties.ApplicationProperties;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.SettingsBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;

import static com.atlassian.jira.config.properties.APKeys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SettingsServiceTest {

    private static final URI BASE_URL = URI.create("https://jira.atlassian.com");
    private static final String MODE_PUBLIC = "public";
    private static final String TITLE = "Atlassian Public JIRA";
    private static final String CONTACT_MESSAGE = "Test Contact Message";
    private static final String EXTERNAL_USER_MANAGEMENT = "true";

    @Mock
    private ApplicationProperties applicationProperties;

    private SettingsServiceImpl settingsService;

    @Before
    public void setup() {
        settingsService = new SettingsServiceImpl(applicationProperties);
    }

    @Test
    public void testGetSettings() {
        doReturn(BASE_URL.toString()).when(applicationProperties).getString(JIRA_BASEURL);
        doReturn(MODE_PUBLIC).when(applicationProperties).getString(JIRA_MODE);
        doReturn(TITLE).when(applicationProperties).getString(JIRA_TITLE);
        doReturn(CONTACT_MESSAGE).when(applicationProperties).getString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE);
        doReturn(EXTERNAL_USER_MANAGEMENT).when(applicationProperties).getString(JIRA_OPTION_USER_EXTERNALMGT);

        final SettingsBean settingsBean = settingsService.getSettings();

        assertEquals(BASE_URL, settingsBean.getBaseUrl());
        assertEquals(MODE_PUBLIC, settingsBean.getMode());
        assertEquals(TITLE, settingsBean.getTitle());
        assertEquals(CONTACT_MESSAGE, settingsBean.getContactMessage());
        assertEquals(EXTERNAL_USER_MANAGEMENT, String.valueOf(settingsBean.getExternalUserManagement()));
    }

    @Test
    public void testSetSettings() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(BASE_URL);
        settingsBean.setMode(MODE_PUBLIC);
        settingsBean.setTitle(TITLE);
        settingsBean.setContactMessage(CONTACT_MESSAGE);
        settingsBean.setExternalUserManagement(Boolean.parseBoolean(EXTERNAL_USER_MANAGEMENT));

        settingsService.setSettings(settingsBean);

        verify(applicationProperties).setString(JIRA_BASEURL, BASE_URL.toString());
        verify(applicationProperties).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties).setString(JIRA_TITLE, TITLE);
        verify(applicationProperties).setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, CONTACT_MESSAGE);
        verify(applicationProperties).setString(JIRA_OPTION_USER_EXTERNALMGT, EXTERNAL_USER_MANAGEMENT);
    }

    @Test
    public void testSetSettingsEmptyBean() {
        final SettingsBean settingsBean = new SettingsBean();

        settingsService.setSettings(settingsBean);

        verify(applicationProperties, never()).setString(JIRA_BASEURL, BASE_URL.toString());
        verify(applicationProperties, never()).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties, never()).setString(JIRA_TITLE, TITLE);
        verify(applicationProperties, never()).setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, CONTACT_MESSAGE);
    }

    @Test(expected = BadRequestException.class)
    public void testSetSettingsUnsupportedMode() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setMode("unsupported");

        settingsService.setSettings(settingsBean);
    }

    @Test(expected = BadRequestException.class)
    public void testSetSettingsInvalidCombination() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setMode(MODE_PUBLIC);

        doReturn(true).when(applicationProperties).getOption(JIRA_OPTION_USER_EXTERNALMGT);

        settingsService.setSettings(settingsBean);
    }

}
