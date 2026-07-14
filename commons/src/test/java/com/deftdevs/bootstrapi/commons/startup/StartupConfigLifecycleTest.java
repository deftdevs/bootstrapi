package com.deftdevs.bootstrapi.commons.startup;

import com.atlassian.beehive.ClusterLock;
import com.atlassian.beehive.ClusterLockService;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartupConfigLifecycleTest {

    @Mock
    private _AllService<TestAllModel> allService;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private PluginSettingsFactory pluginSettingsFactory;

    @Mock
    private ClusterLockService clusterLockService;

    @Mock
    private ClusterLock clusterLock;

    @TempDir
    private Path sharedHome;

    @TempDir
    private Path localHome;

    private final Map<String, Object> settings = new HashMap<>();

    private boolean applicationStopped;

    private StartupConfigLifecycle<TestAllModel> startupConfigLifecycle;

    @BeforeEach
    void setup() {
        lenient().when(applicationProperties.getSharedHomeDirectory()).thenReturn(Optional.of(sharedHome));
        lenient().when(applicationProperties.getLocalHomeDirectory()).thenReturn(Optional.of(localHome));
        lenient().when(clusterLockService.getLockForName(StartupConfigLifecycle.LOCK_NAME)).thenReturn(clusterLock);
        lenient().doReturn(new MapBackedPluginSettings(settings)).when(pluginSettingsFactory).createGlobalSettings();

        applicationStopped = false;
        startupConfigLifecycle = new StartupConfigLifecycle<>(
                TestAllModel.class, allService, applicationProperties, pluginSettingsFactory, clusterLockService,
                () -> applicationStopped = true);
    }

    @Test
    void testNoConfigFileDoesNothing() {
        startupConfigLifecycle.onStart();

        verify(allService, never()).setAll(any());
        assertTrue(settings.isEmpty());
        assertFalse(applicationStopped);
    }

    @Test
    void testAppliesNewConfigAndRecordsIt() throws Exception {
        writeConfig(sharedHome, "title: Example\n");
        doReturn(resultWithStatus(_AllModelStatus.success())).when(allService).setAll(any());

        startupConfigLifecycle.onStart();

        final ArgumentCaptor<TestAllModel> captor = ArgumentCaptor.forClass(TestAllModel.class);
        verify(allService).setAll(captor.capture());
        assertEquals("Example", captor.getValue().getTitle());
        assertFalse(settings.isEmpty());
        assertFalse(applicationStopped);
        verify(clusterLock).lock();
        verify(clusterLock).unlock();
    }

    @Test
    void testSkipsUnchangedConfig() throws Exception {
        writeConfig(sharedHome, "title: Example\n");
        doReturn(resultWithStatus(_AllModelStatus.success())).when(allService).setAll(any());
        startupConfigLifecycle.onStart();
        final Object recordedHash = settings.get(StartupConfigLifecycle.SETTINGS_KEY);

        startupConfigLifecycle.onStart();

        // still exactly one apply, and the recorded hash is unchanged
        verify(allService).setAll(any());
        assertEquals(recordedHash, settings.get(StartupConfigLifecycle.SETTINGS_KEY));
    }

    @Test
    void testReappliesChangedConfig() throws Exception {
        writeConfig(sharedHome, "title: Example\n");
        settings.put(StartupConfigLifecycle.SETTINGS_KEY, "0000000000000000");
        doReturn(resultWithStatus(_AllModelStatus.success())).when(allService).setAll(any());

        startupConfigLifecycle.onStart();

        verify(allService).setAll(any());
        assertFalse(settings.get(StartupConfigLifecycle.SETTINGS_KEY).equals("0000000000000000"));
    }

    @Test
    void testPartiallyFailedConfigIsNotRecordedAndStopsTheApplication() throws Exception {
        writeConfig(sharedHome, "title: Example\n");
        doReturn(resultWithStatus(_AllModelStatus.error(400, "Bad request", null))).when(allService).setAll(any());

        startupConfigLifecycle.onStart();

        verify(allService).setAll(any());
        assertTrue(settings.isEmpty());
        assertTrue(applicationStopped);
    }

    @Test
    void testPrefersSharedHomeOverLocalHome() throws Exception {
        writeConfig(sharedHome, "title: Shared\n");
        writeConfig(localHome, "title: Local\n");
        doReturn(resultWithStatus(_AllModelStatus.success())).when(allService).setAll(any());

        startupConfigLifecycle.onStart();

        final ArgumentCaptor<TestAllModel> captor = ArgumentCaptor.forClass(TestAllModel.class);
        verify(allService).setAll(captor.capture());
        assertEquals("Shared", captor.getValue().getTitle());
    }

    @Test
    void testFallsBackToLocalHome() throws Exception {
        writeConfig(localHome, "title: Local\n");
        doReturn(resultWithStatus(_AllModelStatus.success())).when(allService).setAll(any());

        startupConfigLifecycle.onStart();

        final ArgumentCaptor<TestAllModel> captor = ArgumentCaptor.forClass(TestAllModel.class);
        verify(allService).setAll(captor.capture());
        assertEquals("Local", captor.getValue().getTitle());
    }

    @Test
    void testInvalidConfigIsNotAppliedAndStopsTheApplication() throws Exception {
        writeConfig(sharedHome, "title: [unclosed\n");

        startupConfigLifecycle.onStart();

        verify(allService, never()).setAll(any());
        assertTrue(settings.isEmpty());
        assertTrue(applicationStopped);
    }

    @Test
    void testUnexpectedFailureDoesNotThrowButStopsTheApplication() throws Exception {
        writeConfig(sharedHome, "title: Example\n");
        when(clusterLockService.getLockForName(any())).thenThrow(new IllegalStateException("boom"));

        assertDoesNotThrow(() -> startupConfigLifecycle.onStart());
        assertTrue(applicationStopped);
    }

    private static void writeConfig(
            final Path home,
            final String content) throws Exception {

        Files.write(home.resolve(StartupConfigLifecycle.FILE_NAME), content.getBytes(StandardCharsets.UTF_8));
    }

    private static TestAllModel resultWithStatus(
            final _AllModelStatus status) {

        final TestAllModel result = new TestAllModel();
        final Map<String, _AllModelStatus> statusMap = new LinkedHashMap<>();
        statusMap.put("title", status);
        result.setStatus(statusMap);
        return result;
    }

    @Data
    static class TestAllModel implements _AllModelAccessor {

        private String title;

        private Map<String, _AllModelStatus> status;

    }
}
