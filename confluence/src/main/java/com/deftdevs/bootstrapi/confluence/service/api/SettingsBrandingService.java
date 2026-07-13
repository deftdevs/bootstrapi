package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;

import java.io.InputStream;

public interface SettingsBrandingService {

    /**
     * Get the colour scheme.
     *
     * @return the colour scheme
     */
    SettingsBrandingColorSchemeModel getSettingsBrandingColorScheme();

    /**
     * Set the colour scheme
     *
     * @param colourSchemeModel the colour scheme to set
     * @return the updated colour scheme
     */
    SettingsBrandingColorSchemeModel setSettingsBrandingColorScheme(
            final SettingsBrandingColorSchemeModel colourSchemeModel);

    /**
     * Get the logo binary.
     *
     * @return the logo
     */
    InputStream getSettingsBrandingLogo();

    /**
     * Set the logo
     *
     * @param logoBinary the logo to set
     */
    void setSettingsBrandingLogo(
            InputStream logoBinary);

    /**
     * Get the favicon binary.
     *
     * @return the favicon
     */
    InputStream getSettingsBrandingFavicon();

    /**
     * Set the favicon
     *
     * @param faviconBinary the favicon to set
     */
    void setSettingsBrandingFavicon(
            InputStream faviconBinary);

}
