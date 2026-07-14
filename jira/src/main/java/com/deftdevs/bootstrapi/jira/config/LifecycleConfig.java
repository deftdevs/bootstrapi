package com.deftdevs.bootstrapi.jira.config;

import com.atlassian.sal.api.lifecycle.LifecycleAware;
import com.deftdevs.bootstrapi.commons.startup.StartupConfigLifecycle;
import com.deftdevs.bootstrapi.jira.model._AllModel;
import org.osgi.framework.ServiceRegistration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.atlassian.plugins.osgi.javaconfig.ExportOptions.as;
import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.exportOsgiService;

@Configuration
public class LifecycleConfig {

    @Autowired
    private AtlassianConfig atlassianConfig;

    @Autowired
    private ServiceConfig serviceConfig;

    @Bean
    public StartupConfigLifecycle<_AllModel> startupConfigLifecycle() {
        return new StartupConfigLifecycle<>(
                _AllModel.class,
                serviceConfig._allService(),
                atlassianConfig.salApplicationProperties(),
                atlassianConfig.pluginSettingsFactory(),
                atlassianConfig.clusterLockService());
    }

    // SAL only invokes lifecycle callbacks on components exported as OSGi services
    @Bean
    public FactoryBean<ServiceRegistration> startupConfigLifecycleExport() {
        return exportOsgiService(startupConfigLifecycle(), as(LifecycleAware.class));
    }

}
