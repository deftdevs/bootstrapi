package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URISyntaxException;

import static com.deftdevs.bootstrapi.commons.model.SettingsModel.EXAMPLE_1_NO_MODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SettingsServiceTest {

    @Mock
    private PropertyManager propertyManager;

    public static final SettingsModel SETTINGS_MODEL = EXAMPLE_1_NO_MODE;

    private SettingsServiceImpl settingsService;

    @BeforeEach
    public void setup() {
        settingsService = new SettingsServiceImpl(propertyManager);
    }

    @Test
    public void testGetSettingsGeneral() throws PropertyManagerException {
        doReturn(SETTINGS_MODEL.getTitle()).when(propertyManager).getDeploymentTitle();
        doReturn(SETTINGS_MODEL.getBaseUrl()).when(propertyManager).getBaseUrl();
        assertEquals(SETTINGS_MODEL.getTitle(), settingsService.getSettingsGeneral().getTitle());
        assertEquals(SETTINGS_MODEL.getBaseUrl(), settingsService.getSettingsGeneral().getBaseUrl());
    }

    @Test
    public void testGetSettingsGeneralWithInternalServerErrorException() throws PropertyManagerException, URISyntaxException {
        doThrow(new PropertyManagerException()).when(propertyManager).getBaseUrl();

        assertThrows(InternalServerErrorException.class, () -> {
            settingsService.getSettingsGeneral();
        });
    }

    @Test
    public void testSetSettingsGeneral() {
        settingsService.setSettingsGeneral(SETTINGS_MODEL);
        verify(propertyManager).setBaseUrl(SETTINGS_MODEL.getBaseUrl());
        verify(propertyManager).setDeploymentTitle(SETTINGS_MODEL.getTitle());
    }

}
