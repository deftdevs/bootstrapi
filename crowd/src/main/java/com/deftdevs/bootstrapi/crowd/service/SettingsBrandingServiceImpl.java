package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.atlassian.crowd.model.lookandfeel.LookAndFeelConfiguration;
import com.atlassian.crowd.util.ImageInfo;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageBean;
import com.deftdevs.bootstrapi.crowd.service.api.SettingsBrandingService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
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

@Component
public class SettingsBrandingServiceImpl implements SettingsBrandingService {

    private final PropertyManager propertyManager;

    @Inject
    public SettingsBrandingServiceImpl(
            final PropertyManager propertyManager) {

        this.propertyManager = propertyManager;
    }

    @Override
    public SettingsBrandingLoginPageBean getLoginPage() {
        final SettingsBrandingLoginPageBean settingsBrandingLoginPageBean = new SettingsBrandingLoginPageBean();

        LookAndFeelConfiguration lookAndFeelConfiguration = getLookAndFeelConfiguration();
        settingsBrandingLoginPageBean.setHeader(lookAndFeelConfiguration.getHeader());
        settingsBrandingLoginPageBean.setContent(lookAndFeelConfiguration.getWelcomeText());
        settingsBrandingLoginPageBean.setButtonColor(lookAndFeelConfiguration.getPrimaryColor());
        settingsBrandingLoginPageBean.setShowLogo(lookAndFeelConfiguration.isShowLogo());

        return settingsBrandingLoginPageBean;
    }

    @Override
    public SettingsBrandingLoginPageBean setLoginPage(
            SettingsBrandingLoginPageBean settingsBrandingLoginPageBean) {

        LookAndFeelConfiguration lookAndFeelConfiguration = getLookAndFeelConfiguration();
        LookAndFeelConfiguration.Builder builder = LookAndFeelConfiguration.builder(lookAndFeelConfiguration);

        if (settingsBrandingLoginPageBean.getShowLogo() != null) {
            builder.setShowLogo(settingsBrandingLoginPageBean.getShowLogo());
        }
        if (settingsBrandingLoginPageBean.getHeader() != null) {
            builder.setHeader(settingsBrandingLoginPageBean.getHeader());
        }
        if (settingsBrandingLoginPageBean.getContent() != null) {
            builder.setWelcomeText(settingsBrandingLoginPageBean.getContent());
        }
        if (settingsBrandingLoginPageBean.getButtonColor() != null) {
            builder.setPrimaryColor(settingsBrandingLoginPageBean.getButtonColor());
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
