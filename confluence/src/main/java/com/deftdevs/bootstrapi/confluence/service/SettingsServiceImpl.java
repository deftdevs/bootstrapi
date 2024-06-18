package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.setup.settings.Settings;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.URI;

@Component
@ExportAsService(SettingsService.class)
public class SettingsServiceImpl implements SettingsService {

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
}
