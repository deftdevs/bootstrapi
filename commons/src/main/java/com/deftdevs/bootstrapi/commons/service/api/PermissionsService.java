package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;


public interface PermissionsService {

    /**
     * Get global permissions.
     *
     * @return global permissions
     */
    PermissionsGlobalModel getPermissionsGlobal();

    /**
     * Set global permissions.
     *
     * @param permissionsGlobalModel global permissions to set
     * @return global permissions
     */
    PermissionsGlobalModel setPermissionsGlobal(
            PermissionsGlobalModel permissionsGlobalModel);

}
