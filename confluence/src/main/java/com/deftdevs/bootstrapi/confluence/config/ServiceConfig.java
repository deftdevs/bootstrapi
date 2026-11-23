package com.deftdevs.bootstrapi.confluence.config;

import com.deftdevs.bootstrapi.commons.config.CloudConnectorConfig;
import com.deftdevs.bootstrapi.commons.service.DefaultCloudTunnelServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.*;
import com.deftdevs.bootstrapi.confluence.model._AllModel;
import com.deftdevs.bootstrapi.confluence.service.*;
import com.deftdevs.bootstrapi.confluence.service.api.CachesService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceAuthenticationService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
import lombok.RequiredArgsConstructor;
import org.osgi.framework.FrameworkUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServiceConfig {

    private final AtlassianConfig atlassianConfig;

    private final CloudConnectorConfig cloudConnectorConfig;

    private final HelperConfig helperConfig;

    @Bean
    public _AllService<_AllModel> _allService() {
        return new _AllServiceImpl(
                confluenceSettingsService(),
                directoriesService(),
                applicationLinksService(),
                cloudConnectorConfig.cloudConnectorService(),
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

    // the tunnel service is looked up per request, as its plugin is optional
    @Bean
    public CloudTunnelService cloudTunnelService() {
        return new DefaultCloudTunnelServiceImpl(
                FrameworkUtil.getBundle(ServiceConfig.class).getBundleContext(),
                atlassianConfig.mutatingApplicationLinkService());
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
