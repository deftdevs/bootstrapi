package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.plugins.lookandfeel.SiteLogo;
import com.atlassian.confluence.plugins.lookandfeel.SiteLogoManager;
import com.atlassian.confluence.themes.BaseColourScheme;
import com.atlassian.confluence.themes.ColourSchemeManager;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.deftdevs.bootstrapi.confluence.model.util.SettingsBrandingColorSchemeModelUtil.toGlobalColorScheme;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingsBrandingServiceTest {

    @Mock
    private ColourSchemeManager colourSchemeManager;

    @Mock
    private SiteLogoManager siteLogoManager;

    @Mock
    private SettingsBrandingServiceImpl settingsBrandingService;

    @BeforeEach
    public void setup() {
        settingsBrandingService = new SettingsBrandingServiceImpl(colourSchemeManager, siteLogoManager);
    }

    @Test
    void testGetColourScheme() {

        BaseColourScheme dummyBaseColourScheme = toGlobalColorScheme(SettingsBrandingColorSchemeModel.EXAMPLE_1, null);
        doReturn(dummyBaseColourScheme).when(colourSchemeManager).getGlobalColourScheme();

        SettingsBrandingColorSchemeModel colourScheme = settingsBrandingService.getColourScheme();

        assertEquals(SettingsBrandingColorSchemeModel.EXAMPLE_1.getTopBar(), colourScheme.getTopBar());
    }

    @Test
    void testSetColourScheme() {

        SettingsBrandingColorSchemeModel schemeModel = SettingsBrandingColorSchemeModel.EXAMPLE_1;
        BaseColourScheme dummyBaseColourScheme = toGlobalColorScheme(schemeModel, null);
        doReturn(dummyBaseColourScheme).when(colourSchemeManager).getGlobalColourScheme();

        SettingsBrandingColorSchemeModel colourScheme = settingsBrandingService.setColourScheme(schemeModel);
        verify(colourSchemeManager).saveGlobalColourScheme(any());

        assertEquals(schemeModel.getTopBar(), colourScheme.getTopBar());
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

}
