package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityBean;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import com.deftdevs.bootstrapi.jira.model.SettingsBannerBean;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.URI;

import static com.atlassian.jira.config.properties.APKeys.*;

@Component
public class SettingsServiceImpl implements JiraSettingsService {

    private final ApplicationProperties applicationProperties;

    /**
     * Constructor.
     *
     * @param applicationProperties injected {@link ApplicationProperties}
     */
    @Inject
    public SettingsServiceImpl(
            @ComponentImport final ApplicationProperties applicationProperties) {

        this.applicationProperties = applicationProperties;
    }

    @Override
    public SettingsBean getSettingsGeneral() {
        final SettingsBean settingsBean = new SettingsBean();
        final String baseUrl = applicationProperties.getString(JIRA_BASEURL);
        if (baseUrl != null) {
            settingsBean.setBaseUrl(URI.create(baseUrl));
        }
        settingsBean.setMode(applicationProperties.getString(JIRA_MODE));
        settingsBean.setTitle(applicationProperties.getString(JIRA_TITLE));
        settingsBean.setContactMessage(applicationProperties.getString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE));
        settingsBean.setExternalUserManagement(Boolean.parseBoolean(applicationProperties.getString(JIRA_OPTION_USER_EXTERNALMGT)));

        return settingsBean;
    }

    @Override
    public SettingsBean setSettingsGeneral(
            final SettingsBean settingsBean) {

        if (settingsBean.getBaseUrl() != null) {
            applicationProperties.setString(JIRA_BASEURL, settingsBean.getBaseUrl().toString());
        }

        if (settingsBean.getMode() != null) {
            if (!settingsBean.getMode().equalsIgnoreCase("public") && !settingsBean.getMode().equalsIgnoreCase("private")) {
                throw new BadRequestException("Mode '" + settingsBean.getMode() + "' is not supported");
            } else if (settingsBean.getMode().equalsIgnoreCase("public") && applicationProperties.getOption(APKeys.JIRA_OPTION_USER_EXTERNALMGT)) {
                throw new BadRequestException("Mode '" + settingsBean.getMode() + "' cannot be set with external user management");
            }

            applicationProperties.setString(JIRA_MODE, settingsBean.getMode());
        }

        if (settingsBean.getTitle() != null) {
            applicationProperties.setString(JIRA_TITLE, settingsBean.getTitle());
        }

        if (settingsBean.getContactMessage() != null) {
            applicationProperties.setString(JIRA_CONTACT_ADMINISTRATORS_MESSSAGE, settingsBean.getContactMessage());
        }

        if (settingsBean.getExternalUserManagement() != null) {
            applicationProperties.setString(JIRA_OPTION_USER_EXTERNALMGT, String.valueOf(settingsBean.getExternalUserManagement()));
        }

        return getSettingsGeneral();
    }

    @Override
    public SettingsSecurityBean getSettingsSecurity() {
        final Boolean webSudoEnabled = !applicationProperties.getOption(WebSudo.IS_DISABLED);
        final String webSudoTimeout = applicationProperties.getDefaultBackedString(WebSudo.TIMEOUT);

        return SettingsSecurityBean.builder()
                .webSudoEnabled(webSudoEnabled)
                .webSudoTimeout(Long.valueOf(webSudoTimeout))
                .build();
    }

    @Override
    public SettingsSecurityBean setSettingsSecurity(
            final SettingsSecurityBean settingsSecurityBean) {

        if (settingsSecurityBean.getWebSudoEnabled() != null) {
            applicationProperties.setOption(WebSudo.IS_DISABLED, !settingsSecurityBean.getWebSudoEnabled());
        }

        if (settingsSecurityBean.getWebSudoTimeout() != null) {
            applicationProperties.setString(WebSudo.TIMEOUT, Long.toString(settingsSecurityBean.getWebSudoTimeout()));
        }

        return getSettingsSecurity();
    }

    @Override
    public SettingsBannerBean getSettingsBanner() {
        final String content = applicationProperties.getDefaultBackedText(JIRA_ALERT_HEADER);
        final String visibilityString = applicationProperties.getDefaultBackedString(JIRA_ALERT_HEADER_VISIBILITY);
        final SettingsBannerBean.Visibility visibility = SettingsBannerBean.Visibility.valueOf(visibilityString.toUpperCase());

        return SettingsBannerBean.builder()
                .content(content)
                .visibility(visibility)
                .build();
    }

    @Override
    public SettingsBannerBean setSettingsBanner(
            final SettingsBannerBean settingsBannerBean) {

        if (settingsBannerBean.getContent() != null) {
            applicationProperties.setText(JIRA_ALERT_HEADER, settingsBannerBean.getContent());
        }

        if (settingsBannerBean.getVisibility() != null) {
            applicationProperties.setString(JIRA_ALERT_HEADER_VISIBILITY, settingsBannerBean.getVisibility().name().toLowerCase());
        }

        return getSettingsBanner();
    }

}
