package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.plugins.lookandfeel.SiteLogo;
import com.atlassian.confluence.plugins.lookandfeel.SiteLogoManager;
import com.atlassian.confluence.themes.BaseColourScheme;
import com.atlassian.confluence.themes.ColourSchemeManager;
import com.atlassian.favicon.core.FaviconManager;
import com.atlassian.favicon.core.ImageType;
import com.atlassian.favicon.core.StoredFavicon;
import com.atlassian.favicon.core.exceptions.ImageStorageException;
import com.atlassian.favicon.core.exceptions.InvalidImageDataException;
import com.atlassian.favicon.core.exceptions.UnsupportedImageTypeException;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.deftdevs.bootstrapi.confluence.model.util.SettingsBrandingColorSchemeBeanUtil.toGlobalColorScheme;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingsBrandingServiceTest {

    @Mock
    private ColourSchemeManager colourSchemeManager;

    @Mock
    private FaviconManager faviconManager;

    @Mock
    private SiteLogoManager siteLogoManager;

    @Mock
    private SettingsBrandingServiceImpl settingsBrandingService;

    @BeforeEach
    public void setup() {
        settingsBrandingService = new SettingsBrandingServiceImpl(colourSchemeManager, siteLogoManager, faviconManager);
    }

    @Test
    void testGetColourScheme() {

        BaseColourScheme dummyBaseColourScheme = toGlobalColorScheme(SettingsBrandingColorSchemeBean.EXAMPLE_1, null);
        doReturn(dummyBaseColourScheme).when(colourSchemeManager).getGlobalColourScheme();

        SettingsBrandingColorSchemeBean colourScheme = settingsBrandingService.getColourScheme();

        assertEquals(SettingsBrandingColorSchemeBean.EXAMPLE_1.getTopBar(), colourScheme.getTopBar());
    }

    @Test
    void testSetColourScheme() {

        SettingsBrandingColorSchemeBean schemeBean = SettingsBrandingColorSchemeBean.EXAMPLE_1;
        BaseColourScheme dummyBaseColourScheme = toGlobalColorScheme(schemeBean, null);
        doReturn(dummyBaseColourScheme).when(colourSchemeManager).getGlobalColourScheme();

        SettingsBrandingColorSchemeBean colourScheme = settingsBrandingService.setColourScheme(schemeBean);
        verify(colourSchemeManager).saveGlobalColourScheme(any());

        assertEquals(schemeBean.getTopBar(), colourScheme.getTopBar());
    }

    @Test
    void testGetLogo() {

        InputStream is = new ByteArrayInputStream("".getBytes());
        SiteLogo siteLogo = new SiteLogo("", is);
        doReturn(siteLogo).when(siteLogoManager).getCurrent();

        InputStream logoImage = settingsBrandingService.getLogo();

        assertNotNull(logoImage);
    }

    @Test
    void testSetLogo() throws IOException {

        InputStream is = new ByteArrayInputStream("".getBytes());
        settingsBrandingService.setLogo(is);

        verify(siteLogoManager).uploadLogo(any(), any());
    }

    @Test
    void testGetFavicon() {

        InputStream is = new ByteArrayInputStream("".getBytes());
        StoredFavicon storedFavicon = new StoredFavicon(is, "img/png", 100);
        doReturn(true).when(faviconManager).isFaviconConfigured();
        doReturn(Optional.of(storedFavicon)).when(faviconManager).getFavicon(any(), any());

        InputStream favImage = settingsBrandingService.getFavicon();

        assertNotNull(favImage);
    }

    @Test
    void testSetFavicon() throws InvalidImageDataException, UnsupportedImageTypeException, ImageStorageException {

        InputStream is = new ByteArrayInputStream("".getBytes());

        try (MockedStatic<ImageType> imageTypeMockedStatic = mockStatic(ImageType.class)) {
            imageTypeMockedStatic.when(() -> ImageType.parseFromContentType("content/unknown")).thenReturn(Optional.of(ImageType.PNG));

            settingsBrandingService.setFavicon(is);
            verify(faviconManager).setFavicon(any());
        }
    }

    @Test
    void testSetFaviconNoParseableImageType() {
        final InputStream is = new ByteArrayInputStream("".getBytes());

        assertThrows(BadRequestException.class, () -> {
            settingsBrandingService.setFavicon(is);
        });
    }

}
