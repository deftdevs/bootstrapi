package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.setup.settings.CustomHtmlSettings;
import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.setup.settings.Settings;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlBean;
import com.deftdevs.bootstrapi.confluence.model.SettingsSecurityBean;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.URI;

@Component
@ExportAsService({SettingsService.class, ConfluenceSettingsService.class})
public class SettingsServiceImpl implements ConfluenceSettingsService {

    @ComponentImport
    private final GlobalSettingsManager globalSettingsManager;

    @Inject
    public SettingsServiceImpl(
            final GlobalSettingsManager globalSettingsManager) {

        this.globalSettingsManager = globalSettingsManager;
    }

    @Override
    public SettingsBean getSettings() {
        final Settings settings = globalSettingsManager.getGlobalSettings();

        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(URI.create(settings.getBaseUrl()));
        settingsBean.setTitle(settings.getSiteTitle());
        settingsBean.setContactMessage(settings.getCustomContactMessage());
        settingsBean.setExternalUserManagement(settings.isExternalUserManagement());

        return settingsBean;
    }

    @Override
    public SettingsBean setSettings(SettingsBean settingsBean) {
        final Settings settings = globalSettingsManager.getGlobalSettings();

        if (settingsBean.getBaseUrl() != null) {
            settings.setBaseUrl(settingsBean.getBaseUrl().toString());
        }

        if (settingsBean.getTitle() != null) {
            settings.setSiteTitle(settingsBean.getTitle());
        }

        if (settingsBean.getContactMessage() != null) {
            settings.setCustomContactMessage(settingsBean.getContactMessage());
        }

        if (settingsBean.getExternalUserManagement() != null) {
            settings.setExternalUserManagement(settingsBean.getExternalUserManagement());
        }

        globalSettingsManager.updateGlobalSettings(settings);

        return getSettings();
    }

    @Override
    public SettingsCustomHtmlBean getCustomHtml() {
        final CustomHtmlSettings customHtmlSettings = globalSettingsManager.getGlobalSettings().getCustomHtmlSettings();

        return SettingsCustomHtmlBean.builder()
                .beforeHeadEnd(customHtmlSettings.getBeforeHeadEnd())
                .afterBodyStart(customHtmlSettings.getAfterBodyStart())
                .beforeBodyEnd(customHtmlSettings.getBeforeBodyEnd())
                .build();
    }

    @Override
    public SettingsCustomHtmlBean setCustomHtml(
            final SettingsCustomHtmlBean settingsCustomHtmlBean) {

        final Settings settings = globalSettingsManager.getGlobalSettings();
        final CustomHtmlSettings customHtmlSettings = settings.getCustomHtmlSettings();

        if (settingsCustomHtmlBean.getBeforeHeadEnd() != null) {
            customHtmlSettings.setBeforeHeadEnd(settingsCustomHtmlBean.getBeforeHeadEnd());
        }

        if (settingsCustomHtmlBean.getAfterBodyStart() != null) {
            customHtmlSettings.setAfterBodyStart(settingsCustomHtmlBean.getAfterBodyStart());
        }

        if (settingsCustomHtmlBean.getBeforeBodyEnd() != null) {
            customHtmlSettings.setBeforeBodyEnd(settingsCustomHtmlBean.getBeforeBodyEnd());
        }

        globalSettingsManager.updateGlobalSettings(settings);

        return getCustomHtml();
    }

    @Override
    public SettingsSecurityBean getSecurity() {
        final Settings settings = globalSettingsManager.getGlobalSettings();

        return SettingsSecurityBean.builder()
                .webSudoEnabled(settings.getWebSudoEnabled())
                .webSudoTimeout(settings.getWebSudoTimeout())
                .build();
    }

    @Override
    public SettingsSecurityBean setSecurity(
            final SettingsSecurityBean settingsSecurityBean) {

        final Settings settings = globalSettingsManager.getGlobalSettings();

        if (settingsSecurityBean.getWebSudoEnabled() != null) {
            settings.setWebSudoEnabled(settingsSecurityBean.getWebSudoEnabled());
        }

        if (settingsSecurityBean.getWebSudoTimeout() != null) {
            settings.setWebSudoTimeout(settingsSecurityBean.getWebSudoTimeout());
        }

        return getSecurity();
    }

}
