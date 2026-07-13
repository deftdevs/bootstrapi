package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.setup.settings.CustomHtmlSettings;
import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.setup.settings.Settings;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.confluence.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingCustomHtmlModel;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;

import java.io.InputStream;
import java.net.URI;

public class SettingsServiceImpl implements ConfluenceSettingsService {

    private final GlobalSettingsManager globalSettingsManager;
    private final SettingsBrandingService settingsBrandingService;

    public SettingsServiceImpl(
            final GlobalSettingsManager globalSettingsManager,
            final SettingsBrandingService settingsBrandingService) {

        this.globalSettingsManager = globalSettingsManager;
        this.settingsBrandingService = settingsBrandingService;
    }

    @Override
    public SettingsGeneralModel getSettingsGeneral() {
        final Settings settings = globalSettingsManager.getGlobalSettings();

        return SettingsGeneralModel.builder()
            .baseUrl(URI.create(settings.getBaseUrl()))
            .title(settings.getSiteTitle())
            .contactMessage(settings.getCustomContactMessage())
            .externalUserManagement(settings.isExternalUserManagement())
            .build();
    }

    @Override
    public SettingsGeneralModel setSettingsGeneral(SettingsGeneralModel settingsModel) {
        final Settings settings = globalSettingsManager.getGlobalSettings();

        if (settingsModel.getBaseUrl() != null) {
            settings.setBaseUrl(settingsModel.getBaseUrl().toString());
        }

        if (settingsModel.getTitle() != null) {
            settings.setSiteTitle(settingsModel.getTitle());
        }

        if (settingsModel.getContactMessage() != null) {
            settings.setCustomContactMessage(settingsModel.getContactMessage());
        }

        if (settingsModel.getExternalUserManagement() != null) {
            settings.setExternalUserManagement(settingsModel.getExternalUserManagement());
        }

        globalSettingsManager.updateGlobalSettings(settings);

        return getSettingsGeneral();
    }

    @Override
    public SettingsBrandingCustomHtmlModel getSettingsBrandingCustomHtml() {
        final CustomHtmlSettings customHtmlSettings = globalSettingsManager.getGlobalSettings().getCustomHtmlSettings();

        return SettingsBrandingCustomHtmlModel.builder()
                .beforeHeadEnd(customHtmlSettings.getBeforeHeadEnd())
                .afterBodyStart(customHtmlSettings.getAfterBodyStart())
                .beforeBodyEnd(customHtmlSettings.getBeforeBodyEnd())
                .build();
    }

    @Override
    public SettingsBrandingCustomHtmlModel setSettingsBrandingCustomHtml(
            final SettingsBrandingCustomHtmlModel settingsCustomHtmlModel) {

        final Settings settings = globalSettingsManager.getGlobalSettings();
        final CustomHtmlSettings customHtmlSettings = settings.getCustomHtmlSettings();

        if (settingsCustomHtmlModel.getBeforeHeadEnd() != null) {
            customHtmlSettings.setBeforeHeadEnd(settingsCustomHtmlModel.getBeforeHeadEnd());
        }

        if (settingsCustomHtmlModel.getAfterBodyStart() != null) {
            customHtmlSettings.setAfterBodyStart(settingsCustomHtmlModel.getAfterBodyStart());
        }

        if (settingsCustomHtmlModel.getBeforeBodyEnd() != null) {
            customHtmlSettings.setBeforeBodyEnd(settingsCustomHtmlModel.getBeforeBodyEnd());
        }

        globalSettingsManager.updateGlobalSettings(settings);

        return getSettingsBrandingCustomHtml();
    }

    @Override
    public SettingsSecurityModel getSettingsSecurity() {
        final Settings settings = globalSettingsManager.getGlobalSettings();

        return SettingsSecurityModel.builder()
                .webSudoEnabled(settings.getWebSudoEnabled())
                .webSudoTimeout(settings.getWebSudoTimeout())
                .build();
    }

    @Override
    public SettingsSecurityModel setSettingsSecurity(
            final SettingsSecurityModel settingsSecurityModel) {

        final Settings settings = globalSettingsManager.getGlobalSettings();

        if (settingsSecurityModel.getWebSudoEnabled() != null) {
            settings.setWebSudoEnabled(settingsSecurityModel.getWebSudoEnabled());
        }

        if (settingsSecurityModel.getWebSudoTimeout() != null) {
            settings.setWebSudoTimeout(settingsSecurityModel.getWebSudoTimeout());
        }

        globalSettingsManager.updateGlobalSettings(settings);

        return getSettingsSecurity();
    }

    @Override
    public SettingsBrandingColorSchemeModel getSettingsBrandingColorScheme() {
        return settingsBrandingService.getSettingsBrandingColorScheme();
    }

    @Override
    public SettingsBrandingColorSchemeModel setSettingsBrandingColorScheme(final SettingsBrandingColorSchemeModel colourSchemeModel) {
        return settingsBrandingService.setSettingsBrandingColorScheme(colourSchemeModel);
    }

    @Override
    public InputStream getSettingsBrandingLogo() {
        return settingsBrandingService.getSettingsBrandingLogo();
    }

    @Override
    public void setSettingsBrandingLogo(final InputStream inputStream) {
        settingsBrandingService.setSettingsBrandingLogo(inputStream);
    }

    @Override
    public InputStream getSettingsBrandingFavicon() {
        return settingsBrandingService.getSettingsBrandingFavicon();
    }

    @Override
    public void setSettingsBrandingFavicon(final InputStream inputStream) {
        settingsBrandingService.setSettingsBrandingFavicon(inputStream);
    }
}
