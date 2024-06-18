package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsBean;

import javax.validation.constraints.NotNull;

public interface SettingsService {

    /**
     * Get the settings.
     *
     * @return the settings
     */
    SettingsBean getSettings();


    /**
     * Set the settings
     *
     * @param settingsBean the settings to set
     * @return the settings
     */
    SettingsBean setSettings(
            @NotNull final SettingsBean settingsBean);
}
