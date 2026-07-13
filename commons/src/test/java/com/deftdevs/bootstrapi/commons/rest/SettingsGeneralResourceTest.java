package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestSettingsGeneralResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsGeneralService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SettingsGeneralResourceTest {

    @Mock
    private SettingsGeneralService settingsService;

    private TestSettingsGeneralResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestSettingsGeneralResourceImpl(settingsService);
    }

    @Test
    void testGetSettings() {
        final SettingsGeneralModel bean = SettingsGeneralModel.EXAMPLE_1;

        doReturn(bean).when(settingsService).getSettingsGeneral();

        final Response response = resource.getSettings();
        assertEquals(200, response.getStatus());
        final SettingsGeneralModel settingsModel = (SettingsGeneralModel) response.getEntity();

        assertEquals(settingsModel, bean);
    }

    @Test
    void testSetSettings() {
        final SettingsGeneralModel bean = SettingsGeneralModel.EXAMPLE_1;

        doReturn(bean).when(settingsService).setSettingsGeneral(bean);

        final Response response = resource.setSettings(bean);
        assertEquals(200, response.getStatus());
        final SettingsGeneralModel settingsModel = (SettingsGeneralModel) response.getEntity();

        assertEquals(settingsModel, bean);
    }
}
