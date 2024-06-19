package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalBean;

import javax.validation.constraints.NotNull;

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
            @NotNull PermissionsGlobalBean permissionsGlobalBean);

}
