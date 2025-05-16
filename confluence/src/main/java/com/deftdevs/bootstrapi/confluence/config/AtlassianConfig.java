package com.deftdevs.bootstrapi.confluence.config;

import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.atlassian.cache.CacheManager;
import com.atlassian.confluence.languages.LocaleManager;
import com.atlassian.confluence.plugins.lookandfeel.SiteLogoManager;
import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.security.SpacePermissionManager;
import com.atlassian.confluence.setup.settings.GlobalSettingsManager;
import com.atlassian.confluence.themes.ColourSchemeManager;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.favicon.core.FaviconManager;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecStore;
import com.atlassian.gadgets.spec.GadgetSpecFactory;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.oauth.consumer.ConsumerService;
import com.atlassian.oauth.consumer.ConsumerTokenStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import com.atlassian.plugins.authentication.api.config.IdpConfigService;
import com.atlassian.plugins.authentication.api.config.SsoConfigService;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.license.LicenseHandler;
import com.atlassian.user.UserManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.importOsgiService;

@Configuration
public class AtlassianConfig {

    @Bean
    public ApplicationProperties applicationProperties() {
        return importOsgiService(ApplicationProperties.class);
    }

    @Bean
    public ApplinkStatusService applinkStatusService() {
        return importOsgiService(ApplinkStatusService.class);
    }

    @Bean
    public AuthenticationConfigurationManager authenticationConfigurationManager() {
        return importOsgiService(AuthenticationConfigurationManager.class);
    }

    @Bean
    public CacheManager cacheManager() {
        return importOsgiService(CacheManager.class);
    }

    @Bean
    public ColourSchemeManager colourSchemeManager() {
        return importOsgiService(ColourSchemeManager.class);
    }

    @Bean
    public ConsumerService consumerService() {
        return importOsgiService(ConsumerService.class);
    }

    @Bean
    public ConsumerTokenStore consumerTokenStore() {
        return importOsgiService(ConsumerTokenStore.class);
    }

    @Bean
    public CrowdDirectoryService crowdDirectoryService() {
        return importOsgiService(CrowdDirectoryService.class);
    }

    @Bean
    public ExternalGadgetSpecStore externalGadgetSpecStore() {
        return importOsgiService(ExternalGadgetSpecStore.class);
    }

    @Bean
    public FaviconManager faviconManager() {
        return importOsgiService(FaviconManager.class);
    }

    @Bean
    public GadgetSpecFactory gadgetSpecFactory() {
        return importOsgiService(GadgetSpecFactory.class);
    }

    @Bean
    public GlobalSettingsManager globalSettingsManager() {
        return importOsgiService(GlobalSettingsManager.class);
    }

    @Bean
    public IdpConfigService idpConfigService() {
        return importOsgiService(IdpConfigService.class);
    }

    @Bean
    public LicenseHandler licenseHandler() {
        return importOsgiService(LicenseHandler.class);
    }

    @Bean
    public LocaleManager localeManager() {
        return importOsgiService(LocaleManager.class);
    }

    @Bean
    public MailServerManager mailServerManager() {
        return importOsgiService(MailServerManager.class);
    }

    @Bean
    public MutatingApplicationLinkService mutatingApplicationLinkService() {
        return importOsgiService(MutatingApplicationLinkService.class);
    }

    @Bean
    public PermissionManager permissionManager() {
        return importOsgiService(PermissionManager.class);
    }

    @Bean
    public ServiceProviderConsumerStore serviceProviderConsumerStore() {
        return importOsgiService(ServiceProviderConsumerStore.class);
    }

    @Bean
    public ServiceProviderTokenStore serviceProviderTokenStore() {
        return importOsgiService(ServiceProviderTokenStore.class);
    }

    @Bean
    public SiteLogoManager siteLogoManager() {
        return importOsgiService(SiteLogoManager.class);
    }

    @Bean
    public SpacePermissionManager spacePermissionManager() {
        return importOsgiService(SpacePermissionManager.class);
    }

    @Bean
    public SsoConfigService ssoConfigService() {
        return importOsgiService(SsoConfigService.class);
    }

    @Bean
    public TypeAccessor typeAccessor() {
        return importOsgiService(TypeAccessor.class);
    }

    @Bean
    public UserAccessor userAccessor() {
        return importOsgiService(UserAccessor.class);
    }

    @Bean
    public UserManager userManager() {
        return importOsgiService(UserManager.class);
    }

}
