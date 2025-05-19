package com.deftdevs.bootstrapi.jira.config;

import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.config.util.JiraHome;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.lookandfeel.LookAndFeelProperties;
import com.atlassian.jira.lookandfeel.upload.UploadService;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.oauth.consumer.ConsumerService;
import com.atlassian.oauth.consumer.ConsumerTokenStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import com.atlassian.plugins.authentication.api.config.IdpConfigService;
import com.atlassian.plugins.authentication.api.config.SsoConfigService;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.user.UserManager;
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
    public GlobalPermissionManager globalPermissionManager() {
        return importOsgiService(GlobalPermissionManager.class);
    }

    @Bean
    public IdpConfigService idpConfigService() {
        return importOsgiService(IdpConfigService.class);
    }

    @Bean
    public JiraAuthenticationContext jiraAuthenticationContext() {
        return importOsgiService(JiraAuthenticationContext.class);
    }

    @Bean
    public JiraHome jiraHome() {
        return importOsgiService(JiraHome.class);
    }

    @Bean
    public JiraLicenseManager jiraLicenseManager() {
        return importOsgiService(JiraLicenseManager.class);
    }

    @Bean
    public LookAndFeelProperties lookAndFeelProperties() {
        return importOsgiService(LookAndFeelProperties.class);
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
    public PluginSettingsFactory pluginSettingsFactory() {
        return importOsgiService(PluginSettingsFactory.class);
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
    public SsoConfigService ssoConfigService() {
        return importOsgiService(SsoConfigService.class);
    }

    @Bean
    public TypeAccessor typeAccessor() {
        return importOsgiService(TypeAccessor.class);
    }

    @Bean
    public UploadService uploadService() {
        return importOsgiService(UploadService.class);
    }

    @Bean
    public UserManager userManager() {
        return importOsgiService(UserManager.class);
    }

}
