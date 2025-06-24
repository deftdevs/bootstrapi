package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SettingsBrandingColorSchemeModelUtil {

    /**
     * Instantiates a new SettingsBrandingColourSchemeModel
     *
     * @param applicationProperties the application properties holding the colour scheme
     */
    @NotNull
    public static SettingsBrandingColorSchemeModel getSettingsBrandingColorSchemeModel(
            final ApplicationProperties applicationProperties) {

        final SettingsBrandingColorSchemeModel schemeModel = new SettingsBrandingColorSchemeModel();
        Map<String, Object> propertyMap = applicationProperties.asMap();

        schemeModel.setTopBar(propertyMap.get(APKeys.JIRA_LF_TOP_BGCOLOUR).toString());
        schemeModel.setTopBarMenuItemText(propertyMap.get(APKeys.JIRA_LF_MENU_TEXTCOLOUR).toString());
        schemeModel.setTopBarMenuSelectedBackground(propertyMap.get(APKeys.JIRA_LF_TOP_HIGHLIGHTCOLOR).toString());
        schemeModel.setTopBarMenuSelectedText(propertyMap.get(APKeys.JIRA_LF_TOP_TEXTHIGHLIGHTCOLOR).toString());
        schemeModel.setTopBarText(propertyMap.get(APKeys.JIRA_LF_TOP_TEXTCOLOUR).toString());
        schemeModel.setBordersAndDividers(propertyMap.get(APKeys.JIRA_LF_TOP_SEPARATOR_BGCOLOR).toString());
        schemeModel.setHeaderButtonBackground(propertyMap.get(APKeys.JIRA_LF_HERO_BUTTON_BASEBGCOLOUR).toString());
        schemeModel.setHeaderButtonText(propertyMap.get(APKeys.JIRA_LF_HERO_BUTTON_TEXTCOLOUR).toString());
        schemeModel.setHeadingText(propertyMap.get(APKeys.JIRA_LF_TEXT_HEADINGCOLOUR).toString());
        schemeModel.setLinks(propertyMap.get(APKeys.JIRA_LF_TEXT_LINKCOLOUR).toString());
        schemeModel.setMenuItemSelectedBackground(propertyMap.get(APKeys.JIRA_LF_MENU_BGCOLOUR).toString());
        schemeModel.setMenuItemSelectedText(propertyMap.get(APKeys.JIRA_LF_MENU_TEXTCOLOUR).toString());

        return schemeModel;
    }

    /**
     * Instantiates a new Confluence ColourScheme
     *
     * @param schemeModel the colour scheme bean
     * @param setNullValues whether or not to set null value when provided via bean
     * @param applicationProperties the application properties holding the colour scheme
     */
    @NotNull
    public static void setGlobalColorScheme(
            final SettingsBrandingColorSchemeModel schemeModel,
            boolean setNullValues,
            final ApplicationProperties applicationProperties) {

        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_BGCOLOUR, schemeModel.getTopBar(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_MENU_TEXTCOLOUR, schemeModel.getTopBarMenuItemText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_HIGHLIGHTCOLOR, schemeModel.getTopBarMenuSelectedBackground(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_TEXTHIGHLIGHTCOLOR, schemeModel.getTopBarMenuSelectedText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_TEXTCOLOUR, schemeModel.getTopBarText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_SEPARATOR_BGCOLOR, schemeModel.getBordersAndDividers(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_HERO_BUTTON_BASEBGCOLOUR, schemeModel.getHeaderButtonBackground(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_HERO_BUTTON_TEXTCOLOUR, schemeModel.getHeaderButtonText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TEXT_HEADINGCOLOUR, schemeModel.getHeadingText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TEXT_LINKCOLOUR, schemeModel.getLinks(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_MENU_BGCOLOUR, schemeModel.getMenuItemSelectedBackground(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_MENU_TEXTCOLOUR, schemeModel.getMenuItemSelectedText(), setNullValues);
    }

    private static void setColorCode(ApplicationProperties applicationProperties, String key, String value, final boolean setNullValues) {
        if (setNullValues || !isBlank(value)) {
            applicationProperties.setString(key, value);
        }
    }

    private SettingsBrandingColorSchemeModelUtil() {
    }

}
