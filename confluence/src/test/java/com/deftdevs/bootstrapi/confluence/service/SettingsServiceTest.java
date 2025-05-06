package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.settings.setup.DefaultTestSettings;
import com.atlassian.confluence.settings.setup.OtherTestSettings;
import com.atlassian.confluence.setup.settings.CustomHtmlSettings;
import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.setup.settings.Settings;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

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

    @Test
    void testGetSettingsGeneral() {
        final Settings settings = new DefaultTestSettings();

        doReturn(settings).when(globalSettingsManager).getGlobalSettings();

        final SettingsModel settingsModel = settingsService.getSettingsGeneral();

        final SettingsModel settingsModelRef = new SettingsModel();
        settingsModelRef.setBaseUrl(URI.create(settings.getBaseUrl()));
        settingsModelRef.setTitle(settings.getSiteTitle());
        settingsModelRef.setContactMessage(settings.getCustomContactMessage());
        settingsModelRef.setExternalUserManagement(settings.isExternalUserManagement());

        assertEquals(settingsModelRef, settingsModel);
    }

    @Test
    void testSetSettingsGeneral() {
        final Settings defaultSettings = new DefaultTestSettings();
        doReturn(defaultSettings).when(globalSettingsManager).getGlobalSettings();

        final Settings updateSettings = new OtherTestSettings();

        final SettingsModel requestModel = new SettingsModel();
        requestModel.setBaseUrl(URI.create(updateSettings.getBaseUrl()));
        requestModel.setTitle(updateSettings.getSiteTitle());
        requestModel.setContactMessage(updateSettings.getCustomContactMessage());
        requestModel.setExternalUserManagement(updateSettings.isExternalUserManagement());

        final SettingsModel responseModel = settingsService.setSettingsGeneral(requestModel);

        final ArgumentCaptor<Settings> settingsCaptor = ArgumentCaptor.forClass(Settings.class);
        verify(globalSettingsManager).updateGlobalSettings(settingsCaptor.capture());
        final Settings settings = settingsCaptor.getValue();

        final SettingsModel settingsModel = new SettingsModel();
        settingsModel.setBaseUrl(URI.create(settings.getBaseUrl()));
        settingsModel.setTitle(settings.getSiteTitle());
        settingsModel.setContactMessage(settings.getCustomContactMessage());
        settingsModel.setExternalUserManagement(settings.isExternalUserManagement());

        assertEquals(requestModel, settingsModel);
        assertEquals(requestModel, responseModel);
    }

    @Test
    void testSetSettingsDefaultConfig(){
        final SettingsModel settingsModel = new SettingsModel();

        final Settings defaultSettings = new DefaultTestSettings();
        doReturn(defaultSettings).when(globalSettingsManager).getGlobalSettings();

        settingsService.setSettingsGeneral(settingsModel);

        final ArgumentCaptor<Settings> settingsCaptor = ArgumentCaptor.forClass(Settings.class);
        verify(globalSettingsManager).updateGlobalSettings(settingsCaptor.capture());
        final Settings settings = settingsCaptor.getValue();

        assertEquals(defaultSettings.getBaseUrl(), settings.getBaseUrl());
        assertEquals(defaultSettings.getSiteTitle(), settings.getSiteTitle());
        assertEquals(defaultSettings.getCustomContactMessage(), settings.getCustomContactMessage());
        assertEquals(defaultSettings.isExternalUserManagement(), settings.isExternalUserManagement());
    }

    @Test
    void testGetCustomHtml() {
        final SettingsCustomHtmlModel customHtmlModel = SettingsCustomHtmlModel.EXAMPLE_1;
        final CustomHtmlSettings customHtmlSettings = new CustomHtmlSettings(
                customHtmlModel.getBeforeHeadEnd(),
                customHtmlModel.getAfterBodyStart(),
                customHtmlModel.getBeforeBodyEnd());
        final Settings settings = new Settings();
        settings.setCustomHtmlSettings(customHtmlSettings);
        doReturn(settings).when(globalSettingsManager).getGlobalSettings();

        final SettingsCustomHtmlModel result = settingsService.getCustomHtml();
        assertEquals(customHtmlModel, result);
    }

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
