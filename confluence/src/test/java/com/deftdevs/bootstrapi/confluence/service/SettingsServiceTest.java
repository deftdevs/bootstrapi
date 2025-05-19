package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.setup.settings.CustomHtmlSettings;
import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.setup.settings.Settings;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingsServiceTest {

    @Mock
    private GlobalSettingsManager globalSettingsManager;

    private SettingsServiceImpl settingsService;

    @BeforeEach
    public void setup() {
        settingsService = new SettingsServiceImpl(globalSettingsManager);
    }

//    @Test
//    void testGetSettingsGeneral() {
//        final Settings settings = new DefaultTestSettings();
//
//        doReturn(settings).when(globalSettingsManager).getGlobalSettings();
//
//        final SettingsBean settingsBean = settingsService.getSettingsGeneral();
//
//        final SettingsBean settingsBeanRef = new SettingsBean();
//        settingsBeanRef.setBaseUrl(URI.create(settings.getBaseUrl()));
//        settingsBeanRef.setTitle(settings.getSiteTitle());
//        settingsBeanRef.setContactMessage(settings.getCustomContactMessage());
//        settingsBeanRef.setExternalUserManagement(settings.isExternalUserManagement());
//
//        assertEquals(settingsBeanRef, settingsBean);
//    }

//    @Test
//    void testSetSettingsGeneral() {
//        final Settings defaultSettings = new DefaultTestSettings();
//        doReturn(defaultSettings).when(globalSettingsManager).getGlobalSettings();
//
//        final Settings updateSettings = new OtherTestSettings();
//
//        final SettingsBean requestBean = new SettingsBean();
//        requestBean.setBaseUrl(URI.create(updateSettings.getBaseUrl()));
//        requestBean.setTitle(updateSettings.getSiteTitle());
//        requestBean.setContactMessage(updateSettings.getCustomContactMessage());
//        requestBean.setExternalUserManagement(updateSettings.isExternalUserManagement());
//
//        final SettingsBean responseBean = settingsService.setSettingsGeneral(requestBean);
//
//        final ArgumentCaptor<Settings> settingsCaptor = ArgumentCaptor.forClass(Settings.class);
//        verify(globalSettingsManager).updateGlobalSettings(settingsCaptor.capture());
//        final Settings settings = settingsCaptor.getValue();
//
//        final SettingsBean settingsBean = new SettingsBean();
//        settingsBean.setBaseUrl(URI.create(settings.getBaseUrl()));
//        settingsBean.setTitle(settings.getSiteTitle());
//        settingsBean.setContactMessage(settings.getCustomContactMessage());
//        settingsBean.setExternalUserManagement(settings.isExternalUserManagement());
//
//        assertEquals(requestBean, settingsBean);
//        assertEquals(requestBean, responseBean);
//    }
//
//    @Test
//    void testSetSettingsDefaultConfig(){
//        final SettingsBean settingsBean = new SettingsBean();
//
//        final Settings defaultSettings = new DefaultTestSettings();
//        doReturn(defaultSettings).when(globalSettingsManager).getGlobalSettings();
//
//        settingsService.setSettingsGeneral(settingsBean);
//
//        final ArgumentCaptor<Settings> settingsCaptor = ArgumentCaptor.forClass(Settings.class);
//        verify(globalSettingsManager).updateGlobalSettings(settingsCaptor.capture());
//        final Settings settings = settingsCaptor.getValue();
//
//        assertEquals(defaultSettings.getBaseUrl(), settings.getBaseUrl());
//        assertEquals(defaultSettings.getSiteTitle(), settings.getSiteTitle());
//        assertEquals(defaultSettings.getCustomContactMessage(), settings.getCustomContactMessage());
//        assertEquals(defaultSettings.isExternalUserManagement(), settings.isExternalUserManagement());
//    }

//    @Test
//    void testGetCustomHtml() {
//        final SettingsCustomHtmlBean customHtmlBean = SettingsCustomHtmlBean.EXAMPLE_1;
//        final CustomHtmlSettings customHtmlSettings = new CustomHtmlSettings(
//                customHtmlBean.getBeforeHeadEnd(),
//                customHtmlBean.getAfterBodyStart(),
//                customHtmlBean.getBeforeBodyEnd());
//        final Settings settings = new Settings();
//        settings.setCustomHtmlSettings(customHtmlSettings);
//        doReturn(settings).when(globalSettingsManager).getGlobalSettings();
//
//        final SettingsCustomHtmlBean result = settingsService.getCustomHtml();
//        assertEquals(customHtmlBean, result);
//    }

    @Test
    void testSetCustomHtml() {
        final Settings settings = mock(Settings.class);
        doReturn(new CustomHtmlSettings()).when(settings).getCustomHtmlSettings();
        doReturn(settings).when(globalSettingsManager).getGlobalSettings();

        final SettingsCustomHtmlModel customHtmlModel = SettingsCustomHtmlModel.EXAMPLE_1;
        final SettingsServiceImpl spy = spy(settingsService);
        doReturn(customHtmlModel).when(spy).getCustomHtml();

        final ArgumentCaptor<Settings> settingsArgumentCaptor = ArgumentCaptor.forClass(Settings.class);
        spy.setCustomHtml(customHtmlModel);
        verify(globalSettingsManager).updateGlobalSettings(settingsArgumentCaptor.capture());

        final Settings capuredSettings = settingsArgumentCaptor.getValue();
        assertEquals(customHtmlModel.getBeforeHeadEnd(), capuredSettings.getCustomHtmlSettings().getBeforeHeadEnd());
    }

    @Test
    void testGetSecurity() {
        final SettingsSecurityModel settingsSecurityModel = SettingsSecurityModel.EXAMPLE_1;
        final Settings settings = mock(Settings.class);
        doReturn(settingsSecurityModel.getWebSudoEnabled()).when(settings).getWebSudoEnabled();
        doReturn(settingsSecurityModel.getWebSudoTimeout()).when(settings).getWebSudoTimeout();
        doReturn(settings).when(globalSettingsManager).getGlobalSettings();

        final SettingsSecurityModel resultSettingsSecurityModel = settingsService.getSettingsSecurity();
        assertEquals(settingsSecurityModel, resultSettingsSecurityModel);
    }

    @Test
    void testSetSettingsSecurity() {
        final Settings settings = mock(Settings.class);
        doReturn(settings).when(globalSettingsManager).getGlobalSettings();

        final SettingsSecurityModel settingsSecurityModel = SettingsSecurityModel.EXAMPLE_1;
        final SettingsServiceImpl spy = spy(settingsService);
        doReturn(settingsSecurityModel).when(spy).getSettingsSecurity();

        spy.setSettingsSecurity(settingsSecurityModel);
        verify(settings).setWebSudoEnabled(settingsSecurityModel.getWebSudoEnabled());
        verify(settings).setWebSudoTimeout(settingsSecurityModel.getWebSudoTimeout());
    }

}
