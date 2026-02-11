package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static com.atlassian.jira.config.properties.APKeys.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JiraSettingsServiceTest {

    private static final URI BASE_URL = URI.create("https://jira.atlassian.com");
    private static final String MODE_PUBLIC = "public";
    private static final String TITLE = "Atlassian Public JIRA";
    private static final String CONTACT_MESSAGE = "Test Contact Message";
    private static final String EXTERNAL_USER_MANAGEMENT = "true";

    @Mock
    private ApplicationProperties applicationProperties;

    private SettingsServiceImpl settingsService;

    @BeforeEach
    public void setup() {
        settingsService = new SettingsServiceImpl(applicationProperties);
    }

    @Test
    void testGetSettingsGeneral() {
        doReturn(BASE_URL.toString()).when(applicationProperties).getString(JIRA_BASEURL);
        doReturn(MODE_PUBLIC).when(applicationProperties).getString(JIRA_MODE);
        doReturn(TITLE).when(applicationProperties).getString(JIRA_TITLE);
        doReturn(CONTACT_MESSAGE).when(applicationProperties).getString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE);
        doReturn(EXTERNAL_USER_MANAGEMENT).when(applicationProperties).getString(JIRA_OPTION_USER_EXTERNALMGT);

        final SettingsModel settingsModel = settingsService.getSettingsGeneral();

        assertEquals(BASE_URL, settingsModel.getBaseUrl());
        assertEquals(MODE_PUBLIC, settingsModel.getMode());
        assertEquals(TITLE, settingsModel.getTitle());
        assertEquals(CONTACT_MESSAGE, settingsModel.getContactMessage());
        assertEquals(EXTERNAL_USER_MANAGEMENT, String.valueOf(settingsModel.getExternalUserManagement()));
    }

    @Test
    void testSetSettingsGeneral() {
        final SettingsModel settingsModel = SettingsModel.builder()
            .baseUrl(BASE_URL)
            .mode(MODE_PUBLIC)
            .title(TITLE)
            .contactMessage(CONTACT_MESSAGE)
            .externalUserManagement(Boolean.parseBoolean(EXTERNAL_USER_MANAGEMENT))
            .build();

        settingsService.setSettingsGeneral(settingsModel);

        verify(applicationProperties).setString(JIRA_BASEURL, BASE_URL.toString());
        verify(applicationProperties).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties).setString(JIRA_TITLE, TITLE);
        verify(applicationProperties).setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, CONTACT_MESSAGE);
        verify(applicationProperties).setString(JIRA_OPTION_USER_EXTERNALMGT, EXTERNAL_USER_MANAGEMENT);
    }

    @Test
    void testSetSettingsGeneralEmptyModel() {
        final SettingsModel settingsModel = SettingsModel.builder().build();

        settingsService.setSettingsGeneral(settingsModel);

        verify(applicationProperties, never()).setString(JIRA_BASEURL, BASE_URL.toString());
        verify(applicationProperties, never()).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties, never()).setString(JIRA_TITLE, TITLE);
        verify(applicationProperties, never()).setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, CONTACT_MESSAGE);
    }

    @Test
    void testSetSettingsGeneralUnsupportedMode() {
        final SettingsModel settingsModel = SettingsModel.builder().mode("unsupported").build();

        assertThrows(BadRequestException.class, () -> {
            settingsService.setSettingsGeneral(settingsModel);
        });
    }

    @Test
    void testSetSettingsGeneralInvalidCombination() {
        final SettingsModel settingsModel = SettingsModel.builder().mode(MODE_PUBLIC).build();
        doReturn(true).when(applicationProperties).getOption(JIRA_OPTION_USER_EXTERNALMGT);

        assertThrows(BadRequestException.class, () -> {
            settingsService.setSettingsGeneral(settingsModel);
        });
    }

}
