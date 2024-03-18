package de.aservo.confapi.jira.service;

import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.config.properties.LnFDefaultColorProvider;
import com.atlassian.jira.config.properties.LogoProvider;
import com.atlassian.jira.config.util.JiraHome;
import com.atlassian.jira.lookandfeel.LogoChoice;
import com.atlassian.jira.lookandfeel.LookAndFeelProperties;
import com.atlassian.jira.lookandfeel.upload.UploadService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import de.aservo.confapi.commons.model.SettingsBrandingColorSchemeBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static de.aservo.confapi.jira.model.util.SettingsColourSchemeBeanUtilTest.getDummyBaseColourScheme;
import static org.junit.jupiter.api.Assertions.*;
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

    //InternalServerErrorException -> FileNotFoundException is expected because no logofile is present in the filesystem at test time
    @Test
    void testGetLogo() {
        assertThrows(InternalServerErrorException.class, () -> {
            settingsBrandingService.getLogo();
        });
    }

    @Test
    void testSetLogo() {
        InputStream is = new ByteArrayInputStream("".getBytes());

        try (MockedStatic<ComponentAccessor> componentAccessorMockedStatic = mockStatic(ComponentAccessor.class)) {
            componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(LnFDefaultColorProvider.class)).thenReturn(mock(LnFDefaultColorProvider.class));
            componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(LogoProvider.class)).thenReturn(mock(LogoProvider.class));
            componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(EventPublisher.class)).thenReturn(mock(EventPublisher.class));

            settingsBrandingService.setLogo(is);
        }

        verify(lookAndFeelProperties).setLogoChoice(LogoChoice.UPLOAD);
    }

    //InternalServerErrorException -> FileNotFoundException is expected because no logofile is present in the filesystem at test time
    @Test
    void testGetFavicon() {
        assertThrows(InternalServerErrorException.class, () -> {
            settingsBrandingService.getFavicon();
        });
    }

    @Test
    void testSetFavicon() {
        final InputStream is = new ByteArrayInputStream("".getBytes());

        try (MockedStatic<ComponentAccessor> componentAccessorMockedStatic = mockStatic(ComponentAccessor.class)) {
            componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(LnFDefaultColorProvider.class)).thenReturn(mock(LnFDefaultColorProvider.class));
            componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(LogoProvider.class)).thenReturn(mock(LogoProvider.class));
            componentAccessorMockedStatic.when(() -> ComponentAccessor.getComponent(EventPublisher.class)).thenReturn(mock(EventPublisher.class));

            settingsBrandingService.setFavicon(is);
        }

        verify(lookAndFeelProperties).setFaviconChoice(LogoChoice.UPLOAD);
    }
}
