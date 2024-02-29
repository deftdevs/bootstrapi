package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.SettingsBean;
import de.aservo.confapi.commons.rest.impl.TestSettingsResourceImpl;
import de.aservo.confapi.commons.service.api.SettingsService;
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
        final SettingsBean bean = SettingsBean.EXAMPLE_1;

        doReturn(bean).when(settingsService).getSettings();

        final Response response = resource.getSettings();
        assertEquals(200, response.getStatus());
        final SettingsBean settingsBean = (SettingsBean) response.getEntity();

        assertEquals(settingsBean, bean);
    }

    @Test
    void testSetSettings() {
        final SettingsBean bean = SettingsBean.EXAMPLE_1;

        doReturn(bean).when(settingsService).setSettings(bean);

        final Response response = resource.setSettings(bean);
        assertEquals(200, response.getStatus());
        final SettingsBean settingsBean = (SettingsBean) response.getEntity();

        assertEquals(settingsBean, bean);
    }
}
