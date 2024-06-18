package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.SettingsBean;
import com.deftdevs.bootstrapi.commons.service.api.SettingsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@ExportAsService(SettingsService.class)
public class SettingsServiceImpl implements SettingsService {

    private final PropertyManager propertyManager;

    @Inject
    public SettingsServiceImpl(@ComponentImport PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    @Override
    public SettingsBean getSettings() {
        SettingsBean settingsBean = new SettingsBean();
        try {
            settingsBean.setBaseUrl(propertyManager.getBaseUrl());
            settingsBean.setTitle(propertyManager.getDeploymentTitle());
        } catch (PropertyManagerException e) {
            throw new InternalServerErrorException(e);
        }
        return settingsBean;
    }

    @Override
    public SettingsBean setSettings(SettingsBean settingsBean) {
        if (settingsBean.getBaseUrl() != null) {
            propertyManager.setBaseUrl(settingsBean.getBaseUrl());
        }
        if (settingsBean.getTitle() != null) {
            propertyManager.setDeploymentTitle(settingsBean.getTitle());
        }
        return getSettings();
    }

}
