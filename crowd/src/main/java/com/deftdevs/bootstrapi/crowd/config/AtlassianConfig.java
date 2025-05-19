package com.deftdevs.bootstrapi.crowd.config;

import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.manager.application.ApplicationManager;
import com.atlassian.crowd.manager.application.DefaultGroupMembershipService;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.license.CrowdLicenseManager;
import com.atlassian.crowd.manager.mail.MailConfigurationService;
import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.proxy.TrustedProxyManager;
import com.atlassian.oauth.consumer.ConsumerService;
import com.atlassian.oauth.consumer.ConsumerTokenStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.importOsgiService;

@Configuration
public class AtlassianConfig {

    @Bean
    public ApplicationManager applicationManager() {
        return importOsgiService(ApplicationManager.class);
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
    public ConsumerService consumerService() {
        return importOsgiService(ConsumerService.class);
    }

    @Bean
    public ConsumerTokenStore consumerTokenStore() {
        return importOsgiService(ConsumerTokenStore.class);
    }

    @Bean
    public CrowdLicenseManager crowdLicenseManager() {
        return importOsgiService(CrowdLicenseManager.class);
    }

    @Bean
    public CrowdService crowdService() {
        return importOsgiService(CrowdService.class);
    }

    @Bean
    public DefaultGroupMembershipService defaultGroupMembershipService() {
        return importOsgiService(DefaultGroupMembershipService.class);
    }

    @Bean
    public DirectoryManager directoryManager() {
        return importOsgiService(DirectoryManager.class);
    }

    @Bean
    public MailConfigurationService mailConfigurationService() {
        return importOsgiService(MailConfigurationService.class);
    }

    @Bean
    public MutatingApplicationLinkService mutatingApplicationLinkService() {
        return importOsgiService(MutatingApplicationLinkService.class);
    }

    @Bean
    public PropertyManager propertyManager() {
        return importOsgiService(PropertyManager.class);
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
    public TrustedProxyManager trustedProxyManager() {
        return importOsgiService(TrustedProxyManager.class);
    }

    @Bean
    public TypeAccessor typeAccessor() {
        return importOsgiService(TypeAccessor.class);
    }

}
