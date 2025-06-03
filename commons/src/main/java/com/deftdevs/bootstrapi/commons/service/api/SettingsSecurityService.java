package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;


public interface SettingsSecurityService<B extends SettingsSecurityModel> {

    /**
     * Get the security settings.
     *
     * @return the security settings
     */
    B getSettingsSecurity();

    /**
     * Set the settings
     *
     * @param settingsSecurityModel the security settings to set
     * @return the security settings
     */
    B setSettingsSecurity(
            final B settingsSecurityModel);

}
