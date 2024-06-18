package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.config.properties.LnFDefaultColorProvider;
import com.atlassian.jira.config.properties.LogoProvider;
import com.atlassian.jira.config.properties.UiSettingsStateManager;
import com.atlassian.jira.config.util.JiraHome;
import com.atlassian.jira.lookandfeel.LogoChoice;
import com.atlassian.jira.lookandfeel.LookAndFeelConstants;
import com.atlassian.jira.lookandfeel.LookAndFeelProperties;
import com.atlassian.jira.lookandfeel.upload.UploadService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

import static com.deftdevs.bootstrapi.jira.model.util.SettingsColourSchemeBeanUtilTest.getDummyBaseColourScheme;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettingsBrandingServiceTest {

    private ApplicationProperties applicationProperties;
    private UploadService uploadService;
    private LookAndFeelProperties lookAndFeelProperties;
    private SettingsBrandingServiceImpl settingsBrandingService;

    @BeforeEach
    public void setup() {
        //when using powermock we cannot initialize with @Mock or @InjectMocks unfortunately
        applicationProperties = mock(ApplicationProperties.class);
        uploadService = mock(UploadService.class);
        lookAndFeelProperties = mock(LookAndFeelProperties.class);

        JiraHome jiraHome = mock(JiraHome.class);
        JiraAuthenticationContext authenticationContext = mock(JiraAuthenticationContext.class);
        PluginSettingsFactory globalSettingsFactory = mock(PluginSettingsFactory.class);

        PluginSettings pluginSettings = mock(PluginSettings.class);
        doReturn(pluginSettings).when(globalSettingsFactory).createGlobalSettings();

        settingsBrandingService = new SettingsBrandingServiceImpl(applicationProperties, authenticationContext, jiraHome, globalSettingsFactory, lookAndFeelProperties, uploadService);
    }

    @Test
    void testGetColourScheme() {

        Map<String, Object> dummyBaseColourScheme = getDummyBaseColourScheme();
        doReturn(dummyBaseColourScheme).when(applicationProperties).asMap();

        SettingsBrandingColorSchemeBean colourScheme = settingsBrandingService.getColourScheme();

        assertEquals(SettingsBrandingColorSchemeBean.EXAMPLE_1.getTopBar(), colourScheme.getTopBar());
    }

    @Test
    void testSetColourScheme() {

        SettingsBrandingColorSchemeBean schemeBean = SettingsBrandingColorSchemeBean.EXAMPLE_1;

        Map<String, Object> dummyBaseColourScheme = getDummyBaseColourScheme();
        doReturn(dummyBaseColourScheme).when(applicationProperties).asMap();

        SettingsBrandingColorSchemeBean colourScheme = settingsBrandingService.setColourScheme(schemeBean);

        assertEquals(schemeBean.getTopBar(), colourScheme.getTopBar());
    }

    @Test
    void testGetLogo() throws URISyntaxException {
        final URL logoUrl = getClass().getResource("/images/" + LookAndFeelConstants.JIRA_SCALED_LOGO_FILENAME);
        assert logoUrl != null;
        final File imagesDirectory = new File(logoUrl.toURI()).getParentFile();

        doReturn(imagesDirectory).when(uploadService).getLogoDirectory();
        assertNotNull(settingsBrandingService.getLogo());
    }

    @Test
    void testSetLogo() throws IOException {
        final File logoDirectory = new File("target/logos");
        Files.createDirectories(logoDirectory.toPath());
        doReturn(logoDirectory).when(uploadService).getLogoDirectory();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("images/" + LookAndFeelConstants.JIRA_SCALED_LOGO_FILENAME)) {
            try (MockedStatic<ComponentAccessor> componentAccessorMockedStatic = mockStatic(ComponentAccessor.class)) {
                componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(LnFDefaultColorProvider.class)).thenReturn(mock(LnFDefaultColorProvider.class));
                componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(LogoProvider.class)).thenReturn(mock(LogoProvider.class));
                componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(EventPublisher.class)).thenReturn(mock(EventPublisher.class));
                componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(UiSettingsStateManager.class)).thenReturn(mock(UiSettingsStateManager.class));

                settingsBrandingService.setLogo(is);
            }

            verify(lookAndFeelProperties).setLogoChoice(LogoChoice.UPLOAD);
        }
    }

    @Test
    void testGetFavicon() throws URISyntaxException {
        final URL faviconUrl = getClass().getResource("/images/" + LookAndFeelConstants.JIRA_SCALED_FAVICON_FILENAME);
        assert faviconUrl != null;
        final File imagesDirectory = new File(faviconUrl.toURI()).getParentFile();

        doReturn(imagesDirectory).when(uploadService).getLogoDirectory();
        assertNotNull(settingsBrandingService.getFavicon());
    }

    @Test
    void testSetFavicon() throws IOException {
        final File logoDirectory = new File("target/logos");
        Files.createDirectories(logoDirectory.toPath());
        doReturn(logoDirectory).when(uploadService).getLogoDirectory();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("images/" + LookAndFeelConstants.JIRA_SCALED_FAVICON_FILENAME)) {
            try (MockedStatic<ComponentAccessor> componentAccessorMockedStatic = mockStatic(ComponentAccessor.class)) {
                componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(LnFDefaultColorProvider.class)).thenReturn(mock(LnFDefaultColorProvider.class));
                componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(LogoProvider.class)).thenReturn(mock(LogoProvider.class));
                componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(EventPublisher.class)).thenReturn(mock(EventPublisher.class));
                componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(UiSettingsStateManager.class)).thenReturn(mock(UiSettingsStateManager.class));

                settingsBrandingService.setFavicon(is);
            }

            verify(lookAndFeelProperties).setFaviconChoice(LogoChoice.UPLOAD);
        }
    }
}
