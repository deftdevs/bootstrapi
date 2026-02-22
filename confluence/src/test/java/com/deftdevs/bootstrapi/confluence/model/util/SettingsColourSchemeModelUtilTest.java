package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.confluence.themes.BaseColourScheme;
import com.atlassian.confluence.themes.ColourScheme;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.deftdevs.bootstrapi.confluence.model.util.SettingsBrandingColorSchemeModelUtil.toGlobalColorScheme;
import static com.deftdevs.bootstrapi.confluence.model.util.SettingsBrandingColorSchemeModelUtil.toSettingsBrandingColorSchemeModel;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SettingsColourSchemeModelUtilTest {

    @Test
    void testToGlobalColorScheme() {

        SettingsBrandingColorSchemeModel schemeModel = SettingsBrandingColorSchemeModel.EXAMPLE_1;
        BaseColourScheme baseColourScheme = toGlobalColorScheme(schemeModel, null);

        assertNotNull(baseColourScheme);
        assertEquals(schemeModel.getTopBar(), baseColourScheme.get(ColourScheme.TOP_BAR));
        assertEquals(schemeModel.getHeaderButtonText(), baseColourScheme.get(ColourScheme.HEADER_BUTTON_TEXT));
        assertEquals(schemeModel.getBordersAndDividers(), baseColourScheme.get(ColourScheme.BORDER));
    }

    @Test
    void testToGlobalColorSchemeSetNullValues() {

        SettingsBrandingColorSchemeModel schemeModel = SettingsBrandingColorSchemeModel.builder().build();
        BaseColourScheme baseColourScheme = toGlobalColorScheme(schemeModel, null);

        assertNotNull(baseColourScheme);
        assertNull(baseColourScheme.get(ColourScheme.TOP_BAR));
        assertNull(baseColourScheme.get(ColourScheme.HEADER_BUTTON_TEXT));
        assertNull(baseColourScheme.get(ColourScheme.BORDER));
    }

    @Test
    void testToSettingsBrandingColorSchemeModel() {

        final BaseColourScheme colourScheme = getDummyBaseColourScheme();

        SettingsBrandingColorSchemeModel schemeModel = toSettingsBrandingColorSchemeModel(colourScheme);

        assertNotNull(schemeModel);
        assertEquals(colourScheme.get(ColourScheme.TOP_BAR), schemeModel.getTopBar());
        assertEquals(colourScheme.get(ColourScheme.HEADER_BUTTON_TEXT), schemeModel.getHeaderButtonText());
        assertEquals(colourScheme.get(ColourScheme.BORDER), schemeModel.getBordersAndDividers());
    }

    public static BaseColourScheme getDummyBaseColourScheme() {
        final BaseColourScheme colourScheme = new BaseColourScheme();
        colourScheme.set(ColourScheme.TOP_BAR, "#0049B0");
        colourScheme.set(ColourScheme.HEADER_BUTTON_TEXT, "#0049B0");
        colourScheme.set(ColourScheme.BORDER, "#0049B0");
        return colourScheme;
    }
}
