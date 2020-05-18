package de.aservo.atlassian.confapi.service.api;

import de.aservo.atlassian.confapi.model.SettingsBean;

import javax.validation.constraints.NotNull;

public interface SettingsService {

    /**
     * Get the settings.
     *
     * @return the settings
     */
    public SettingsBean getSettings();


    /**
     * Set the settings
     *
     * @param settingsBean the settings to set
     * @return the settings
     */
    public SettingsBean setSettings(
            @NotNull final SettingsBean settingsBean);
}
