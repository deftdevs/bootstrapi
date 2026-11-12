package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.CloudConnectorModel;

public interface CloudConnectorService {

    /**
     * Gets the cloud connector.
     *
     * @return the cloud connector
     */
    CloudConnectorModel getCloudConnector();

    /**
     * Sets the cloud connector from the client credentials of the given model.
     * Applying the credentials of the current connector again is a no-op.
     *
     * @param cloudConnectorModel the cloud connector to set
     * @return the updated cloud connector
     */
    CloudConnectorModel setCloudConnector(
            final CloudConnectorModel cloudConnectorModel);

    /**
     * Disables the cloud connector and revokes its credentials in the cloud.
     */
    void deleteCloudConnector();

}
