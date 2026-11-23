package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel;

public interface CloudTunnelService {

    /**
     * Gets the cloud application tunnel.
     *
     * @return the cloud application tunnel
     */
    CloudTunnelModel getCloudTunnel();

    /**
     * Sets the cloud application tunnel from the key of the given model, optionally
     * with an upstream URL. Applying the current key again is a no-op.
     *
     * @param cloudTunnelModel the cloud application tunnel to set
     * @return the updated cloud application tunnel
     */
    CloudTunnelModel setCloudTunnel(
            final CloudTunnelModel cloudTunnelModel);

    /**
     * Deletes the cloud application tunnel in this instance only. The tunnel in the
     * cloud is not affected, as it can only be deleted in the cloud.
     */
    void deleteCloudTunnel();

}
