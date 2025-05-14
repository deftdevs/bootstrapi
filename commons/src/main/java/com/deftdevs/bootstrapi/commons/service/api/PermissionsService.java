package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalBean;

public interface PermissionsService {

    /**
     * Get global permissions.
     *
     * @return global permissions
     */
    PermissionsGlobalBean getPermissionsGlobal();

    /**
     * Set global permissions.
     *
     * @param permissionsGlobalBean global permissions to set
     * @return global permissions
     */
    PermissionsGlobalBean setPermissionsGlobal(
            PermissionsGlobalBean permissionsGlobalBean);

}
