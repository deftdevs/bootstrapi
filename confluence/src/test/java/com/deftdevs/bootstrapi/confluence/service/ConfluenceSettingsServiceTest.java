package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.confluence.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConfluenceSettingsServiceTest {

    @Mock
    private GlobalSettingsManager globalSettingsManager;

    @Mock
    private SettingsBrandingService settingsBrandingService;

    private SettingsServiceImpl settingsService;

    @BeforeEach
    void setup() {
        settingsService = spy(new SettingsServiceImpl(globalSettingsManager, settingsBrandingService));
    }

    @Test
    void testGetSettings() {
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(settingsService).getSettingsGeneral();
        doReturn(SettingsSecurityModel.EXAMPLE_1).when(settingsService).getSettingsSecurity();
        doReturn(SettingsBrandingColorSchemeModel.EXAMPLE_1).when(settingsBrandingService).getColourScheme();
        doReturn(SettingsCustomHtmlModel.EXAMPLE_1).when(settingsService).getCustomHtml();

        final SettingsModel settingsModel = settingsService.getSettings();

        assertEquals(SettingsGeneralModel.EXAMPLE_1, settingsModel.getGeneral());
        assertEquals(SettingsSecurityModel.EXAMPLE_1, settingsModel.getSecurity());
        assertEquals(SettingsBrandingColorSchemeModel.EXAMPLE_1, settingsModel.getBranding().getColorScheme());
        assertEquals(SettingsCustomHtmlModel.EXAMPLE_1, settingsModel.getCustomHtml());
    }

    @Test
    void testSetSettingsAppliesAllSubFields() {
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(settingsService).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
        doReturn(SettingsSecurityModel.EXAMPLE_1).when(settingsService).setSettingsSecurity(SettingsSecurityModel.EXAMPLE_1);
        doReturn(SettingsBrandingColorSchemeModel.EXAMPLE_1).when(settingsBrandingService).setColourScheme(SettingsBrandingColorSchemeModel.EXAMPLE_1);
        doReturn(SettingsCustomHtmlModel.EXAMPLE_1).when(settingsService).setCustomHtml(SettingsCustomHtmlModel.EXAMPLE_1);

        final ServiceResult<SettingsModel> result = settingsService.setSettings(SettingsModel.builder()
                .general(SettingsGeneralModel.EXAMPLE_1)
                .security(SettingsSecurityModel.EXAMPLE_1)
                .branding(SettingsBrandingModel.builder()
                        .colorScheme(SettingsBrandingColorSchemeModel.EXAMPLE_1)
                        .build())
                .customHtml(SettingsCustomHtmlModel.EXAMPLE_1)
                .build());

        assertEquals(SettingsGeneralModel.EXAMPLE_1, result.getModel().getGeneral());
        assertEquals(SettingsSecurityModel.EXAMPLE_1, result.getModel().getSecurity());
        assertEquals(SettingsBrandingColorSchemeModel.EXAMPLE_1, result.getModel().getBranding().getColorScheme());
        assertEquals(SettingsCustomHtmlModel.EXAMPLE_1, result.getModel().getCustomHtml());

        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsSecurityModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.pathOf(SettingsModel.class, SettingsBrandingColorSchemeModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsCustomHtmlModel.class)).getStatus());
    }

    @Test
    void testSetSettingsSkipsNullSubFields() {
        final ServiceResult<SettingsModel> result = settingsService.setSettings(new SettingsModel());

        assertTrue(result.getStatus().isEmpty());
        verify(settingsService, never()).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
    }

    @Test
    void testSetSettingsRecordsPerSubFieldFailure() {
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(settingsService).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
        doThrow(new BadRequestException("invalid colour scheme"))
                .when(settingsBrandingService).setColourScheme(SettingsBrandingColorSchemeModel.EXAMPLE_1);

        final ServiceResult<SettingsModel> result = settingsService.setSettings(SettingsModel.builder()
                .general(SettingsGeneralModel.EXAMPLE_1)
                .branding(SettingsBrandingModel.builder()
                        .colorScheme(SettingsBrandingColorSchemeModel.EXAMPLE_1)
                        .build())
                .build());

        assertEquals(SettingsGeneralModel.EXAMPLE_1, result.getModel().getGeneral());
        assertNull(result.getModel().getBranding().getColorScheme());
        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(400, result.getStatus().get(FieldNames.pathOf(SettingsModel.class, SettingsBrandingColorSchemeModel.class)).getStatus());
    }
}
