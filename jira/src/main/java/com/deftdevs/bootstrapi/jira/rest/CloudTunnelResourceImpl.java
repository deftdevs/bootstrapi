package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractCloudTunnelResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.CloudTunnelService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path(BootstrAPI.CLOUD + "/" + BootstrAPI.CLOUD_TUNNEL)
@SystemAdminOnly
public class CloudTunnelResourceImpl extends AbstractCloudTunnelResourceImpl {

    @Inject
    public CloudTunnelResourceImpl(
            final CloudTunnelService cloudTunnelService) {

        super(cloudTunnelService);
    }

}
