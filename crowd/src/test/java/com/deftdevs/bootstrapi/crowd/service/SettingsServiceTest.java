package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.deftdevs.bootstrapi.commons.exception.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URISyntaxException;

import static com.deftdevs.bootstrapi.commons.model.SettingsBean.EXAMPLE_1_NO_MODE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SettingsServiceTest {

    @Mock
    private PropertyManager propertyManager;

    public static final SettingsBean settingsBean = EXAMPLE_1_NO_MODE;

    private SettingsServiceImpl settingsService;

    @Before
    public void setup() {
        settingsService = new SettingsServiceImpl(propertyManager);
    }

    @Test
    public void testGetSettings() throws PropertyManagerException {
        doReturn(settingsBean.getTitle()).when(propertyManager).getDeploymentTitle();
        doReturn(settingsBean.getBaseUrl()).when(propertyManager).getBaseUrl();
        assertEquals(settingsBean.getTitle(), settingsService.getSettings().getTitle());
        assertEquals(settingsBean.getBaseUrl(), settingsService.getSettings().getBaseUrl());
    }

    @Test(expected = InternalServerErrorException.class)
    public void testGetSettingsWithInternalServerErrorException() throws PropertyManagerException, URISyntaxException {
        doThrow(new PropertyManagerException()).when(propertyManager).getBaseUrl();
        settingsService.getSettings();
    }

    @Test
    public void testSetSettings() {
        settingsService.setSettings(settingsBean);
        verify(propertyManager).setBaseUrl(settingsBean.getBaseUrl());
        verify(propertyManager).setDeploymentTitle(settingsBean.getTitle());
    }

}
