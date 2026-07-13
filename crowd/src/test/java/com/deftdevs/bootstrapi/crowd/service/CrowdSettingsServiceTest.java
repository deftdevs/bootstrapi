package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.util.FieldNames;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingModel;
import com.deftdevs.bootstrapi.crowd.model.SettingsModel;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsBrandingService;
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
class CrowdSettingsServiceTest {

    @Mock
    private PropertyManager propertyManager;

    @Mock
    private CrowdSettingsBrandingService settingsBrandingService;

    private SettingsServiceImpl settingsService;

    @BeforeEach
    void setup() {
        settingsService = spy(new SettingsServiceImpl(propertyManager, settingsBrandingService));
    }

    @Test
    void testGetSettings() {
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(settingsService).getSettingsGeneral();
        doReturn(SettingsBrandingLoginPageModel.EXAMPLE_1).when(settingsBrandingService).getSettingsBrandingLoginPage();

        final SettingsModel settingsModel = settingsService.getSettings();

        assertEquals(SettingsGeneralModel.EXAMPLE_1, settingsModel.getGeneral());
        assertEquals(SettingsBrandingLoginPageModel.EXAMPLE_1, settingsModel.getBranding().getLoginPage());
    }

    @Test
    void testSetSettingsAppliesAllSubFields() {
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(settingsService).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
        doReturn(SettingsBrandingLoginPageModel.EXAMPLE_1).when(settingsBrandingService).setSettingsBrandingLoginPage(SettingsBrandingLoginPageModel.EXAMPLE_1);

        final ServiceResult<SettingsModel> result = settingsService.setSettings(SettingsModel.builder()
                .general(SettingsGeneralModel.EXAMPLE_1)
                .branding(SettingsBrandingModel.builder()
                        .loginPage(SettingsBrandingLoginPageModel.EXAMPLE_1)
                        .build())
                .build());

        assertEquals(SettingsGeneralModel.EXAMPLE_1, result.getModel().getGeneral());
        assertEquals(SettingsBrandingLoginPageModel.EXAMPLE_1, result.getModel().getBranding().getLoginPage());
        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(200, result.getStatus().get(FieldNames.pathOf(SettingsModel.class, SettingsBrandingLoginPageModel.class)).getStatus());
    }

    @Test
    void testSetSettingsRejectsSecurity() {
        final ServiceResult<SettingsModel> result = settingsService.setSettings(SettingsModel.builder()
                .security(SettingsSecurityModel.EXAMPLE_1)
                .build());

        assertNull(result.getModel().getSecurity());
        assertEquals(400, result.getStatus().get(FieldNames.of(SettingsSecurityModel.class)).getStatus());
    }

    @Test
    void testSetSettingsSkipsNullSubFields() {
        final ServiceResult<SettingsModel> result = settingsService.setSettings(new SettingsModel());

        assertTrue(result.getStatus().isEmpty());
        verify(settingsService, never()).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
        verify(settingsBrandingService, never()).setSettingsBrandingLoginPage(SettingsBrandingLoginPageModel.EXAMPLE_1);
    }

    @Test
    void testSetSettingsRecordsPerSubFieldFailure() {
        doReturn(SettingsGeneralModel.EXAMPLE_1).when(settingsService).setSettingsGeneral(SettingsGeneralModel.EXAMPLE_1);
        doThrow(new BadRequestException("invalid login page"))
                .when(settingsBrandingService).setSettingsBrandingLoginPage(SettingsBrandingLoginPageModel.EXAMPLE_1);

        final ServiceResult<SettingsModel> result = settingsService.setSettings(SettingsModel.builder()
                .general(SettingsGeneralModel.EXAMPLE_1)
                .branding(SettingsBrandingModel.builder()
                        .loginPage(SettingsBrandingLoginPageModel.EXAMPLE_1)
                        .build())
                .build());

        assertEquals(SettingsGeneralModel.EXAMPLE_1, result.getModel().getGeneral());
        assertNull(result.getModel().getBranding().getLoginPage());
        assertEquals(200, result.getStatus().get(FieldNames.of(SettingsModel.class, SettingsGeneralModel.class)).getStatus());
        assertEquals(400, result.getStatus().get(FieldNames.pathOf(SettingsModel.class, SettingsBrandingLoginPageModel.class)).getStatus());
    }
}
