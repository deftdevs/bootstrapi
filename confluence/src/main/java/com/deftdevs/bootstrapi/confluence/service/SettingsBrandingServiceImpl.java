package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.plugins.lookandfeel.SiteLogoManager;
import com.atlassian.confluence.themes.BaseColourScheme;
import com.atlassian.confluence.themes.ColourScheme;
import com.atlassian.confluence.themes.ColourSchemeManager;
import com.atlassian.core.util.thumbnail.ThumbnailDimension;
import com.atlassian.favicon.core.FaviconManager;
import com.atlassian.favicon.core.ImageType;
import com.atlassian.favicon.core.StoredFavicon;
import com.atlassian.favicon.core.UploadedFaviconFile;
import com.atlassian.favicon.core.exceptions.ImageStorageException;
import com.atlassian.favicon.core.exceptions.InvalidImageDataException;
import com.atlassian.favicon.core.exceptions.UnsupportedImageTypeException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.confluence.model.util.SettingsBrandingColorSchemeModelUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
public class SettingsBrandingServiceImpl implements SettingsBrandingService {

    private static final int DEFAULT_FAVICON_DIMENSION = 16;

    private final ColourSchemeManager colourSchemeManager;
    private final FaviconManager faviconManager;
    private final SiteLogoManager siteLogoManager;

    @Inject
    public SettingsBrandingServiceImpl(
            @ComponentImport ColourSchemeManager colourSchemeManager,
            @ComponentImport SiteLogoManager siteLogoManager,
            @ComponentImport FaviconManager faviconManager) {
        this.colourSchemeManager = colourSchemeManager;
        this.siteLogoManager = siteLogoManager;
        this.faviconManager = faviconManager;
    }

    @Override
    public SettingsBrandingColorSchemeModel getColourScheme() {
        ColourScheme globalColourScheme = colourSchemeManager.getGlobalColourScheme();
        return SettingsBrandingColorSchemeModelUtil.toSettingsBrandingColorSchemeModel(globalColourScheme);
    }

    @Override
    public SettingsBrandingColorSchemeModel setColourScheme(
            @NotNull SettingsBrandingColorSchemeModel colorSchemeModel) {
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
            @NotNull InputStream inputStream) {
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
        if (faviconManager.isFaviconConfigured()) {
            Optional<StoredFavicon> favicon = faviconManager.getFavicon(ImageType.PNG, new ThumbnailDimension(DEFAULT_FAVICON_DIMENSION, DEFAULT_FAVICON_DIMENSION));
            if (favicon.isPresent()) {
                return favicon.get().getImageDataStream();
            }
        }

        throw new NotFoundException("No favicon configured");
    }

    @Override
    public void setFavicon(
            @NotNull InputStream inputStream) {
        try {
            File file = File.createTempFile("bootstrapi-temp", null);
            FileUtils.copyInputStreamToFile(inputStream, file);

            String contentType = file.toURI().toURL().openConnection().getContentType();
            Optional<ImageType> imageType = ImageType.parseFromContentType(contentType);
            if (!imageType.isPresent()) {
                throw new BadRequestException("Image type could not be determined from source image.");
            }

            UploadedFaviconFile faviconFile = new UploadedFaviconFile(file, imageType.get());
            faviconManager.setFavicon(faviconFile);

            file.deleteOnExit();
        } catch (IOException | ImageStorageException e) {
            throw new InternalServerErrorException(e);
        } catch (UnsupportedImageTypeException | InvalidImageDataException e) {
            throw new BadRequestException(e);
        }
    }
}
