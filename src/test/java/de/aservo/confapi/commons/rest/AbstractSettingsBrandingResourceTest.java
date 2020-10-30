package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.SettingsBrandingColourSchemeBean;
import de.aservo.confapi.commons.service.api.BrandingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class AbstractSettingsBrandingResourceTest {

    @Mock
    private BrandingService brandingService;

    private TestSettingsBrandingResourceImpl resource;

    @Before
    public void setup() {
        resource = new TestSettingsBrandingResourceImpl(brandingService);
    }

    @Test
    public void testGetColourScheme() {
        final SettingsBrandingColourSchemeBean bean = SettingsBrandingColourSchemeBean.EXAMPLE_1;

        doReturn(bean).when(brandingService).getColourScheme();

        final Response response = resource.getColourScheme();
        assertEquals(200, response.getStatus());
        final SettingsBrandingColourSchemeBean colourSchemeBean = (SettingsBrandingColourSchemeBean) response.getEntity();

        assertEquals(colourSchemeBean, bean);
    }

    @Test
    public void testSetColourScheme() {
        final SettingsBrandingColourSchemeBean bean = SettingsBrandingColourSchemeBean.EXAMPLE_1;

        doReturn(bean).when(brandingService).setColourScheme(bean);

        final Response response = resource.setColourScheme(bean);
        assertEquals(200, response.getStatus());
        final SettingsBrandingColourSchemeBean colourSchemeBean = (SettingsBrandingColourSchemeBean) response.getEntity();

        assertEquals(colourSchemeBean, bean);
    }

    @Test
    public void testGetLogo() {
        final InputStream stream = createDummyInputStream();

        doReturn(stream).when(brandingService).getLogo();

        final Response response = resource.getLogo();
        assertEquals(200, response.getStatus());
        final InputStream inputStream = (InputStream) response.getEntity();

        assertNotNull(inputStream);
    }

    @Test
    public void testSetLogo() {
        final InputStream stream = createDummyInputStream();

        final Response response = resource.setLogo(stream);

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGetFavicon() {
        final InputStream stream = createDummyInputStream();

        doReturn(stream).when(brandingService).getFavicon();

        final Response response = resource.getFavicon();
        assertEquals(200, response.getStatus());
        final InputStream inputStream = (InputStream) response.getEntity();

        assertNotNull(inputStream);
    }

    @Test
    public void testSetFavicon() {
        final InputStream stream = createDummyInputStream();

        final Response response = resource.setFavicon(stream);

        assertEquals(200, response.getStatus());
    }

    private InputStream createDummyInputStream() {
        return new ByteArrayInputStream("TEST-STREAM".getBytes());
    }
}
