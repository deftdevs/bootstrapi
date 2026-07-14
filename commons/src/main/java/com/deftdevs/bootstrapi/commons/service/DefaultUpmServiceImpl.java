package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.plugin.JarPluginArtifact;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.PluginController;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.PluginModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.plugins.PluginArtifactDownloader;
import com.deftdevs.bootstrapi.commons.service.api.UpmService;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;

import jakarta.ws.rs.core.Response;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Installs plugins through the plugin framework itself
 * ({@link PluginController}), the same layer the UPM builds on, so no HTTP
 * round-trip, UPM token or admin credentials are involved. The
 * implementation is product-independent because the plugin framework API is
 * shared by all products.
 */
public class DefaultUpmServiceImpl implements UpmService {

    private final PluginAccessor pluginAccessor;
    private final PluginController pluginController;
    private final PluginArtifactDownloader pluginArtifactDownloader;

    public DefaultUpmServiceImpl(
            final PluginAccessor pluginAccessor,
            final PluginController pluginController) {

        this(pluginAccessor, pluginController, new PluginArtifactDownloader());
    }

    DefaultUpmServiceImpl(
            final PluginAccessor pluginAccessor,
            final PluginController pluginController,
            final PluginArtifactDownloader pluginArtifactDownloader) {

        this.pluginAccessor = pluginAccessor;
        this.pluginController = pluginController;
        this.pluginArtifactDownloader = pluginArtifactDownloader;
    }

    @Override
    public Map<String, PluginModel> getPlugins() {
        final Map<String, PluginModel> pluginModels = new TreeMap<>();
        for (final Plugin plugin : pluginAccessor.getPlugins()) {
            pluginModels.put(plugin.getKey(), PluginModel.builder()
                    .version(plugin.getPluginInformation().getVersion())
                    .enabled(pluginAccessor.isPluginEnabled(plugin.getKey()))
                    .build());
        }
        return pluginModels;
    }

    @Override
    public ServiceResult<UpmModel> setUpm(
            final UpmModel upmModel) {

        final Map<String, PluginModel> results = new LinkedHashMap<>();
        final Map<String, _AllModelStatus> statusMap = new LinkedHashMap<>();

        final Map<String, PluginModel> pluginModels = upmModel.getPlugins() != null
                ? upmModel.getPlugins()
                : Map.of();
        for (final Map.Entry<String, PluginModel> entry : pluginModels.entrySet()) {
            final String pluginKey = entry.getKey();
            if (entry.getValue() == null) {
                // setEntity would silently skip a null input, but an empty
                // plugin entry is a mistake and must be reported
                statusMap.put(pluginKey, _AllModelStatus.error(Response.Status.BAD_REQUEST,
                        "Failed to apply " + pluginKey + " configuration",
                        "The plugin entry must provide a version and a resolver"));
                continue;
            }
            ServiceResultUtil.setEntity(statusMap, pluginKey, entry.getValue(),
                    pluginModel -> applyPlugin(upmModel, pluginKey, pluginModel),
                    pluginModel -> results.put(pluginKey, pluginModel));
        }

        // only the plugins map is echoed, never the resolver credentials
        return new ServiceResult<>(UpmModel.builder().plugins(results).build(), statusMap);
    }

    private PluginModel applyPlugin(
            final UpmModel upmModel,
            final String pluginKey,
            final PluginModel pluginModel) {

        validate(pluginKey, pluginModel);

        final Plugin existingPlugin = pluginAccessor.getPlugin(pluginKey);
        if (existingPlugin == null
                || !pluginModel.getVersion().equals(existingPlugin.getPluginInformation().getVersion())) {
            installPlugin(upmModel, pluginKey, pluginModel);
        }

        // absent means enabled, so a minimal entry yields an active plugin
        if (pluginModel.getEnabled() == null || pluginModel.getEnabled()) {
            pluginController.enablePlugins(pluginKey);
        } else {
            pluginController.disablePlugin(pluginKey);
        }

        final Plugin plugin = pluginAccessor.getPlugin(pluginKey);
        return PluginModel.builder()
                .version(plugin.getPluginInformation().getVersion())
                .resolver(pluginModel.getResolver())
                .enabled(pluginAccessor.isPluginEnabled(pluginKey))
                .build();
    }

    private void installPlugin(
            final UpmModel upmModel,
            final String pluginKey,
            final PluginModel pluginModel) {

        final Path artifactFile = pluginArtifactDownloader.download(upmModel, pluginKey, pluginModel);
        try {
            final Set<String> installedKeys =
                    pluginController.installPlugins(new JarPluginArtifact(artifactFile.toFile()));
            if (!installedKeys.contains(pluginKey)) {
                throw new BadRequestException("The resolved artifact installed the plugin keys "
                        + installedKeys + " instead of '" + pluginKey + "'");
            }
        } finally {
            PluginArtifactDownloader.deleteArtifact(artifactFile);
        }
    }

    private static void validate(
            final String pluginKey,
            final PluginModel pluginModel) {

        if (isBlank(pluginModel.getVersion())) {
            throw new BadRequestException("Plugin '" + pluginKey + "' must provide a version");
        }
        if (isBlank(pluginModel.getResolver())) {
            throw new BadRequestException("Plugin '" + pluginKey
                    + "' must name its resolver explicitly");
        }
    }

    private static boolean isBlank(
            final String value) {

        return value == null || value.isBlank();
    }
}
