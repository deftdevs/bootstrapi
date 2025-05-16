package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SettingsColourSchemeModelUtilTest {

    @Mock
    private ApplicationProperties applicationProperties;

    @Test
    void testToGlobalColorScheme() {

        SettingsBrandingColorSchemeModel schemeModel = SettingsBrandingColorSchemeModel.EXAMPLE_1;
        SettingsBrandingColorSchemeModelUtil.setGlobalColorScheme(schemeModel, true, applicationProperties);

        verify(applicationProperties).setString(APKeys.JIRA_LF_TOP_BGCOLOUR, schemeModel.getTopBar());
        verify(applicationProperties).setString(APKeys.JIRA_LF_HERO_BUTTON_TEXTCOLOUR, schemeModel.getHeaderButtonText());
        verify(applicationProperties).setString(APKeys.JIRA_LF_TOP_SEPARATOR_BGCOLOR, schemeModel.getBordersAndDividers());
    }

    @Test
    void testToSettingsBrandingColorSchemeModel() {

        Map<String, Object> dummyBaseColourScheme = getDummyBaseColourScheme();
        doReturn(dummyBaseColourScheme).when(applicationProperties).asMap();

        SettingsBrandingColorSchemeModel schemeModel = SettingsBrandingColorSchemeModelUtil.getSettingsBrandingColorSchemeModel(applicationProperties);

        assertNotNull(schemeModel);
        assertEquals(dummyBaseColourScheme.get(APKeys.JIRA_LF_TOP_BGCOLOUR), schemeModel.getTopBar());
        assertEquals(dummyBaseColourScheme.get(APKeys.JIRA_LF_HERO_BUTTON_TEXTCOLOUR), schemeModel.getHeaderButtonText());
        assertEquals(dummyBaseColourScheme.get(APKeys.JIRA_LF_TOP_SEPARATOR_BGCOLOR), schemeModel.getBordersAndDividers());
    }

    public static Map<String, Object> getDummyBaseColourScheme() {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(APKeys.JIRA_LF_TOP_BGCOLOUR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_MENU_TEXTCOLOUR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_TOP_HIGHLIGHTCOLOR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_TOP_TEXTHIGHLIGHTCOLOR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_TOP_TEXTCOLOUR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_TOP_SEPARATOR_BGCOLOR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_HERO_BUTTON_BASEBGCOLOUR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_HERO_BUTTON_TEXTCOLOUR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_TEXT_HEADINGCOLOUR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_TEXT_LINKCOLOUR, "#FFFFFF");
        propertyMap.put(APKeys.JIRA_LF_MENU_BGCOLOUR, "#FFFFFF");
        return propertyMap;
    }
}
