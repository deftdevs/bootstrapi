package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsBean;

import javax.validation.constraints.NotNull;

public interface SettingsService<B extends SettingsBean> {

    /**
     * Get the settings.
     *
     * @return the general settings
     */
    B getSettingsGeneral();

    /**
     * Set the settings
     *
     * @param settingsBean the general settings to set
     * @return the settings
     */
    B setSettingsGeneral(
            @NotNull final B settingsBean);

}
