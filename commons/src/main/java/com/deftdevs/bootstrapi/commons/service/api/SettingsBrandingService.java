package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;

import java.io.InputStream;

public interface SettingsBrandingService {

    /**
     * Get the colour scheme.
     *
     * @return the colour scheme
     */
    SettingsBrandingColorSchemeModel getColourScheme();

    /**
     * Set the colour scheme
     *
     * @param colourSchemeModel the colour scheme to set
     * @return the updated colour scheme
     */
    SettingsBrandingColorSchemeModel setColourScheme(
            final SettingsBrandingColorSchemeModel colourSchemeModel);

    /**
     * Get the logo binary.
     *
     * @return the logo
     */
    InputStream getLogo();

    /**
     * Set the logo
     *
     * @param logoBinary the logo to set
     */
    void setLogo(
            InputStream logoBinary);

    /**
     * Get the favicon binary.
     *
     * @return the favicon
     */
    InputStream getFavicon();

    /**
     * Set the favicon
     *
     * @param faviconBinary the favicon to set
     */
    void setFavicon(
            InputStream faviconBinary);

}
