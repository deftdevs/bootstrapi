package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;


public interface SettingsService<B extends SettingsModel> {

    /**
     * Get the settings.
     *
     * @return the general settings
     */
    B getSettingsGeneral();

    /**
     * Set the settings
     *
     * @param settingsModel the general settings to set
     * @return the settings
     */
    B setSettingsGeneral(
            final B settingsModel);

}
