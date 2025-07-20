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
        final ColourScheme cs = colourScheme;
        return SettingsBrandingColorSchemeModel.builder()
            .topBar(cs.get(ColourScheme.TOP_BAR))
            .topBarMenuItemText(cs.get(ColourScheme.TOP_BAR_MENU_ITEM_TEXT))
            .topBarMenuSelectedBackground(cs.get(ColourScheme.TOP_BAR_MENU_SELECTED_BACKGROUND))
            .topBarMenuSelectedText(cs.get(ColourScheme.TOP_BAR_MENU_SELECTED_TEXT))
            .topBarText(cs.get(ColourScheme.TOP_BAR_MENU_ITEM_TEXT))
            .headerButtonBackground(cs.get(ColourScheme.HEADER_BUTTON_BASE_BACKGROUND))
            .headerButtonText(cs.get(ColourScheme.HEADER_BUTTON_TEXT))
            .headingText(cs.get(ColourScheme.HEADING_TEXT))
            .menuItemSelectedBackground(cs.get(ColourScheme.MENU_ITEM_SELECTED_BACKGROUND))
            .menuItemSelectedText(cs.get(ColourScheme.MENU_ITEM_SELECTED_TEXT))
            .pageMenuItemText(cs.get(ColourScheme.MENU_ITEM_TEXT))
            // .pageMenuSelectedBackground(cs.get(ColourScheme.PAGE_MENU_SELECTED_BACKGROUND)) // REMOVED: constant does not exist
            .bordersAndDividers(cs.get(ColourScheme.BORDER))
            .searchFieldBackground(cs.get(ColourScheme.SEARCH_FIELD_BACKGROUND))
            .searchFieldText(cs.get(ColourScheme.SEARCH_FIELD_TEXT))
            .build();
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
        // setColorCode(colourScheme, ColourScheme.LINK, schemeModel.getLinks()); // REMOVED: not present in model or builder
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_BACKGROUND, schemeModel.getMenuItemSelectedBackground());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_TEXT, schemeModel.getMenuItemSelectedText());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_TEXT, schemeModel.getPageMenuItemText());
        // setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_BACKGROUND, schemeModel.getPageMenuSelectedBackground()); // REMOVED: field/method does not exist
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
