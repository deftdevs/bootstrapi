package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.atlassian.crowd.model.lookandfeel.LookAndFeelConfiguration;
import com.atlassian.crowd.util.ImageInfo;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CrowdSettingsBrandingServiceTest {

    @Mock
    private PropertyManager propertyManager;

    private SettingsBrandingServiceImpl settingsBrandingService;

    @BeforeEach
    public void setup() {
        settingsBrandingService = new SettingsBrandingServiceImpl(propertyManager);
    }

    @Test
    public void getLoginPage() throws PropertyManagerException {

        LookAndFeelConfiguration lookAndFeelConfiguration = getLookAndFeelConfiguration(SettingsBrandingLoginPageModel.EXAMPLE_1);
        Optional<LookAndFeelConfiguration> lookAndFeelConfigurationOptional = Optional.of(lookAndFeelConfiguration);
        doReturn(lookAndFeelConfigurationOptional).when(propertyManager).getLookAndFeelConfiguration();

        SettingsBrandingLoginPageModel result = settingsBrandingService.getLoginPage();

        assertEquals(lookAndFeelConfiguration.getHeader(), result.getHeader());
        assertEquals(lookAndFeelConfiguration.getWelcomeText(), result.getContent());
        assertEquals(lookAndFeelConfiguration.getPrimaryColor(), result.getButtonColor());
        assertEquals(lookAndFeelConfiguration.isShowLogo(), result.getShowLogo());
    }

    @Test
    public void getLoginPageDefaultConfig() throws PropertyManagerException {
        doReturn(Optional.empty()).when(propertyManager).getLookAndFeelConfiguration();

        assertNotNull(settingsBrandingService.getLoginPage());
    }

    @Test
    public void getLoginPageInternalServerErrorException() throws PropertyManagerException {
        doThrow(new PropertyManagerException()).when(propertyManager).getLookAndFeelConfiguration();

        assertThrows(InternalServerErrorException.class, () -> {
            settingsBrandingService.getLoginPage();
        });
    }

    @Test
    public void setLoginPage() throws PropertyManagerException {

        SettingsBrandingLoginPageModel settingsBrandingLoginPageModel = SettingsBrandingLoginPageModel.EXAMPLE_2;

        LookAndFeelConfiguration lookAndFeelConfiguration = getLookAndFeelConfiguration(SettingsBrandingLoginPageModel.EXAMPLE_1);
        Optional<LookAndFeelConfiguration> lookAndFeelConfigurationOptional = Optional.of(lookAndFeelConfiguration);
        doReturn(lookAndFeelConfigurationOptional).when(propertyManager).getLookAndFeelConfiguration();

        settingsBrandingService.setLoginPage(settingsBrandingLoginPageModel);

        final ArgumentCaptor<LookAndFeelConfiguration> captor = ArgumentCaptor.forClass(LookAndFeelConfiguration.class);
        verify(propertyManager).setLookAndFeelConfiguration(captor.capture(), any());
        LookAndFeelConfiguration captorValue = captor.getValue();

        assertEquals(settingsBrandingLoginPageModel.getHeader(), captorValue.getHeader());
        assertEquals(settingsBrandingLoginPageModel.getContent(), captorValue.getWelcomeText());
        assertEquals(settingsBrandingLoginPageModel.getButtonColor(), captorValue.getPrimaryColor());
        assertEquals(settingsBrandingLoginPageModel.getShowLogo(), captorValue.isShowLogo());
    }

    @Test
    public void setLoginPageDefaultConfig() throws PropertyManagerException {

        SettingsBrandingLoginPageModel settingsBrandingLoginPageModel = new SettingsBrandingLoginPageModel();

        LookAndFeelConfiguration lookAndFeelConfiguration = getLookAndFeelConfiguration(SettingsBrandingLoginPageModel.EXAMPLE_1);
        Optional<LookAndFeelConfiguration> lookAndFeelConfigurationOptional = Optional.of(lookAndFeelConfiguration);
        doReturn(lookAndFeelConfigurationOptional).when(propertyManager).getLookAndFeelConfiguration();

        settingsBrandingService.setLoginPage(settingsBrandingLoginPageModel);

        final ArgumentCaptor<LookAndFeelConfiguration> captor = ArgumentCaptor.forClass(LookAndFeelConfiguration.class);
        verify(propertyManager).setLookAndFeelConfiguration(captor.capture(), any());
        LookAndFeelConfiguration captorValue = captor.getValue();

        assertEquals(lookAndFeelConfiguration.getHeader(), captorValue.getHeader());
        assertEquals(lookAndFeelConfiguration.getWelcomeText(), captorValue.getWelcomeText());
        assertEquals(lookAndFeelConfiguration.getPrimaryColor(), captorValue.getPrimaryColor());
        assertEquals(lookAndFeelConfiguration.isShowLogo(), captorValue.isShowLogo());
    }

    @Test
    public void setLoginPageBadRequestException() throws PropertyManagerException {

        SettingsBrandingLoginPageModel settingsBrandingLoginPageModel = new SettingsBrandingLoginPageModel();

        LookAndFeelConfiguration lookAndFeelConfiguration = getLookAndFeelConfiguration(SettingsBrandingLoginPageModel.EXAMPLE_2);
        Optional<LookAndFeelConfiguration> lookAndFeelConfigurationOptional = Optional.of(lookAndFeelConfiguration);
        doReturn(lookAndFeelConfigurationOptional).when(propertyManager).getLookAndFeelConfiguration();

        doThrow(new PropertyManagerException()).when(propertyManager).setLookAndFeelConfiguration(any(LookAndFeelConfiguration.class), any());


        assertThrows(BadRequestException.class, () -> {
            settingsBrandingService.setLoginPage(settingsBrandingLoginPageModel);
        });
    }

    @Test
    public void setLogo() throws PropertyManagerException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/deftdevs.png");

        LookAndFeelConfiguration lookAndFeelConfiguration = LookAndFeelConfiguration.builder().build();
        Optional<LookAndFeelConfiguration> lookAndFeelConfigurationOptional = Optional.of(lookAndFeelConfiguration);
        doReturn(lookAndFeelConfigurationOptional).when(propertyManager).getLookAndFeelConfiguration();

        settingsBrandingService.setLogo(inputStream);
        verify(propertyManager).setLookAndFeelConfiguration(any(LookAndFeelConfiguration.class), any(ImageInfo.class));
    }

    @Test
    public void setLogoBadRequestException() throws PropertyManagerException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("images/deftdevs.png");

        LookAndFeelConfiguration lookAndFeelConfiguration = LookAndFeelConfiguration.builder().build();
        Optional<LookAndFeelConfiguration> lookAndFeelConfigurationOptional = Optional.of(lookAndFeelConfiguration);
        doReturn(lookAndFeelConfigurationOptional).when(propertyManager).getLookAndFeelConfiguration();

        doThrow(new PropertyManagerException()).when(propertyManager).setLookAndFeelConfiguration(any(LookAndFeelConfiguration.class), any(ImageInfo.class));

        assertThrows(BadRequestException.class, () -> {
            settingsBrandingService.setLogo(inputStream);
        });
    }

    private LookAndFeelConfiguration getLookAndFeelConfiguration(SettingsBrandingLoginPageModel bean) {

        LookAndFeelConfiguration.Builder lookAndFeelConfiguration = LookAndFeelConfiguration.builder();
        if (bean != null) {
            lookAndFeelConfiguration
                    .setHeader(bean.getHeader())
                    .setWelcomeText(bean.getContent())
                    .setPrimaryColor(bean.getButtonColor())
                    .setShowLogo(bean.getShowLogo());
        }
        return lookAndFeelConfiguration.build();
    }
}