package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsGeneralService;

public class SettingsServiceImpl
        implements CrowdSettingsGeneralService {

    private final PropertyManager propertyManager;

    public SettingsServiceImpl(
            final PropertyManager propertyManager) {

        this.propertyManager = propertyManager;
    }

    @Override
    public SettingsModel getSettingsGeneral() {
        SettingsModel settingsModel = new SettingsModel();
        try {
            settingsModel.setBaseUrl(propertyManager.getBaseUrl());
            settingsModel.setTitle(propertyManager.getDeploymentTitle());
        } catch (PropertyManagerException e) {
            throw new InternalServerErrorException(e);
        }
        return settingsModel;
    }

    @Override
    public SettingsModel setSettingsGeneral(SettingsModel settingsModel) {
        if (settingsModel.getBaseUrl() != null) {
            propertyManager.setBaseUrl(settingsModel.getBaseUrl());
        }
        if (settingsModel.getTitle() != null) {
            propertyManager.setDeploymentTitle(settingsModel.getTitle());
        }
        return getSettingsGeneral();
    }

}
