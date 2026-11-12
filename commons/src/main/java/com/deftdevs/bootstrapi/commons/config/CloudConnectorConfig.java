package com.deftdevs.bootstrapi.commons.config;

import com.atlassian.oauth2.client.api.storage.config.ClientConfigStorageService;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.sal.api.features.DarkFeatureManager;
import com.atlassian.sal.api.user.UserManager;
import com.deftdevs.bootstrapi.commons.service.DefaultCloudConnectorServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.CloudConnectorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.importOsgiService;

/**
 * Cloud connector wiring shared by the products that support it (Jira and
 * Confluence). The products import this configuration in their app config.
 */
@Configuration
public class CloudConnectorConfig {

    @Bean
    public CloudConnectorService cloudConnectorService() {
        return new DefaultCloudConnectorServiceImpl(
                clientConfigStorageService(),
                pluginAccessor(),
                darkFeatureManager(),
                salUserManager());
    }

    @Bean
    public ClientConfigStorageService clientConfigStorageService() {
        return importOsgiService(ClientConfigStorageService.class);
    }

    @Bean
    public DarkFeatureManager darkFeatureManager() {
        return importOsgiService(DarkFeatureManager.class);
    }

    @Bean
    public PluginAccessor pluginAccessor() {
        return importOsgiService(PluginAccessor.class);
    }

    // named explicitly as Confluence also defines a bean for its own UserManager
    @Bean
    public UserManager salUserManager() {
        return importOsgiService(UserManager.class);
    }

}
