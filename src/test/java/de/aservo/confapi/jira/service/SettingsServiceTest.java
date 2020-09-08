package de.aservo.confapi.jira.service;

import com.atlassian.jira.config.properties.ApplicationProperties;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.SettingsBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.atlassian.jira.config.properties.APKeys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SettingsServiceTest {

    private static final String BASE_URL = "https://jira.atlassian.com";
    private static final String MODE_PUBLIC = "public";
    private static final String TITLE = "Atlassian Public JIRA";

    @Mock
    private ApplicationProperties applicationProperties;

    private SettingsServiceImpl settingsService;

    @Before
    public void setup() {
        settingsService = new SettingsServiceImpl(applicationProperties);
    }

    @Test
    public void testGetSettings() {
        doReturn(BASE_URL).when(applicationProperties).getString(JIRA_BASEURL);
        doReturn(MODE_PUBLIC).when(applicationProperties).getString(JIRA_MODE);
        doReturn(TITLE).when(applicationProperties).getString(JIRA_TITLE);

        final SettingsBean settingsBean = settingsService.getSettings();

        assertEquals(BASE_URL, settingsBean.getBaseUrl());
        assertEquals(MODE_PUBLIC, settingsBean.getMode());
        assertEquals(TITLE, settingsBean.getTitle());
    }

    @Test
    public void testSetSettings() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(BASE_URL);
        settingsBean.setMode(MODE_PUBLIC);
        settingsBean.setTitle(TITLE);

        settingsService.setSettings(settingsBean);

        verify(applicationProperties).setString(JIRA_BASEURL, BASE_URL);
        verify(applicationProperties).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties).setString(JIRA_TITLE, TITLE);
    }

    @Test
    public void testSetSettingsEmptyBean() {
        final SettingsBean settingsBean = new SettingsBean();

        settingsService.setSettings(settingsBean);

        verify(applicationProperties, never()).setString(JIRA_BASEURL, BASE_URL);
        verify(applicationProperties, never()).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties, never()).setString(JIRA_TITLE, TITLE);
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
