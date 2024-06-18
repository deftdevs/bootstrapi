package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeBean;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SettingsBrandingColorSchemeBeanUtil {

    /**
     * Instantiates a new SettingsBrandingColourSchemeBean
     *
     * @param applicationProperties the application properties holding the colour scheme
     */
    @NotNull
    public static SettingsBrandingColorSchemeBean getSettingsBrandingColorSchemeBean(
            @NotNull final ApplicationProperties applicationProperties) {

        final SettingsBrandingColorSchemeBean schemeBean = new SettingsBrandingColorSchemeBean();
        Map<String, Object> propertyMap = applicationProperties.asMap();

        schemeBean.setTopBar(propertyMap.get(APKeys.JIRA_LF_TOP_BGCOLOUR).toString());
        schemeBean.setTopBarMenuItemText(propertyMap.get(APKeys.JIRA_LF_MENU_TEXTCOLOUR).toString());
        schemeBean.setTopBarMenuSelectedBackground(propertyMap.get(APKeys.JIRA_LF_TOP_HIGHLIGHTCOLOR).toString());
        schemeBean.setTopBarMenuSelectedText(propertyMap.get(APKeys.JIRA_LF_TOP_TEXTHIGHLIGHTCOLOR).toString());
        schemeBean.setTopBarText(propertyMap.get(APKeys.JIRA_LF_TOP_TEXTCOLOUR).toString());
        schemeBean.setBordersAndDividers(propertyMap.get(APKeys.JIRA_LF_TOP_SEPARATOR_BGCOLOR).toString());
        schemeBean.setHeaderButtonBackground(propertyMap.get(APKeys.JIRA_LF_HERO_BUTTON_BASEBGCOLOUR).toString());
        schemeBean.setHeaderButtonText(propertyMap.get(APKeys.JIRA_LF_HERO_BUTTON_TEXTCOLOUR).toString());
        schemeBean.setHeadingText(propertyMap.get(APKeys.JIRA_LF_TEXT_HEADINGCOLOUR).toString());
        schemeBean.setLinks(propertyMap.get(APKeys.JIRA_LF_TEXT_LINKCOLOUR).toString());
        schemeBean.setMenuItemSelectedBackground(propertyMap.get(APKeys.JIRA_LF_MENU_BGCOLOUR).toString());
        schemeBean.setMenuItemSelectedText(propertyMap.get(APKeys.JIRA_LF_MENU_TEXTCOLOUR).toString());

        return schemeBean;
    }

    /**
     * Instantiates a new Confluence ColourScheme
     *
     * @param schemeBean the colour scheme bean
     * @param setNullValues whether or not to set null value when provided via bean
     * @param applicationProperties the application properties holding the colour scheme
     */
    @NotNull
    public static void setGlobalColorScheme(
            @NotNull final SettingsBrandingColorSchemeBean schemeBean,
            boolean setNullValues,
            final ApplicationProperties applicationProperties) {

        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_BGCOLOUR, schemeBean.getTopBar(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_MENU_TEXTCOLOUR, schemeBean.getTopBarMenuItemText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_HIGHLIGHTCOLOR, schemeBean.getTopBarMenuSelectedBackground(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_TEXTHIGHLIGHTCOLOR, schemeBean.getTopBarMenuSelectedText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_TEXTCOLOUR, schemeBean.getTopBarText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TOP_SEPARATOR_BGCOLOR, schemeBean.getBordersAndDividers(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_HERO_BUTTON_BASEBGCOLOUR, schemeBean.getHeaderButtonBackground(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_HERO_BUTTON_TEXTCOLOUR, schemeBean.getHeaderButtonText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TEXT_HEADINGCOLOUR, schemeBean.getHeadingText(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_TEXT_LINKCOLOUR, schemeBean.getLinks(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_MENU_BGCOLOUR, schemeBean.getMenuItemSelectedBackground(), setNullValues);
        setColorCode(applicationProperties, APKeys.JIRA_LF_MENU_TEXTCOLOUR, schemeBean.getMenuItemSelectedText(), setNullValues);
    }

    private static void setColorCode(ApplicationProperties applicationProperties, String key, String value, final boolean setNullValues) {
        if (setNullValues || !isBlank(value)) {
            applicationProperties.setString(key, value);
        }
    }

    private SettingsBrandingColorSchemeBeanUtil() {
    }

}
