package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.confluence.themes.BaseColourScheme;
import com.atlassian.confluence.themes.ColourScheme;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;


public class SettingsBrandingColorSchemeModelUtil {

    /**
     * Instantiates a new SettingsBrandingColourSchemeModel
     *
     * @param colourScheme the colour scheme
     */
    public static SettingsBrandingColorSchemeModel toSettingsBrandingColorSchemeModel(
            final ColourScheme colourScheme) {

        final SettingsBrandingColorSchemeModel schemeModel = new SettingsBrandingColorSchemeModel();
        schemeModel.setTopBar(colourScheme.get(ColourScheme.TOP_BAR));
        schemeModel.setTopBarMenuItemText(colourScheme.get(ColourScheme.TOP_BAR_MENU_ITEM_TEXT));
        schemeModel.setTopBarMenuSelectedBackground(colourScheme.get(ColourScheme.TOP_BAR_MENU_SELECTED_BACKGROUND));
        schemeModel.setTopBarMenuSelectedText(colourScheme.get(ColourScheme.TOP_BAR_MENU_SELECTED_TEXT));
        schemeModel.setTopBarText(colourScheme.get(ColourScheme.TOP_BAR_MENU_ITEM_TEXT));
        schemeModel.setBordersAndDividers(colourScheme.get(ColourScheme.BORDER));
        schemeModel.setHeaderButtonBackground(colourScheme.get(ColourScheme.HEADER_BUTTON_BASE_BACKGROUND));
        schemeModel.setHeaderButtonText(colourScheme.get(ColourScheme.HEADER_BUTTON_TEXT));
        schemeModel.setHeadingText(colourScheme.get(ColourScheme.HEADING_TEXT));
        schemeModel.setLinks(colourScheme.get(ColourScheme.LINK));
        schemeModel.setMenuItemSelectedBackground(colourScheme.get(ColourScheme.MENU_ITEM_SELECTED_BACKGROUND));
        schemeModel.setMenuItemSelectedText(colourScheme.get(ColourScheme.MENU_ITEM_SELECTED_TEXT));
        schemeModel.setPageMenuItemText(colourScheme.get(ColourScheme.MENU_ITEM_TEXT));
        schemeModel.setPageMenuSelectedBackground(colourScheme.get(ColourScheme.MENU_ITEM_SELECTED_BACKGROUND));
        schemeModel.setSearchFieldBackground(colourScheme.get(ColourScheme.SEARCH_FIELD_BACKGROUND));
        schemeModel.setSearchFieldText(colourScheme.get(ColourScheme.SEARCH_FIELD_TEXT));
        return schemeModel;
    }

    /**
     * Instantiates a new Confluence ColourScheme
     *
     * @param schemeModel the colour scheme bean
     * @param baseScheme optional - the initial base scheme to modify
     */
    public static BaseColourScheme toGlobalColorScheme(
            final SettingsBrandingColorSchemeModel schemeModel,
            final ColourScheme baseScheme) {

        final BaseColourScheme colourScheme = baseScheme == null ? new BaseColourScheme() : new BaseColourScheme(baseScheme);

        setColorCode(colourScheme, ColourScheme.TOP_BAR, schemeModel.getTopBar());
        setColorCode(colourScheme, ColourScheme.TOP_BAR_MENU_ITEM_TEXT, schemeModel.getTopBarMenuItemText());
        setColorCode(colourScheme, ColourScheme.TOP_BAR_MENU_SELECTED_BACKGROUND, schemeModel.getTopBarMenuSelectedBackground());
        setColorCode(colourScheme, ColourScheme.TOP_BAR_MENU_SELECTED_TEXT, schemeModel.getTopBarMenuSelectedText());
        setColorCode(colourScheme, ColourScheme.TOP_BAR_MENU_ITEM_TEXT, schemeModel.getTopBarText());
        setColorCode(colourScheme, ColourScheme.BORDER, schemeModel.getBordersAndDividers());
        setColorCode(colourScheme, ColourScheme.HEADER_BUTTON_BASE_BACKGROUND, schemeModel.getHeaderButtonBackground());
        setColorCode(colourScheme, ColourScheme.HEADER_BUTTON_TEXT, schemeModel.getHeaderButtonText());
        setColorCode(colourScheme, ColourScheme.HEADING_TEXT, schemeModel.getHeadingText());
        setColorCode(colourScheme, ColourScheme.LINK, schemeModel.getLinks());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_BACKGROUND, schemeModel.getMenuItemSelectedBackground());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_TEXT, schemeModel.getMenuItemSelectedText());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_TEXT, schemeModel.getPageMenuItemText());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_BACKGROUND, schemeModel.getPageMenuSelectedBackground());
        setColorCode(colourScheme, ColourScheme.SEARCH_FIELD_BACKGROUND, schemeModel.getSearchFieldBackground());
        setColorCode(colourScheme, ColourScheme.SEARCH_FIELD_TEXT, schemeModel.getSearchFieldText());

        return colourScheme;
    }

    private static void setColorCode(BaseColourScheme colorScheme, String key, String value) {
        if (value != null) {
            colorScheme.set(key, value);
        }
    }

    private SettingsBrandingColorSchemeModelUtil() {
    }

}
