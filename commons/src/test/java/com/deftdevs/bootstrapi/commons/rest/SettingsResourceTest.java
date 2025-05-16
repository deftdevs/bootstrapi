package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestSettingsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SettingsResourceTest {

    @Mock
    private SettingsService settingsService;

    private TestSettingsResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestSettingsResourceImpl(settingsService);
    }

    @Test
    void testGetSettings() {
        final SettingsModel bean = SettingsModel.EXAMPLE_1;

        doReturn(bean).when(settingsService).getSettingsGeneral();

        final Response response = resource.getSettings();
        assertEquals(200, response.getStatus());
        final SettingsModel settingsModel = (SettingsModel) response.getEntity();

        assertEquals(settingsModel, bean);
    }

    @Test
    void testSetSettings() {
        final SettingsModel bean = SettingsModel.EXAMPLE_1;

        doReturn(bean).when(settingsService).setSettingsGeneral(bean);

        final Response response = resource.setSettings(bean);
        assertEquals(200, response.getStatus());
        final SettingsModel settingsModel = (SettingsModel) response.getEntity();

        assertEquals(settingsModel, bean);
    }
}
