package de.aservo.confapi.jira.service;

import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.SettingsBean;
import de.aservo.confapi.commons.service.api.SettingsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import static com.atlassian.jira.config.properties.APKeys.*;

@Component
@ExportAsService(SettingsService.class)
public class SettingsServiceImpl implements SettingsService {

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
    public SettingsBean getSettings() {
        final SettingsBean settingsBean = new SettingsBean();
        settingsBean.setBaseUrl(applicationProperties.getString(JIRA_BASEURL));
        settingsBean.setMode(applicationProperties.getString(JIRA_MODE));
        settingsBean.setTitle(applicationProperties.getString(JIRA_TITLE));
        return settingsBean;
    }

    @Override
    public SettingsBean setSettings(
            @NotNull final SettingsBean settingsBean) {

        if (settingsBean.getBaseUrl() != null) {
            applicationProperties.setString(JIRA_BASEURL, settingsBean.getBaseUrl());
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

        return getSettings();
    }

}
