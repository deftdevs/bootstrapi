package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.jira.model.SettingsBrandingBannerModel;
import com.deftdevs.bootstrapi.jira.model.SettingsBrandingModel;
import com.deftdevs.bootstrapi.jira.model.SettingsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static com.atlassian.jira.config.properties.APKeys.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        final SettingsGeneralModel settingsModel = settingsService.getSettingsGeneral();

        assertEquals(BASE_URL, settingsModel.getBaseUrl());
        assertEquals(MODE_PUBLIC, settingsModel.getMode());
        assertEquals(TITLE, settingsModel.getTitle());
        assertEquals(CONTACT_MESSAGE, settingsModel.getContactMessage());
        assertEquals(EXTERNAL_USER_MANAGEMENT, String.valueOf(settingsModel.getExternalUserManagement()));
    }

    @Test
    void testSetSettingsGeneral() {
        final SettingsGeneralModel settingsModel = SettingsGeneralModel.builder()
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
        final SettingsGeneralModel settingsModel = SettingsGeneralModel.builder().build();

        settingsService.setSettingsGeneral(settingsModel);

        verify(applicationProperties, never()).setString(JIRA_BASEURL, BASE_URL.toString());
        verify(applicationProperties, never()).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties, never()).setString(JIRA_TITLE, TITLE);
        verify(applicationProperties, never()).setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, CONTACT_MESSAGE);
    }

    @Test
    void testSetSettingsGeneralUnsupportedMode() {
        final SettingsGeneralModel settingsModel = SettingsGeneralModel.builder().mode("unsupported").build();

        assertThrows(BadRequestException.class, () -> {
            settingsService.setSettingsGeneral(settingsModel);
        });
    }

    @Test
    void testSetSettingsGeneralInvalidCombination() {
        final SettingsGeneralModel settingsModel = SettingsGeneralModel.builder().mode(MODE_PUBLIC).build();
        doReturn(true).when(applicationProperties).getOption(JIRA_OPTION_USER_EXTERNALMGT);

        assertThrows(BadRequestException.class, () -> {
            settingsService.setSettingsGeneral(settingsModel);
        });
    }

    // composite getSettings/setSettings default methods

    @Test
    void testGetSettings() {
        final SettingsServiceImpl serviceSpy = spy(settingsService);
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(serviceSpy).getSettingsGeneral();
        doReturn(SettingsSecurityModel.EXAMPLE_1).when(serviceSpy).getSettingsSecurity();
        doReturn(SettingsBrandingBannerModel.EXAMPLE_1).when(serviceSpy).getSettingsBrandingBanner();

        final SettingsModel settingsModel = serviceSpy.getSettings();

        assertEquals(SettingsGeneralModel.EXAMPLE_1, settingsModel.getGeneral());
        assertEquals(SettingsSecurityModel.EXAMPLE_1, settingsModel.getSecurity());
        assertEquals(SettingsBrandingBannerModel.EXAMPLE_1, settingsModel.getBranding().getBanner());
    }

    @Test
    void testSetSettingsAppliesAllSubFields() {
        final SettingsServiceImpl serviceSpy = spy(settingsService);
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(serviceSpy).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
        doReturn(SettingsSecurityModel.EXAMPLE_1).when(serviceSpy).setSettingsSecurity(SettingsSecurityModel.EXAMPLE_1);
        doReturn(SettingsBrandingBannerModel.EXAMPLE_1).when(serviceSpy).setSettingsBrandingBanner(SettingsBrandingBannerModel.EXAMPLE_1);

        final ServiceResult<SettingsModel> result = serviceSpy.setSettings(SettingsModel.builder()
                .general(SettingsGeneralModel.EXAMPLE_1)
                .security(SettingsSecurityModel.EXAMPLE_1)
                .branding(SettingsBrandingModel.builder()
                        .banner(SettingsBrandingBannerModel.EXAMPLE_1)
                        .build())
                .build());

        assertEquals(SettingsGeneralModel.EXAMPLE_1, result.getModel().getGeneral());
        assertEquals(SettingsSecurityModel.EXAMPLE_1, result.getModel().getSecurity());
        assertEquals(SettingsBrandingBannerModel.EXAMPLE_1, result.getModel().getBranding().getBanner());
        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsSecurityModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.pathOf(SettingsModel.class, SettingsBrandingBannerModel.class)).getStatus());
    }

    @Test
    void testSetSettingsSkipsNullSubFields() {
        final SettingsServiceImpl serviceSpy = spy(settingsService);

        final ServiceResult<SettingsModel> result = serviceSpy.setSettings(new SettingsModel());

        assertEquals(0, result.getStatus().size());
        verify(serviceSpy, never()).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
    }

    @Test
    void testSetSettingsRecordsPerSubFieldFailure() {
        final SettingsServiceImpl serviceSpy = spy(settingsService);
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(serviceSpy).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
        doThrow(new BadRequestException("invalid banner")).when(serviceSpy).setSettingsBrandingBanner(SettingsBrandingBannerModel.EXAMPLE_1);

        final ServiceResult<SettingsModel> result = serviceSpy.setSettings(SettingsModel.builder()
                .general(SettingsGeneralModel.EXAMPLE_1)
                .branding(SettingsBrandingModel.builder()
                        .banner(SettingsBrandingBannerModel.EXAMPLE_1)
                        .build())
                .build());

        assertEquals(SettingsGeneralModel.EXAMPLE_1, result.getModel().getGeneral());
        assertNull(result.getModel().getBranding().getBanner());
        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(400, result.getStatus().get(FieldNames.pathOf(SettingsModel.class, SettingsBrandingBannerModel.class)).getStatus());
    }

}
