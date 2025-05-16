package com.deftdevs.bootstrapi.confluence.beans;

import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.atlassian.cache.CacheManager;
import com.atlassian.confluence.languages.LocaleManager;
import com.atlassian.confluence.plugins.lookandfeel.SiteLogoManager;
import com.atlassian.confluence.security.SpacePermissionManager;
import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.themes.ColourSchemeManager;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.favicon.core.FaviconManager;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecStore;
import com.atlassian.gadgets.spec.GadgetSpecFactory;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.plugins.authentication.api.config.IdpConfigService;
import com.atlassian.plugins.authentication.api.config.SsoConfigService;
import com.atlassian.sal.api.license.LicenseHandler;
import com.atlassian.user.UserManager;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.commons.service.api.*;
import com.deftdevs.bootstrapi.confluence.service.*;
import com.deftdevs.bootstrapi.confluence.service.api.CachesService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceAuthenticationService;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceSettingsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AtlassianBeans.class,
        HelperBeans.class,
})
public class ServiceBeans {

    @Bean
    public ApplicationLinksService applicationLinksService(
            final MutatingApplicationLinkService mutatingApplicationLinkService,
            final ApplinkStatusService applinkStatusService,
            final TypeAccessor typeAccessor,
            final ApplicationLinksAuthConfigHelper applicationLinksAuthConfigHelper) {

        return new ApplicationLinksServiceImpl(
                mutatingApplicationLinkService,
                applinkStatusService,
                typeAccessor,
                applicationLinksAuthConfigHelper);
    }

    @Bean
    public CachesService cachesService(
            final CacheManager cacheManager) {

        return new CachesServiceImpl(
                cacheManager);
    }

    @Bean
    public ConfluenceAuthenticationService confluenceAuthenticationService(
            final IdpConfigService idpConfigService,
            final SsoConfigService ssoConfigService) {

        return new AuthenticationServiceImpl(
                idpConfigService,
                ssoConfigService);
    }

    @Bean
    public ConfluenceSettingsService confluenceSettingsService(
            final GlobalSettingsManager globalSettingsManager) {

        return new SettingsServiceImpl(
                globalSettingsManager);
    }

    @Bean
    public DirectoriesService directoriesService(
            final CrowdDirectoryService crowdDirectoryService) {

        return new DirectoryServiceImpl(
                crowdDirectoryService);
    }

    @Bean
    public GadgetsService gadgetsService(
            final ExternalGadgetSpecStore externalGadgetSpecStore,
            final GadgetSpecFactory gadgetSpecFactory,
            final LocaleManager localeManager) {

        return new GadgetsServiceImpl(
                externalGadgetSpecStore,
                gadgetSpecFactory,
                localeManager);
    }

    @Bean
    public LicensesService licensesService(
            final LicenseHandler licenseHandler) {

        return new LicensesServiceImpl(
                licenseHandler);
    }

    @Bean
    public MailServerService mailServerService(
            final MailServerManager mailServerManager) {

        return new MailServerServiceImpl(
                mailServerManager);
    }

    @Bean
    public PermissionsService permissionsService(
            final SpacePermissionManager spacePermissionManager) {

        return new PermissionsServiceImpl(
                spacePermissionManager);
    }

    @Bean
    public SettingsBrandingService settingsBrandingService(
            final ColourSchemeManager colourSchemeManager,
            final SiteLogoManager siteLogoManager,
            final FaviconManager faviconManager) {

        return new SettingsBrandingServiceImpl(
                colourSchemeManager,
                siteLogoManager,
                faviconManager);
    }

    @Bean
    public UsersService usersService(
            final UserManager userManager,
            final UserAccessor userAccessor) {

        return new UsersServiceImpl(
                userManager,
                userAccessor);
    }
}
