package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractCloudConnectorResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.CloudConnectorService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path(BootstrAPI.CLOUD_CONNECTOR)
@SystemAdminOnly
public class CloudConnectorResourceImpl extends AbstractCloudConnectorResourceImpl {

    @Inject
    public CloudConnectorResourceImpl(
            final CloudConnectorService cloudConnectorService) {

        super(cloudConnectorService);
    }

}
