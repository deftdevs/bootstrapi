package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.PluginArtifact;
import com.atlassian.plugin.PluginController;
import com.atlassian.plugin.PluginInformation;
import com.deftdevs.bootstrapi.commons.model.PluginModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.plugins.PluginArtifactDownloader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class DefaultUpmServiceImplTest {

    private static final String PLUGIN_KEY = "com.example.plugin";

    @Mock
    private PluginAccessor pluginAccessor;

    @Mock
    private PluginController pluginController;

    @Mock
    private PluginArtifactDownloader pluginArtifactDownloader;

    @InjectMocks
    private DefaultUpmServiceImpl upmService;

    @Test
    void testGetPluginsReturnsSortedPlugins() {
        doReturn(List.of(plugin("com.example.b", "2.0.0"), plugin("com.example.a", "1.0.0")))
                .when(pluginAccessor).getPlugins();
        doReturn(true).when(pluginAccessor).isPluginEnabled("com.example.a");
        doReturn(false).when(pluginAccessor).isPluginEnabled("com.example.b");

        final Map<String, PluginModel> plugins = upmService.getPlugins();

        assertEquals(List.of("com.example.a", "com.example.b"), List.copyOf(plugins.keySet()));
        assertEquals("1.0.0", plugins.get("com.example.a").getVersion());
        assertTrue(plugins.get("com.example.a").getEnabled());
        assertFalse(plugins.get("com.example.b").getEnabled());
    }

    @Test
    void testSetUpmInstallsAndEnablesAbsentPlugin() throws IOException {
        final Path artifact = Files.createTempFile("bootstrapi-plugin-test-", ".jar");
        final UpmModel upmModel = upmModel(pluginModel());
        doReturn(null, plugin(PLUGIN_KEY, "1.2.3")).when(pluginAccessor).getPlugin(PLUGIN_KEY);
        doReturn(artifact).when(pluginArtifactDownloader)
                .download(upmModel, PLUGIN_KEY, upmModel.getPlugins().get(PLUGIN_KEY));
        doReturn(Set.of(PLUGIN_KEY)).when(pluginController).installPlugins(any(PluginArtifact.class));
        doReturn(true).when(pluginAccessor).isPluginEnabled(PLUGIN_KEY);

        final ServiceResult<UpmModel> result = upmService.setUpm(upmModel);

        verify(pluginController).installPlugins(any(PluginArtifact.class));
        verify(pluginController).enablePlugins(PLUGIN_KEY);
        assertEquals(200, result.getStatus().get(PLUGIN_KEY).getStatus());
        assertEquals("1.2.3", result.getModel().getPlugins().get(PLUGIN_KEY).getVersion());
        assertTrue(result.getModel().getPlugins().get(PLUGIN_KEY).getEnabled());
        // the resolver endpoints and their credentials are not echoed
        assertNull(result.getModel().getResolvers());
        assertFalse(Files.exists(artifact), "the downloaded artifact must be deleted after the install");
    }

    @Test
    void testSetUpmSkipsInstallWhenVersionMatches() {
        final UpmModel upmModel = upmModel(pluginModel());
        doReturn(plugin(PLUGIN_KEY, "1.2.3")).when(pluginAccessor).getPlugin(PLUGIN_KEY);
        doReturn(true).when(pluginAccessor).isPluginEnabled(PLUGIN_KEY);

        final ServiceResult<UpmModel> result = upmService.setUpm(upmModel);

        verifyNoInteractions(pluginArtifactDownloader);
        verify(pluginController, never()).installPlugins(any(PluginArtifact.class));
        verify(pluginController).enablePlugins(PLUGIN_KEY);
        assertEquals(200, result.getStatus().get(PLUGIN_KEY).getStatus());
    }

    @Test
    void testSetUpmDisablesPluginWhenRequested() {
        final PluginModel pluginModel = pluginModel();
        pluginModel.setEnabled(false);
        final UpmModel upmModel = upmModel(pluginModel);
        doReturn(plugin(PLUGIN_KEY, "1.2.3")).when(pluginAccessor).getPlugin(PLUGIN_KEY);
        doReturn(false).when(pluginAccessor).isPluginEnabled(PLUGIN_KEY);

        final ServiceResult<UpmModel> result = upmService.setUpm(upmModel);

        verify(pluginController).disablePlugin(PLUGIN_KEY);
        verify(pluginController, never()).enablePlugins(PLUGIN_KEY);
        assertFalse(result.getModel().getPlugins().get(PLUGIN_KEY).getEnabled());
    }

    @Test
    void testSetUpmWithoutResolverFailsThePlugin() {
        final PluginModel pluginModel = pluginModel();
        pluginModel.setResolver(null);

        final ServiceResult<UpmModel> result = upmService.setUpm(upmModel(pluginModel));

        assertEquals(400, result.getStatus().get(PLUGIN_KEY).getStatus());
        assertTrue(result.getModel().getPlugins().isEmpty());
        verifyNoInteractions(pluginController, pluginArtifactDownloader);
    }

    @Test
    void testSetUpmWithEmptyPluginEntryFailsThePlugin() {
        final UpmModel upmModel = new UpmModel();
        final Map<String, PluginModel> plugins = new LinkedHashMap<>();
        plugins.put(PLUGIN_KEY, null);
        upmModel.setPlugins(plugins);

        final ServiceResult<UpmModel> result = upmService.setUpm(upmModel);

        assertEquals(400, result.getStatus().get(PLUGIN_KEY).getStatus());
        verifyNoInteractions(pluginAccessor, pluginController, pluginArtifactDownloader);
    }

    @Test
    void testSetUpmWithKeyMismatchFailsThePlugin() throws IOException {
        final Path artifact = Files.createTempFile("bootstrapi-plugin-test-", ".jar");
        final UpmModel upmModel = upmModel(pluginModel());
        doReturn(null).when(pluginAccessor).getPlugin(PLUGIN_KEY);
        doReturn(artifact).when(pluginArtifactDownloader)
                .download(upmModel, PLUGIN_KEY, upmModel.getPlugins().get(PLUGIN_KEY));
        doReturn(Set.of("com.example.other")).when(pluginController).installPlugins(any(PluginArtifact.class));

        final ServiceResult<UpmModel> result = upmService.setUpm(upmModel);

        assertEquals(400, result.getStatus().get(PLUGIN_KEY).getStatus());
        assertFalse(Files.exists(artifact), "the downloaded artifact must be deleted after a failed install");
    }

    @Test
    void testSetUpmContinuesAfterAFailedPlugin() {
        final PluginModel invalidModel = PluginModel.builder()
                .resolver("central")
                .build();
        final UpmModel upmModel = new UpmModel();
        final Map<String, PluginModel> plugins = new LinkedHashMap<>();
        plugins.put("com.example.invalid", invalidModel);
        plugins.put(PLUGIN_KEY, pluginModel());
        upmModel.setPlugins(plugins);
        doReturn(plugin(PLUGIN_KEY, "1.2.3")).when(pluginAccessor).getPlugin(PLUGIN_KEY);
        doReturn(true).when(pluginAccessor).isPluginEnabled(PLUGIN_KEY);

        final ServiceResult<UpmModel> result = upmService.setUpm(upmModel);

        assertEquals(400, result.getStatus().get("com.example.invalid").getStatus());
        assertEquals(200, result.getStatus().get(PLUGIN_KEY).getStatus());
        assertEquals(1, result.getModel().getPlugins().size());
    }

    @Test
    void testSetUpmWithoutPluginsDoesNothing() {
        final ServiceResult<UpmModel> result = upmService.setUpm(new UpmModel());

        assertTrue(result.getStatus().isEmpty());
        verifyNoInteractions(pluginAccessor, pluginController, pluginArtifactDownloader);
    }

    private static UpmModel upmModel(
            final PluginModel pluginModel) {

        return UpmModel.builder()
                .plugins(Map.of(PLUGIN_KEY, pluginModel))
                .build();
    }

    private static PluginModel pluginModel() {
        return PluginModel.builder()
                .version("1.2.3")
                .resolver("central")
                .build();
    }

    private static Plugin plugin(
            final String key,
            final String version) {

        final PluginInformation pluginInformation = mock(PluginInformation.class);
        lenient().doReturn(version).when(pluginInformation).getVersion();
        final Plugin plugin = mock(Plugin.class);
        lenient().doReturn(key).when(plugin).getKey();
        lenient().doReturn(pluginInformation).when(plugin).getPluginInformation();
        return plugin;
    }
}
