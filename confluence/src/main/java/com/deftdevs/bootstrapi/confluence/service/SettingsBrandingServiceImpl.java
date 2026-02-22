package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.plugins.lookandfeel.SiteLogoManager;
import com.atlassian.confluence.themes.BaseColourScheme;
import com.atlassian.confluence.themes.ColourScheme;
import com.atlassian.confluence.themes.ColourSchemeManager;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.confluence.model.util.SettingsBrandingColorSchemeModelUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SettingsBrandingServiceImpl implements SettingsBrandingService {

    private static final int DEFAULT_FAVICON_DIMENSION = 16;

    private static final String ERROR_MESSAGE_FAVICON = "It is currently not possible to use the favicon endpoints in BootstrAPI for Confluence because of API changes made by Atlassian";

    private final ColourSchemeManager colourSchemeManager;
    private final SiteLogoManager siteLogoManager;

    public SettingsBrandingServiceImpl(
            final ColourSchemeManager colourSchemeManager,
            final SiteLogoManager siteLogoManager) {

        this.colourSchemeManager = colourSchemeManager;
        this.siteLogoManager = siteLogoManager;
    }

    @Override
    public SettingsBrandingColorSchemeModel getColourScheme() {
        ColourScheme globalColourScheme = colourSchemeManager.getGlobalColourScheme();
        return SettingsBrandingColorSchemeModelUtil.toSettingsBrandingColorSchemeModel(globalColourScheme);
    }

    @Override
    public SettingsBrandingColorSchemeModel setColourScheme(
            final SettingsBrandingColorSchemeModel colorSchemeModel) {

        BaseColourScheme baseColourScheme = new BaseColourScheme(colourSchemeManager.getGlobalColourScheme());
        BaseColourScheme newColourScheme = SettingsBrandingColorSchemeModelUtil.toGlobalColorScheme(colorSchemeModel, baseColourScheme);
        colourSchemeManager.saveGlobalColourScheme(newColourScheme);
        return SettingsBrandingColorSchemeModelUtil.toSettingsBrandingColorSchemeModel(newColourScheme);
    }

    @Override
    public InputStream getLogo() {
        return siteLogoManager.getCurrent().getContent();
    }

    @Override
    public void setLogo(
            final InputStream inputStream) {

        try {
            File file = File.createTempFile("bootstrapi-temp", null);
            FileUtils.copyInputStreamToFile(inputStream, file);

            String contentType = file.toURI().toURL().openConnection().getContentType();
            siteLogoManager.uploadLogo(file, contentType);

            file.deleteOnExit();
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public InputStream getFavicon() {
        throw new InternalServerErrorException(ERROR_MESSAGE_FAVICON);
    }

    @Override
    public void setFavicon(
            final InputStream inputStream) {

        throw new InternalServerErrorException(ERROR_MESSAGE_FAVICON);
    }
}
