package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel;
import com.deftdevs.bootstrapi.commons.rest.api.CloudTunnelResource;
import com.deftdevs.bootstrapi.commons.service.api.CloudTunnelService;

public abstract class AbstractCloudTunnelResourceImpl implements CloudTunnelResource {

    private final CloudTunnelService cloudTunnelService;

    protected AbstractCloudTunnelResourceImpl(
            final CloudTunnelService cloudTunnelService) {

        this.cloudTunnelService = cloudTunnelService;
    }

    @Override
    public CloudTunnelModel getCloudTunnel() {
        return cloudTunnelService.getCloudTunnel();
    }

    @Override
    public CloudTunnelModel setCloudTunnel(
            final CloudTunnelModel cloudTunnelModel) {

        return cloudTunnelService.setCloudTunnel(cloudTunnelModel);
    }

    @Override
    public void deleteCloudTunnel() {
        cloudTunnelService.deleteCloudTunnel();
    }

}
