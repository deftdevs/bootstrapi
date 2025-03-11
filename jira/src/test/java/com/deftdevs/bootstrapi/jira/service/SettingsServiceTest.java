package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.web.action.admin.EditAnnouncementBanner;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityBean;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static com.atlassian.jira.config.properties.APKeys.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingsServiceTest {

    private static final URI BASE_URL = URI.create("https://jira.atlassian.com");
    private static final String MODE_PUBLIC = "public";
    private static final String TITLE = "Atlassian Public JIRA";
    private static final String CONTACT_MESSAGE = "Test Contact Message";
    private static final String EXTERNAL_USER_MANAGEMENT = "true";

    @Mock
    private ApplicationProperties applicationProperties;

    private SettingsServiceImpl settingsService;

    @BeforeEach
    public void setup() {
        settingsService = new SettingsServiceImpl(applicationProperties);
    }

    @Test
    void testGetSettingsGeneral() {
        doReturn(BASE_URL.toString()).when(applicationProperties).getString(JIRA_BASEURL);
        doReturn(MODE_PUBLIC).when(applicationProperties).getString(JIRA_MODE);
        doReturn(TITLE).when(applicationProperties).getString(JIRA_TITLE);
        doReturn(CONTACT_MESSAGE).when(applicationProperties).getString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE);
        doReturn(EXTERNAL_USER_MANAGEMENT).when(applicationProperties).getString(JIRA_OPTION_USER_EXTERNALMGT);

        final SettingsBean settingsBean = settingsService.getSettingsGeneral();

        assertEquals(BASE_URL, settingsBean.getBaseUrl());
        assertEquals(MODE_PUBLIC, settingsBean.getMode());
        assertEquals(TITLE, settingsBean.getTitle());
        assertEquals(CONTACT_MESSAGE, settingsBean.getContactMessage());
        assertEquals(EXTERNAL_USER_MANAGEMENT, String.valueOf(settingsBean.getExternalUserManagement()));
    }

    @Test
    void testSetSettingsGeneral() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(BASE_URL);
        settingsBean.setMode(MODE_PUBLIC);
        settingsBean.setTitle(TITLE);
        settingsBean.setContactMessage(CONTACT_MESSAGE);
        settingsBean.setExternalUserManagement(Boolean.parseBoolean(EXTERNAL_USER_MANAGEMENT));

        settingsService.setSettingsGeneral(settingsBean);

        verify(applicationProperties).setString(JIRA_BASEURL, BASE_URL.toString());
        verify(applicationProperties).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties).setString(JIRA_TITLE, TITLE);
        verify(applicationProperties).setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, CONTACT_MESSAGE);
        verify(applicationProperties).setString(JIRA_OPTION_USER_EXTERNALMGT, EXTERNAL_USER_MANAGEMENT);
    }

    @Test
    void testSetSettingsGeneralEmptyBean() {
        final SettingsBean settingsBean = new SettingsBean();

        settingsService.setSettingsGeneral(settingsBean);

        verify(applicationProperties, never()).setString(JIRA_BASEURL, BASE_URL.toString());
        verify(applicationProperties, never()).setString(JIRA_MODE, MODE_PUBLIC);
        verify(applicationProperties, never()).setString(JIRA_TITLE, TITLE);
        verify(applicationProperties, never()).setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, CONTACT_MESSAGE);
    }

    @Test
    void testGetSecurity() {
        final SettingsSecurityBean settingsSecurityBean = SettingsSecurityBean.EXAMPLE_1;
        doReturn(!settingsSecurityBean.getWebSudoEnabled()).when(applicationProperties).getOption(WebSudo.IS_DISABLED);
        doReturn(String.valueOf(settingsSecurityBean.getWebSudoTimeout())).when(applicationProperties).getDefaultBackedString(WebSudo.TIMEOUT);

        final SettingsSecurityBean resultSettingsSecurityBean = settingsService.getSettingsSecurity();
        assertEquals(settingsSecurityBean.getWebSudoEnabled(), resultSettingsSecurityBean.getWebSudoEnabled());
        assertEquals(settingsSecurityBean.getWebSudoTimeout(), resultSettingsSecurityBean.getWebSudoTimeout());
    }

    @Test
    void testSetSettingsSecurity() {
        final SettingsSecurityBean settingsSecurityBean = SettingsSecurityBean.EXAMPLE_1;
        final SettingsServiceImpl spy = spy(settingsService);
        doReturn(settingsSecurityBean).when(spy).getSettingsSecurity();

        spy.setSettingsSecurity(settingsSecurityBean);
        verify(applicationProperties).setOption(WebSudo.IS_DISABLED, !settingsSecurityBean.getWebSudoEnabled());
        verify(applicationProperties).setString(WebSudo.TIMEOUT, String.valueOf(settingsSecurityBean.getWebSudoTimeout()));
    }

    @Test
    void testSetSettingsGeneralUnsupportedMode() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setMode("unsupported");

        assertThrows(BadRequestException.class, () -> {
            settingsService.setSettingsGeneral(settingsBean);
        });
    }

    @Test
    void testSetSettingsGeneralInvalidCombination() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setMode(MODE_PUBLIC);
        doReturn(true).when(applicationProperties).getOption(JIRA_OPTION_USER_EXTERNALMGT);

        assertThrows(BadRequestException.class, () -> {
            settingsService.setSettingsGeneral(settingsBean);
        });
    }

    @Test
    void testGetBanner() {
        final String content = "Banner!";
        final String visibility = EditAnnouncementBanner.PUBLIC_BANNER;
        doReturn(content).when(applicationProperties).getDefaultBackedText(JIRA_ALERT_HEADER);
        doReturn(visibility).when(applicationProperties).getDefaultBackedString(JIRA_ALERT_HEADER_VISIBILITY);

        final SettingsBannerBean bannerBean = settingsService.getSettingsBanner();
        assertEquals(content, bannerBean.getContent());
        assertEquals(visibility, bannerBean.getVisibility().name().toLowerCase());
    }

    @Test
    void testSetBanner() {
        final SettingsBannerBean settingsBannerBean = SettingsBannerBean.builder()
                .content("Hello...")
                .visibility(SettingsBannerBean.Visibility.valueOf(EditAnnouncementBanner.PRIVATE_BANNER.toUpperCase()))
                .build();

        final SettingsServiceImpl spy = spy(settingsService);
        doReturn(settingsBannerBean).when(spy).getSettingsBanner();

        spy.setSettingsBanner(settingsBannerBean);
        verify(applicationProperties).setText(JIRA_ALERT_HEADER, settingsBannerBean.getContent());
        verify(applicationProperties).setString(JIRA_ALERT_HEADER_VISIBILITY, settingsBannerBean.getVisibility().name().toLowerCase());
        verify(spy).getSettingsBanner();
    }

}
