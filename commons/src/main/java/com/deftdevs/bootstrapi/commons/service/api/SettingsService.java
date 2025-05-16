package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsModel;

import javax.validation.constraints.NotNull;

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
            @NotNull final B settingsModel);

}
