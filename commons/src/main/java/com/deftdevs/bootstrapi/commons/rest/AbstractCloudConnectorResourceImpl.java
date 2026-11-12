package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.CloudConnectorModel;
import com.deftdevs.bootstrapi.commons.rest.api.CloudConnectorResource;
import com.deftdevs.bootstrapi.commons.service.api.CloudConnectorService;

public abstract class AbstractCloudConnectorResourceImpl implements CloudConnectorResource {

    private final CloudConnectorService cloudConnectorService;

    protected AbstractCloudConnectorResourceImpl(
            final CloudConnectorService cloudConnectorService) {

        this.cloudConnectorService = cloudConnectorService;
    }

    @Override
    public CloudConnectorModel getCloudConnector() {
        return cloudConnectorService.getCloudConnector();
    }

    @Override
    public CloudConnectorModel setCloudConnector(
            final CloudConnectorModel cloudConnectorModel) {

        return cloudConnectorService.setCloudConnector(cloudConnectorModel);
    }

    @Override
    public void deleteCloudConnector() {
        cloudConnectorService.deleteCloudConnector();
    }

}
