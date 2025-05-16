package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.setup.settings.CustomHtmlSettings;
import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.setup.settings.Settings;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;

import java.net.URI;

public class SettingsServiceImpl implements ConfluenceSettingsService {

    private final GlobalSettingsManager globalSettingsManager;

    public SettingsServiceImpl(
            final GlobalSettingsManager globalSettingsManager) {

        this.globalSettingsManager = globalSettingsManager;
    }

    @Override
    public SettingsModel getSettingsGeneral() {
        final Settings settings = globalSettingsManager.getGlobalSettings();

        final SettingsModel settingsModel = new SettingsModel();
        settingsModel.setBaseUrl(URI.create(settings.getBaseUrl()));
        settingsModel.setTitle(settings.getSiteTitle());
        settingsModel.setContactMessage(settings.getCustomContactMessage());
        settingsModel.setExternalUserManagement(settings.isExternalUserManagement());

        return settingsModel;
    }

    @Override
    public SettingsModel setSettingsGeneral(SettingsModel settingsModel) {
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
    public SettingsCustomHtmlModel getCustomHtml() {
        final CustomHtmlSettings customHtmlSettings = globalSettingsManager.getGlobalSettings().getCustomHtmlSettings();

        return SettingsCustomHtmlModel.builder()
                .beforeHeadEnd(customHtmlSettings.getBeforeHeadEnd())
                .afterBodyStart(customHtmlSettings.getAfterBodyStart())
                .beforeBodyEnd(customHtmlSettings.getBeforeBodyEnd())
                .build();
    }

    @Override
    public SettingsCustomHtmlModel setCustomHtml(
            final SettingsCustomHtmlModel settingsCustomHtmlModel) {

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

        return getCustomHtml();
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

        return getSettingsSecurity();
    }

}
