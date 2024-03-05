package de.aservo.confapi.confluence.service;

import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.setup.settings.Settings;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.model.SettingsBean;
import de.aservo.confapi.commons.service.api.SettingsService;
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
