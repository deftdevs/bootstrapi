package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.deftdevs.bootstrapi.commons.exception.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URISyntaxException;

import static com.deftdevs.bootstrapi.commons.model.SettingsBean.EXAMPLE_1_NO_MODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SettingsServiceTest {

    @Mock
    private PropertyManager propertyManager;

    public static final SettingsBean settingsBean = EXAMPLE_1_NO_MODE;

    private SettingsServiceImpl settingsService;

    @BeforeEach
    public void setup() {
        settingsService = new SettingsServiceImpl(propertyManager);
    }

    @Test
    public void testGetSettingsGeneral() throws PropertyManagerException {
        doReturn(settingsBean.getTitle()).when(propertyManager).getDeploymentTitle();
        doReturn(settingsBean.getBaseUrl()).when(propertyManager).getBaseUrl();
        assertEquals(settingsBean.getTitle(), settingsService.getSettingsGeneral().getTitle());
        assertEquals(settingsBean.getBaseUrl(), settingsService.getSettingsGeneral().getBaseUrl());
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
        settingsService.setSettingsGeneral(settingsBean);
        verify(propertyManager).setBaseUrl(settingsBean.getBaseUrl());
        verify(propertyManager).setDeploymentTitle(settingsBean.getTitle());
    }

}
