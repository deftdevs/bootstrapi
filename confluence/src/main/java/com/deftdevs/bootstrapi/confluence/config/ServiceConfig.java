package com.deftdevs.bootstrapi.confluence.config;

import com.deftdevs.bootstrapi.commons.service.api.*;
import com.deftdevs.bootstrapi.confluence.model._AllModel;
import com.deftdevs.bootstrapi.confluence.service.*;
import com.deftdevs.bootstrapi.confluence.service.api.CachesService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceAuthenticationService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
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
    public _AllService<_AllModel> _allService() {
        return new _AllServiceImpl(
                confluenceSettingsService(),
                directoriesService(),
                applicationLinksService(),
                confluenceAuthenticationService(),
                licensesService(),
                mailServerService(),
                permissionsService());
    }

    @Bean
    public ApplicationLinksService applicationLinksService() {
        return new ApplicationLinksServiceImpl(
                atlassianConfig.mutatingApplicationLinkService(),
                atlassianConfig.applinkStatusService(),
                atlassianConfig.typeAccessor(),
                helperConfig.applicationLinksAuthConfigHelper());
    }

    @Bean
    public CachesService cachesService() {
        return new CachesServiceImpl(
                atlassianConfig.cacheManager());
    }

    @Bean
    public ConfluenceAuthenticationService confluenceAuthenticationService() {
        return new AuthenticationServiceImpl(
                atlassianConfig.idpConfigService(),
                atlassianConfig.ssoConfigService());
    }

    @Bean
    public ConfluenceSettingsService confluenceSettingsService() {
        // The branding service is deliberately NOT its own bean: ConfluenceSettingsService
        // extends SettingsBrandingService, so a standalone branding bean would make by-type
        // injection of SettingsBrandingService ambiguous in the REST layer.
        return new SettingsServiceImpl(
                atlassianConfig.globalSettingsManager(),
                new SettingsBrandingServiceImpl(
                        atlassianConfig.colourSchemeManager(),
                        atlassianConfig.siteLogoManager()));
    }

    @Bean
    public DirectoriesService directoriesService() {
        return new DirectoriesServiceImpl(
                atlassianConfig.crowdDirectoryService());
    }

    @Bean
    public LicensesService licensesService() {
        return new LicensesServiceImpl(
                atlassianConfig.licenseHandler());
    }

    @Bean
    public MailServerService mailServerService() {
        return new MailServerServiceImpl(
                atlassianConfig.mailServerManager());
    }

    @Bean
    public PermissionsService permissionsService() {
        return new PermissionsServiceImpl(
                atlassianConfig.spacePermissionManager());
    }

    @Bean
    public UsersService usersService() {
        return new UsersServiceImpl(
                atlassianConfig.userManager(),
                atlassianConfig.userAccessor());
    }
}
