package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.deftdevs.bootstrapi.crowd.model.SessionConfigModel;
import com.deftdevs.bootstrapi.crowd.service.api.SessionConfigService;

public class SessionConfigServiceImpl implements SessionConfigService {

    private final PropertyManager propertyManager;

    public SessionConfigServiceImpl(
            final PropertyManager propertyManager) {

        this.propertyManager = propertyManager;
    }

    @Override
    public SessionConfigModel getSessionConfig() {
        final SessionConfigModel sessionConfigModel = new SessionConfigModel();
        sessionConfigModel.setSessionTimeoutInMinutes(propertyManager.getSessionTime());
        sessionConfigModel.setRequireConsistentClientIP(propertyManager.isIncludeIpAddressInValidationFactors());

        return sessionConfigModel;
    }

    @Override
    public SessionConfigModel setSessionConfig(
            final SessionConfigModel sessionConfigModel) {

        if (sessionConfigModel.getSessionTimeoutInMinutes() != null) {
            propertyManager.setSessionTime(sessionConfigModel.getSessionTimeoutInMinutes());
        }

        if (sessionConfigModel.getRequireConsistentClientIP() != null) {
            propertyManager.setIncludeIpAddressInValidationFactors(sessionConfigModel.getRequireConsistentClientIP());
        }

        return getSessionConfig();
    }
}
