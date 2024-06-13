package de.aservo.confapi.confluence.model.util;

import com.atlassian.confluence.themes.BaseColourScheme;
import com.atlassian.confluence.themes.ColourScheme;
import de.aservo.confapi.commons.model.SettingsBrandingColorSchemeBean;

import javax.validation.constraints.NotNull;


public class SettingsBrandingColorSchemeBeanUtil {

    /**
     * Instantiates a new SettingsBrandingColourSchemeBean
     *
     * @param colourScheme the colour scheme
     */
    @NotNull
    public static SettingsBrandingColorSchemeBean toSettingsBrandingColorSchemeBean(
            @NotNull final ColourScheme colourScheme) {

        final SettingsBrandingColorSchemeBean schemeBean = new SettingsBrandingColorSchemeBean();
        schemeBean.setTopBar(colourScheme.get(ColourScheme.TOP_BAR));
        schemeBean.setTopBarMenuItemText(colourScheme.get(ColourScheme.TOP_BAR_MENU_ITEM_TEXT));
        schemeBean.setTopBarMenuSelectedBackground(colourScheme.get(ColourScheme.TOP_BAR_MENU_SELECTED_BACKGROUND));
        schemeBean.setTopBarMenuSelectedText(colourScheme.get(ColourScheme.TOP_BAR_MENU_SELECTED_TEXT));
        schemeBean.setTopBarText(colourScheme.get(ColourScheme.TOP_BAR_MENU_ITEM_TEXT));
        schemeBean.setBordersAndDividers(colourScheme.get(ColourScheme.BORDER));
        schemeBean.setHeaderButtonBackground(colourScheme.get(ColourScheme.HEADER_BUTTON_BASE_BACKGROUND));
        schemeBean.setHeaderButtonText(colourScheme.get(ColourScheme.HEADER_BUTTON_TEXT));
        schemeBean.setHeadingText(colourScheme.get(ColourScheme.HEADING_TEXT));
        schemeBean.setLinks(colourScheme.get(ColourScheme.LINK));
        schemeBean.setMenuItemSelectedBackground(colourScheme.get(ColourScheme.MENU_ITEM_SELECTED_BACKGROUND));
        schemeBean.setMenuItemSelectedText(colourScheme.get(ColourScheme.MENU_ITEM_SELECTED_TEXT));
        schemeBean.setPageMenuItemText(colourScheme.get(ColourScheme.MENU_ITEM_TEXT));
        schemeBean.setPageMenuSelectedBackground(colourScheme.get(ColourScheme.MENU_ITEM_SELECTED_BACKGROUND));
        schemeBean.setSearchFieldBackground(colourScheme.get(ColourScheme.SEARCH_FIELD_BACKGROUND));
        schemeBean.setSearchFieldText(colourScheme.get(ColourScheme.SEARCH_FIELD_TEXT));
        return schemeBean;
    }

    /**
     * Instantiates a new Confluence ColourScheme
     *
     * @param schemeBean the colour scheme bean
     * @param baseScheme optional - the initial base scheme to modify
     */
    @NotNull
    public static BaseColourScheme toGlobalColorScheme(
            @NotNull final SettingsBrandingColorSchemeBean schemeBean,
            final ColourScheme baseScheme) {

        final BaseColourScheme colourScheme = baseScheme == null ? new BaseColourScheme() : new BaseColourScheme(baseScheme);

        setColorCode(colourScheme, ColourScheme.TOP_BAR, schemeBean.getTopBar());
        setColorCode(colourScheme, ColourScheme.TOP_BAR_MENU_ITEM_TEXT, schemeBean.getTopBarMenuItemText());
        setColorCode(colourScheme, ColourScheme.TOP_BAR_MENU_SELECTED_BACKGROUND, schemeBean.getTopBarMenuSelectedBackground());
        setColorCode(colourScheme, ColourScheme.TOP_BAR_MENU_SELECTED_TEXT, schemeBean.getTopBarMenuSelectedText());
        setColorCode(colourScheme, ColourScheme.TOP_BAR_MENU_ITEM_TEXT, schemeBean.getTopBarText());
        setColorCode(colourScheme, ColourScheme.BORDER, schemeBean.getBordersAndDividers());
        setColorCode(colourScheme, ColourScheme.HEADER_BUTTON_BASE_BACKGROUND, schemeBean.getHeaderButtonBackground());
        setColorCode(colourScheme, ColourScheme.HEADER_BUTTON_TEXT, schemeBean.getHeaderButtonText());
        setColorCode(colourScheme, ColourScheme.HEADING_TEXT, schemeBean.getHeadingText());
        setColorCode(colourScheme, ColourScheme.LINK, schemeBean.getLinks());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_BACKGROUND, schemeBean.getMenuItemSelectedBackground());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_TEXT, schemeBean.getMenuItemSelectedText());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_TEXT, schemeBean.getPageMenuItemText());
        setColorCode(colourScheme, ColourScheme.MENU_ITEM_SELECTED_BACKGROUND, schemeBean.getPageMenuSelectedBackground());
        setColorCode(colourScheme, ColourScheme.SEARCH_FIELD_BACKGROUND, schemeBean.getSearchFieldBackground());
        setColorCode(colourScheme, ColourScheme.SEARCH_FIELD_TEXT, schemeBean.getSearchFieldText());

        return colourScheme;
    }

    private static void setColorCode(BaseColourScheme colorScheme, String key, String value) {
        if (value != null) {
            colorScheme.set(key, value);
        }
    }

    private SettingsBrandingColorSchemeBeanUtil() {
    }

}
