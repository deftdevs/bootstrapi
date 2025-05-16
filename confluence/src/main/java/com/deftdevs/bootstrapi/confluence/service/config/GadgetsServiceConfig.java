package com.deftdevs.bootstrapi.confluence.service.config;

import com.atlassian.confluence.languages.LocaleManager;
import com.atlassian.gadgets.directory.spi.ExternalGadgetSpecStore;
import com.atlassian.gadgets.spec.GadgetSpecFactory;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.spring.container.ContainerManager;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import com.deftdevs.bootstrapi.confluence.service.GadgetsServiceImpl;
import com.deftdevs.bootstrapi.confluence.service.unavailable.GadgetsServiceUnavailableImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GadgetsServiceConfig {

    private static final Logger log = LoggerFactory.getLogger(GadgetsServiceConfig.class);

    // IMPORTANT: Find the correct plugin key for "Gadgets for Confluence"
    // It might be "com.atlassian.confluence.plugins.gadgets" or similar.
    // Check in UPM or the plugin's atlassian-plugin.xml
    private static final String GADGETS_PLUGIN_KEY = "com.atlassian.confluence.plugins.gadgets";

    @Bean
    GadgetsService gadgetsServiceBean() {
        final ExternalGadgetSpecStore externalGadgetSpecStore = ContainerManager.getComponent("externalGadgetSpecStore", ExternalGadgetSpecStore.class);
        final GadgetSpecFactory gadgetSpecFactory = ContainerManager.getComponent("gadgetSpecFactory", GadgetSpecFactory.class);
        final LocaleManager localeManager = ContainerManager.getComponent("localeManager", LocaleManager.class);
        final PluginAccessor pluginAccessor = ContainerManager.getComponent("pluginAccessor", PluginAccessor.class);

        if (pluginAccessor.isPluginEnabled(GADGETS_PLUGIN_KEY) && externalGadgetSpecStore != null && gadgetSpecFactory != null) {
            return new GadgetsServiceImpl(externalGadgetSpecStore, gadgetSpecFactory, localeManager);
        }

        log.info("Optional plugin '{}' (Gadgets for Confluence) is not enabled. GadgetsService will be unavailable.", GADGETS_PLUGIN_KEY);
        return new GadgetsServiceUnavailableImpl();
    }

//    private boolean areGadgetPluginDependenciesAvailable() {
//
//        try {
//            // Attempt to load a few key classes. This is a good proxy for availability.
//            Class.forName("com.atlassian.gadgets.GadgetParsingException");
//            Class.forName("com.atlassian.gadgets.directory.spi.ExternalGadgetSpecStore");
//            Class.forName("com.atlassian.gadgets.spec.GadgetSpecFactory");
//            log.debug("Key classes from '{}' appear to be loadable.", GADGETS_PLUGIN_KEY);
//            return true;
//        } catch (ClassNotFoundException e) {
//            log.warn("Plugin '{}' is enabled, but essential classes (e.g., '{}') are missing. " +
//                    "This might indicate an issue with that plugin's installation, a version mismatch, or an incomplete startup. " +
//                    "GadgetsService will be unavailable.", GADGETS_PLUGIN_KEY, e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        if (areGadgetPluginDependenciesAvailable()) {
//            try {
//                if (externalGadgetSpecStore != null && gadgetSpecFactory != null) {
//                    log.info("Successfully retrieved ExternalGadgetSpecStore and GadgetSpecFactory. Initializing GadgetsServiceImpl.");
//                    this.activeGadgetService = new GadgetsServiceImpl(externalGadgetSpecStore, gadgetSpecFactory, localeManager);
//                } else {
//                    log.warn("Gadget components (ExternalGadgetSpecStore or GadgetSpecFactory) not found via ContainerManager, " +
//                            "even though plugin '{}' classes were loadable. GadgetsService will be unavailable.", GADGETS_PLUGIN_KEY);
//                    this.activeGadgetService = new GadgetsServiceUnavailableImpl();
//                }
//            } catch (Throwable t) { // Catch NoClassDefFoundError, LinkageError, etc. during component retrieval or GadgetsServiceImpl instantiation
//                log.error("Failed to initialize or wire GadgetsServiceImpl due to an unexpected error with gadget dependencies, " +
//                        "even after positive availability checks. Falling back to unavailable implementation. Error: {}", t.getMessage(), t);
//                this.activeGadgetService = new GadgetsServiceUnavailableImpl();
//            }
//        } else {
//            this.activeGadgetService = new GadgetsServiceUnavailableImpl();
//        }
//
//        if (bundleContext != null) {
//            log.info("!\n!\n!\n!\n!\nRegistering {} as {} in OSGi.!\n!\n!\n!\n",
//                    activeGadgetService.getClass().getSimpleName(), GadgetsService.class.getName());
//            serviceRegistration = bundleContext.registerService(GadgetsService.class, activeGadgetService, null);
//        } else {
//            log.error("BundleContext is null. Cannot register GadgetsService in OSGi. Service will not be available via @ComponentImport by other bundles.");
//        }
//    }
//
//    @Override
//    public void destroy() {
//        if (serviceRegistration != null) {
//            log.info("Unregistering GadgetsService from OSGi.");
//            try {
//                serviceRegistration.unregister();
//            } catch (IllegalStateException e) {
//                // This can happen if the bundle is already stopped.
//                log.warn("Could not unregister GadgetsService, OSGi service registration might already be invalid: {}", e.getMessage());
//            }
//        }
//        this.activeGadgetService = null;
//    }
}
