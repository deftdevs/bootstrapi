package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsGeneralModel;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsBrandingService;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsService;

import java.io.InputStream;

public class SettingsServiceImpl implements CrowdSettingsService {

    private final PropertyManager propertyManager;
    private final CrowdSettingsBrandingService settingsBrandingService;

    public SettingsServiceImpl(
            final PropertyManager propertyManager,
            final CrowdSettingsBrandingService settingsBrandingService) {

        this.propertyManager = propertyManager;
        this.settingsBrandingService = settingsBrandingService;
    }

    @Override
    public SettingsGeneralModel getSettingsGeneral() {
        try {
            return SettingsGeneralModel.builder()
                    .baseUrl(propertyManager.getBaseUrl())
                    .title(propertyManager.getDeploymentTitle())
                    .build();
        } catch (PropertyManagerException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public SettingsGeneralModel setSettingsGeneral(SettingsGeneralModel settingsModel) {
        if (settingsModel.getBaseUrl() != null) {
            propertyManager.setBaseUrl(settingsModel.getBaseUrl());
        }
        if (settingsModel.getTitle() != null) {
            propertyManager.setDeploymentTitle(settingsModel.getTitle());
        }
        return getSettingsGeneral();
    }

    @Override
    public SettingsBrandingLoginPageModel getLoginPage() {
        return settingsBrandingService.getLoginPage();
    }

    @Override
    public SettingsBrandingLoginPageModel setLoginPage(final SettingsBrandingLoginPageModel settingsBrandingLoginPageModel) {
        return settingsBrandingService.setLoginPage(settingsBrandingLoginPageModel);
    }

    @Override
    public void setLogo(final InputStream inputStream) {
        settingsBrandingService.setLogo(inputStream);
    }

}
