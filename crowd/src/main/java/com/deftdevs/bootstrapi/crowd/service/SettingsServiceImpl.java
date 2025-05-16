package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import com.deftdevs.bootstrapi.crowd.service.api.CrowdSettingsGeneralService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class SettingsServiceImpl
        implements CrowdSettingsGeneralService {

    private final PropertyManager propertyManager;

    @Inject
    public SettingsServiceImpl(@ComponentImport PropertyManager propertyManager) {
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
