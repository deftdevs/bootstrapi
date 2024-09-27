package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsSecurityBean;

import javax.validation.constraints.NotNull;

public interface SettingsSecurityService<B extends SettingsSecurityBean> {

    /**
     * Get the security settings.
     *
     * @return the security settings
     */
    B getSettingsSecurity();

    /**
     * Set the settings
     *
     * @param settingsSecurityBean the security settings to set
     * @return the security settings
     */
    B setSettingsSecurity(
            @NotNull final B settingsSecurityBean);

}
