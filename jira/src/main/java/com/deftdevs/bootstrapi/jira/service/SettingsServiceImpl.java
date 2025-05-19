package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerModel;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;

import java.net.URI;

import static com.atlassian.jira.config.properties.APKeys.*;

public class SettingsServiceImpl implements JiraSettingsService {

    private final ApplicationProperties applicationProperties;

    /**
     * Constructor.
     *
     * @param applicationProperties {@link ApplicationProperties}
     */
    public SettingsServiceImpl(
            final ApplicationProperties applicationProperties) {

        this.applicationProperties = applicationProperties;
    }

    @Override
    public SettingsModel getSettingsGeneral() {
        final SettingsModel settingsModel = new SettingsModel();
        final String baseUrl = applicationProperties.getString(JIRA_BASEURL);
        if (baseUrl != null) {
            settingsModel.setBaseUrl(URI.create(baseUrl));
        }
        settingsModel.setMode(applicationProperties.getString(JIRA_MODE));
        settingsModel.setTitle(applicationProperties.getString(JIRA_TITLE));
        settingsModel.setContactMessage(applicationProperties.getString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE));
        settingsModel.setExternalUserManagement(Boolean.parseBoolean(applicationProperties.getString(JIRA_OPTION_USER_EXTERNALMGT)));

        return settingsModel;
    }

    @Override
    public SettingsModel setSettingsGeneral(
            final SettingsModel settingsModel) {

        if (settingsModel.getBaseUrl() != null) {
            applicationProperties.setString(JIRA_BASEURL, settingsModel.getBaseUrl().toString());
        }

        if (settingsModel.getMode() != null) {
            if (!settingsModel.getMode().equalsIgnoreCase("public") && !settingsModel.getMode().equalsIgnoreCase("private")) {
                throw new BadRequestException("Mode '" + settingsModel.getMode() + "' is not supported");
            } else if (settingsModel.getMode().equalsIgnoreCase("public") && applicationProperties.getOption(APKeys.JIRA_OPTION_USER_EXTERNALMGT)) {
                throw new BadRequestException("Mode '" + settingsModel.getMode() + "' cannot be set with external user management");
            }

            applicationProperties.setString(JIRA_MODE, settingsModel.getMode());
        }

        if (settingsModel.getTitle() != null) {
            applicationProperties.setString(JIRA_TITLE, settingsModel.getTitle());
        }

        if (settingsModel.getContactMessage() != null) {
            applicationProperties.setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, settingsModel.getContactMessage());
        }

        if (settingsModel.getExternalUserManagement() != null) {
            applicationProperties.setString(JIRA_OPTION_USER_EXTERNALMGT, String.valueOf(settingsModel.getExternalUserManagement()));
        }

        return getSettingsGeneral();
    }

    @Override
    public SettingsSecurityModel getSettingsSecurity() {
        final Boolean webSudoEnabled = !applicationProperties.getOption(WebSudo.IS_DISABLED);
        final String webSudoTimeout = applicationProperties.getDefaultBackedString(WebSudo.TIMEOUT);

        return SettingsSecurityModel.builder()
                .webSudoEnabled(webSudoEnabled)
                .webSudoTimeout(Long.valueOf(webSudoTimeout))
                .build();
    }

    @Override
    public SettingsSecurityModel setSettingsSecurity(
            final SettingsSecurityModel settingsSecurityModel) {

        if (settingsSecurityModel.getWebSudoEnabled() != null) {
            applicationProperties.setOption(WebSudo.IS_DISABLED, !settingsSecurityModel.getWebSudoEnabled());
        }

        if (settingsSecurityModel.getWebSudoTimeout() != null) {
            applicationProperties.setString(WebSudo.TIMEOUT, Long.toString(settingsSecurityModel.getWebSudoTimeout()));
        }

        return getSettingsSecurity();
    }

    @Override
    public SettingsBannerModel getSettingsBanner() {
        final String content = applicationProperties.getDefaultBackedText(JIRA_ALERT_HEADER);
        final String visibilityString = applicationProperties.getDefaultBackedString(JIRA_ALERT_HEADER_VISIBILITY);
        final SettingsBannerModel.Visibility visibility = SettingsBannerModel.Visibility.valueOf(visibilityString.toUpperCase());

        return SettingsBannerModel.builder()
                .content(content)
                .visibility(visibility)
                .build();
    }

    @Override
    public SettingsBannerModel setSettingsBanner(
            final SettingsBannerModel settingsBannerModel) {

        if (settingsBannerModel.getContent() != null) {
            applicationProperties.setText(JIRA_ALERT_HEADER, settingsBannerModel.getContent());
        }

        if (settingsBannerModel.getVisibility() != null) {
            applicationProperties.setString(JIRA_ALERT_HEADER_VISIBILITY, settingsBannerModel.getVisibility().name().toLowerCase());
        }

        return getSettingsBanner();
    }

}
