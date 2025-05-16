package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.config.properties.LookAndFeelBean;
import com.atlassian.jira.config.util.JiraHome;
import com.atlassian.jira.lookandfeel.ImageScaler;
import com.atlassian.jira.lookandfeel.LogoChoice;
import com.atlassian.jira.lookandfeel.LookAndFeelConstants;
import com.atlassian.jira.lookandfeel.LookAndFeelProperties;
import com.atlassian.jira.lookandfeel.upload.LogoUploader;
import com.atlassian.jira.lookandfeel.upload.UploadService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.common.base.Suppliers;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.commons.service.api.SettingsBrandingService;
import com.deftdevs.bootstrapi.jira.model.util.SettingsBrandingColorSchemeModelUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.function.Supplier;

import static com.atlassian.jira.lookandfeel.LookAndFeelConstants.JIRA_SCALED_LOGO_FILENAME;
import static org.apache.commons.lang3.BooleanUtils.toStringTrueFalse;

@Component
public class SettingsBrandingServiceImpl implements SettingsBrandingService {

    private final ApplicationProperties applicationProperties;
    private final UploadService uploadService;
    private final JiraHome jiraHome;
    private final JiraAuthenticationContext authenticationContext;
    private final PluginSettings globalSettings;
    private final Supplier<LookAndFeelBean> lookAndFeelModelSupplier;
    private final LookAndFeelProperties lookAndFeelProperties;

    @Inject
    public SettingsBrandingServiceImpl(
            @ComponentImport ApplicationProperties applicationProperties,
            @ComponentImport JiraAuthenticationContext authenticationContext,
            @ComponentImport JiraHome jiraHome,
            @ComponentImport PluginSettingsFactory globalSettingsFactory,
            @ComponentImport LookAndFeelProperties lookAndFeelProperties,
            @ComponentImport UploadService uploadService) {
        this.applicationProperties = applicationProperties;
        this.uploadService = uploadService;
        this.jiraHome = jiraHome;
        this.authenticationContext = authenticationContext;
        this.globalSettings = globalSettingsFactory.createGlobalSettings();
        //noinspection deprecation
        this.lookAndFeelModelSupplier = Suppliers.memoize(() -> LookAndFeelBean.getInstance(applicationProperties));
        this.lookAndFeelProperties = lookAndFeelProperties;
    }

    @Override
    public SettingsBrandingColorSchemeModel getColourScheme() {
        return SettingsBrandingColorSchemeModelUtil.getSettingsBrandingColorSchemeModel(applicationProperties);
    }

    @Override
    public SettingsBrandingColorSchemeModel setColourScheme(
            @NotNull SettingsBrandingColorSchemeModel colorSchemeModel) {
        SettingsBrandingColorSchemeModelUtil.setGlobalColorScheme(colorSchemeModel, false, applicationProperties);
        return SettingsBrandingColorSchemeModelUtil.getSettingsBrandingColorSchemeModel(applicationProperties);
    }

    @Override
    public InputStream getLogo() {
        return getLogoStream(JIRA_SCALED_LOGO_FILENAME);
    }

    @Override
    public void setLogo(
            @NotNull InputStream inputStream) {

        ImageScaler imageScaler = new ImageScaler();
        LogoUploader logoUploader = new LogoUploader(applicationProperties, jiraHome, imageScaler, authenticationContext.getI18nHelper(), uploadService);
        String logoUrl = logoUploader.saveLogo(inputStream,
                LookAndFeelConstants.JIRA_LOGO_FILENAME,
                JIRA_SCALED_LOGO_FILENAME);

        LookAndFeelBean lfb = lookAndFeelModelSupplier.get();
        lfb.setLogoWidth(Integer.toString(logoUploader.getResizedWidth()));
        lfb.setLogoHeight(Integer.toString(logoUploader.getResizedHeight()));
        lfb.setLogoUrl(ensureUrlCorrect(logoUrl));
        globalSettings.put(LookAndFeelConstants.USING_CUSTOM_LOGO, toStringTrueFalse(true));

        lookAndFeelProperties.setLogoChoice(LogoChoice.UPLOAD);
    }

    @Override
    public InputStream getFavicon() {
        return getLogoStream(LookAndFeelConstants.JIRA_SCALED_FAVICON_FILENAME);
    }

    @Override
    public void setFavicon(
            @NotNull InputStream inputStream) {

        ImageScaler imageScaler = new ImageScaler();
        final LogoUploader logoUploader = new LogoUploader(applicationProperties, jiraHome, imageScaler, authenticationContext.getI18nHelper(), uploadService);
        String faviconUrl = logoUploader.saveFavicon(inputStream,
                LookAndFeelConstants.JIRA_FAVICON_FILENAME,
                LookAndFeelConstants.JIRA_FAVICON_HIRES_FILENAME,
                LookAndFeelConstants.JIRA_SCALED_FAVICON_FILENAME,
                LookAndFeelConstants.JIRA_FAVICON_IEFORMAT_FILENAME);

        LookAndFeelBean lfb = lookAndFeelModelSupplier.get();
        lfb.setFaviconUrl(ensureUrlCorrect(faviconUrl));
        lfb.setFaviconHiResUrl("/" + LookAndFeelConstants.JIRA_FAVICON_HIRES_FILENAME);
        globalSettings.put(LookAndFeelConstants.USING_CUSTOM_FAVICON, toStringTrueFalse(true));

        lookAndFeelProperties.setFaviconChoice(LogoChoice.UPLOAD);
    }

    private String ensureUrlCorrect(
            String url) {
        // url must start with 'http://', 'http://', or else add the leading '/'
        if (StringUtils.isNotBlank(url) && !url.startsWith("http") && !url.startsWith("/")) {
            url = "/" + url;
        }
        return url;
    }

    private FileInputStream getLogoStream(
            String key) {
        final File logoDirectory = uploadService.getLogoDirectory();
        try {
            return new FileInputStream(new File(logoDirectory, key));
        } catch (FileNotFoundException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
