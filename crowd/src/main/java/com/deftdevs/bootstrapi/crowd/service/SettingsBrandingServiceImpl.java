package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.atlassian.crowd.model.lookandfeel.LookAndFeelConfiguration;
import com.atlassian.crowd.util.ImageInfo;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsBrandingService;
import org.apache.commons.io.FileUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SettingsBrandingServiceImpl implements CrowdSettingsBrandingService {

    private final PropertyManager propertyManager;

    public SettingsBrandingServiceImpl(
            final PropertyManager propertyManager) {

        this.propertyManager = propertyManager;
    }

    @Override
    public SettingsBrandingLoginPageModel getLoginPage() {
        final SettingsBrandingLoginPageModel settingsBrandingLoginPageModel = new SettingsBrandingLoginPageModel();

        LookAndFeelConfiguration lookAndFeelConfiguration = getLookAndFeelConfiguration();
        settingsBrandingLoginPageModel.setHeader(lookAndFeelConfiguration.getHeader());
        settingsBrandingLoginPageModel.setContent(lookAndFeelConfiguration.getWelcomeText());
        settingsBrandingLoginPageModel.setButtonColor(lookAndFeelConfiguration.getPrimaryColor());
        settingsBrandingLoginPageModel.setShowLogo(lookAndFeelConfiguration.isShowLogo());

        return settingsBrandingLoginPageModel;
    }

    @Override
    public SettingsBrandingLoginPageModel setLoginPage(
            SettingsBrandingLoginPageModel settingsBrandingLoginPageModel) {

        LookAndFeelConfiguration lookAndFeelConfiguration = getLookAndFeelConfiguration();
        LookAndFeelConfiguration.Builder builder = LookAndFeelConfiguration.builder(lookAndFeelConfiguration);

        if (settingsBrandingLoginPageModel.getShowLogo() != null) {
            builder.setShowLogo(settingsBrandingLoginPageModel.getShowLogo());
        }
        if (settingsBrandingLoginPageModel.getHeader() != null) {
            builder.setHeader(settingsBrandingLoginPageModel.getHeader());
        }
        if (settingsBrandingLoginPageModel.getContent() != null) {
            builder.setWelcomeText(settingsBrandingLoginPageModel.getContent());
        }
        if (settingsBrandingLoginPageModel.getButtonColor() != null) {
            builder.setPrimaryColor(settingsBrandingLoginPageModel.getButtonColor());
        }

        try {
            propertyManager.setLookAndFeelConfiguration(builder.build(), null);
        } catch (PropertyManagerException e) {
            throw new BadRequestException(e.getMessage());
        }

        return getLoginPage();
    }

    @Override
    public void setLogo(
            @NotNull InputStream inputStream) {

        ImageInfo imageInfo = getImageInfo(inputStream);

        LookAndFeelConfiguration.Builder builder = LookAndFeelConfiguration.builder(getLookAndFeelConfiguration());
        builder.setCustomLogoId(imageInfo.getName());

        try {
            propertyManager.setLookAndFeelConfiguration(builder.build(), imageInfo);
        } catch (PropertyManagerException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private LookAndFeelConfiguration getLookAndFeelConfiguration() {

        try {
            Optional<LookAndFeelConfiguration> lookAndFeelConfigurationOptional = propertyManager.getLookAndFeelConfiguration();
            if (lookAndFeelConfigurationOptional.isPresent()) {
                return lookAndFeelConfigurationOptional.get();
            }
        } catch (PropertyManagerException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        return LookAndFeelConfiguration.builder().build();
    }

    private ImageInfo getImageInfo(InputStream inputStream) {
        ImageInfo imageInfo;
        try {
            File file = File.createTempFile("image", null);
            FileUtils.copyInputStreamToFile(inputStream, file);
            String contentType = file.toURI().toURL().openConnection().getContentType();

            final Set<String> allowedTypes = Stream.of("png", "jpg").collect(Collectors.toSet());
            String extension = contentType.split("/")[1];

            if (!allowedTypes.contains(extension)) {
                throw new BadRequestException("The content type must be one of: " + allowedTypes.toString());
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String base64File = Base64.getEncoder().encodeToString(fileContent);

            imageInfo = new ImageInfo("name", base64File, extension);

            file.deleteOnExit();

        } catch (
                IOException e) {
            throw new InternalServerErrorException(e);
        }
        return imageInfo;
    }

}
