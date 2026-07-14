package com.deftdevs.bootstrapi.commons.startup;

import com.atlassian.beehive.ClusterLock;
import com.atlassian.beehive.ClusterLockService;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.lifecycle.LifecycleAware;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.rest.provider.YamlObjectMapperHolder;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Applies the configuration from a {@code bootstrapi.yaml} file in the
 * application home directory when the application has started.
 * <p>
 * The file is looked up in the shared home first (the right place for a
 * cluster) and in the local home as a fallback. The configuration document
 * has the same structure as the body of the {@code _all} endpoint.
 * <p>
 * The apply is cluster-safe: a cluster lock guarantees that only one node
 * applies at a time, and the SHA-256 hash of the last successfully applied
 * document is recorded in the (database-backed) plugin settings, so other
 * replicas and later restarts skip an unchanged file.
 * <p>
 * A configuration that cannot be read, parsed or fully applied stops the
 * application: an instance must not come up with a configuration it could
 * not reach. Since the hash is only recorded on success, the apply is
 * retried when the instance is started again.
 */
public class StartupConfigLifecycle<_AllModel extends _AllModelAccessor> implements LifecycleAware {

    public static final String FILE_NAME = "bootstrapi.yaml";

    static final String LOCK_NAME = "com.deftdevs.bootstrapi.startup-config";
    static final String SETTINGS_KEY = "com.deftdevs.bootstrapi.startup-config.sha256";

    private static final Logger log = LoggerFactory.getLogger(StartupConfigLifecycle.class);

    private final Class<_AllModel> allModelClass;
    private final _AllService<_AllModel> allService;
    private final ApplicationProperties applicationProperties;
    private final PluginSettingsFactory pluginSettingsFactory;
    private final ClusterLockService clusterLockService;
    private final Runnable stopApplication;

    public StartupConfigLifecycle(
            final Class<_AllModel> allModelClass,
            final _AllService<_AllModel> allService,
            final ApplicationProperties applicationProperties,
            final PluginSettingsFactory pluginSettingsFactory,
            final ClusterLockService clusterLockService) {

        this(allModelClass, allService, applicationProperties, pluginSettingsFactory, clusterLockService,
                () -> System.exit(1));
    }

    StartupConfigLifecycle(
            final Class<_AllModel> allModelClass,
            final _AllService<_AllModel> allService,
            final ApplicationProperties applicationProperties,
            final PluginSettingsFactory pluginSettingsFactory,
            final ClusterLockService clusterLockService,
            final Runnable stopApplication) {

        this.allModelClass = allModelClass;
        this.allService = allService;
        this.applicationProperties = applicationProperties;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.clusterLockService = clusterLockService;
        this.stopApplication = stopApplication;
    }

    @Override
    public void onStart() {
        boolean applied;
        try {
            applied = applyStartupConfig();
        } catch (RuntimeException e) {
            log.error("Failed to apply the startup configuration", e);
            applied = false;
        }

        if (!applied) {
            log.error("Stopping the application: an instance must not come up"
                    + " with a startup configuration it could not apply");
            stopApplication.run();
        }
    }

    boolean applyStartupConfig() {
        final Path configFile = findConfigFile();
        if (configFile == null) {
            log.debug("No {} found in the application home, skipping the startup configuration", FILE_NAME);
            return true;
        }

        final byte[] configBytes;
        try {
            configBytes = Files.readAllBytes(configFile);
        } catch (IOException e) {
            log.error("Failed to read the startup configuration {}", configFile, e);
            return false;
        }

        final String configHash = sha256Hex(configBytes);
        final ClusterLock lock = clusterLockService.getLockForName(LOCK_NAME);
        lock.lock();
        try {
            final PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
            if (configHash.equals(pluginSettings.get(SETTINGS_KEY))) {
                log.info("The startup configuration {} is unchanged and already applied, skipping", configFile);
                return true;
            }

            final _AllModel allModel;
            try {
                allModel = YamlObjectMapperHolder.YAML_OBJECT_MAPPER.readValue(configBytes, allModelClass);
            } catch (IOException e) {
                log.error("Failed to parse the startup configuration {}", configFile, e);
                return false;
            }

            log.info("Applying the startup configuration {}", configFile);
            final _AllModel result = allService.setAll(allModel);
            if (!isAppliedSuccessfully(configFile, result.getStatus())) {
                log.error("The startup configuration {} could not be applied completely;"
                        + " nothing was recorded, so the apply is retried on the next startup", configFile);
                return false;
            }

            log.info("Successfully applied the startup configuration {}", configFile);
            pluginSettings.put(SETTINGS_KEY, configHash);
            return true;
        } finally {
            lock.unlock();
        }
    }

    private Path findConfigFile() {
        final Set<Path> candidates = new LinkedHashSet<>();
        applicationProperties.getSharedHomeDirectory().ifPresent(home -> candidates.add(home.resolve(FILE_NAME)));
        applicationProperties.getLocalHomeDirectory().ifPresent(home -> candidates.add(home.resolve(FILE_NAME)));
        return candidates.stream()
                .filter(Files::isRegularFile)
                .findFirst()
                .orElse(null);
    }

    private boolean isAppliedSuccessfully(
            final Path configFile,
            final Map<String, _AllModelStatus> statusMap) {

        boolean success = true;
        if (statusMap != null) {
            for (final Map.Entry<String, _AllModelStatus> entry : statusMap.entrySet()) {
                final _AllModelStatus status = entry.getValue();
                if (status.getStatus() >= 400) {
                    success = false;
                    log.error("Startup configuration {}: applying '{}' failed with status {}: {} {}",
                            configFile, entry.getKey(), status.getStatus(), status.getMessage(),
                            status.getDetails() != null ? status.getDetails() : "");
                }
            }
        }
        return success;
    }

    private static String sha256Hex(
            final byte[] bytes) {

        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available on every JVM
            throw new IllegalStateException(e);
        }

        final StringBuilder hex = new StringBuilder();
        for (final byte b : digest.digest(bytes)) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
