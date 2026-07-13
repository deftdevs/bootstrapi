package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;

import com.deftdevs.bootstrapi.confluence.service.api.SettingsBrandingService;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SettingsBrandingResourceTest {

    @Mock
    private SettingsBrandingService brandingService;

    private SettingsBrandingResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new SettingsBrandingResourceImpl(brandingService);
    }

    @Test
    void testGetColourScheme() {
        final SettingsBrandingColorSchemeModel bean = SettingsBrandingColorSchemeModel.EXAMPLE_1;

        doReturn(bean).when(brandingService).getColourScheme();

        final Response response = resource.getBrandingColorScheme();
        assertEquals(200, response.getStatus());
        final SettingsBrandingColorSchemeModel colourSchemeModel = (SettingsBrandingColorSchemeModel) response.getEntity();

        assertEquals(colourSchemeModel, bean);
    }

    @Test
    void testSetColourScheme() {
        final SettingsBrandingColorSchemeModel bean = SettingsBrandingColorSchemeModel.EXAMPLE_1;

        doReturn(bean).when(brandingService).setColourScheme(bean);

        final Response response = resource.setBrandingColorScheme(bean);
        assertEquals(200, response.getStatus());
        final SettingsBrandingColorSchemeModel colourSchemeModel = (SettingsBrandingColorSchemeModel) response.getEntity();

        assertEquals(colourSchemeModel, bean);
    }

    @Test
    void testGetLogo() {
        final InputStream stream = createDummyInputStream();

        doReturn(stream).when(brandingService).getLogo();

        final Response response = resource.getBrandingLogo();
        assertEquals(200, response.getStatus());
        final InputStream inputStream = (InputStream) response.getEntity();

        assertNotNull(inputStream);
    }

    @Test
    void testSetLogo() {
        final InputStream stream = createDummyInputStream();

        final Response response = resource.setBrandingLogo(stream);

        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetFavicon() {
        final InputStream stream = createDummyInputStream();

        doReturn(stream).when(brandingService).getFavicon();

        final Response response = resource.getBrandingFavicon();
        assertEquals(200, response.getStatus());
        final InputStream inputStream = (InputStream) response.getEntity();

        assertNotNull(inputStream);
    }

    @Test
    void testSetFavicon() {
        final InputStream stream = createDummyInputStream();

        final Response response = resource.setBrandingFavicon(stream);

        assertEquals(200, response.getStatus());
    }

    private InputStream createDummyInputStream() {
        return new ByteArrayInputStream("TEST-STREAM".getBytes());
    }
}
