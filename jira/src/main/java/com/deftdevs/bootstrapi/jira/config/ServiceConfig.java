package com.deftdevs.bootstrapi.jira.config;

import com.deftdevs.bootstrapi.commons.service.api.*;
import com.deftdevs.bootstrapi.jira.service.*;
import com.deftdevs.bootstrapi.jira.service.api.JiraAuthenticationService;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Autowired
    private AtlassianConfig atlassianConfig;

    @Autowired
    private HelperConfig helperConfig;

    @Bean
    public ApplicationLinksService applicationLinksService() {
        return new ApplicationLinksServiceImpl(
                atlassianConfig.mutatingApplicationLinkService(),
                atlassianConfig.applinkStatusService(),
                atlassianConfig.typeAccessor(),
                helperConfig.applicationLinksAuthConfigHelper());
    }

    @Bean
    public DirectoriesService directoriesService() {
        return new DirectoryServiceImpl(
                atlassianConfig.crowdDirectoryService());
    }

    @Bean
    public JiraAuthenticationService jiraAuthenticationService() {
        return new AuthenticationServiceImpl(
                atlassianConfig.idpConfigService(),
                atlassianConfig.ssoConfigService());
    }

    @Bean
    public JiraSettingsService jiraSettingsService() {
        return new SettingsServiceImpl(
                atlassianConfig.applicationProperties());
    }

    @Bean
    public LicensesService licensesService() {
        return new LicensesServiceImpl(
                atlassianConfig.jiraLicenseManager());
    }

    @Bean
    public MailServerService mailServerService() {
        return new MailServerServiceImpl(
                atlassianConfig.mailServerManager());
    }

    @Bean
    public PermissionsService permissionsService() {
        return new PermissionsServiceImpl(
                atlassianConfig.globalPermissionManager());
    }

    @Bean
    public SettingsBrandingService settingsBrandingService() {
        return new SettingsBrandingServiceImpl(
                atlassianConfig.applicationProperties(),
                atlassianConfig.jiraAuthenticationContext(),
                atlassianConfig.jiraHome(),
                atlassianConfig.pluginSettingsFactory(),
                atlassianConfig.lookAndFeelProperties(),
                atlassianConfig.uploadService());
    }

}
