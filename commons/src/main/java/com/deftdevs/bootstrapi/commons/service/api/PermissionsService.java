package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import com.deftdevs.bootstrapi.commons.model.PermissionsModel;
import com.deftdevs.bootstrapi.commons.model.type.ServiceResult;
import com.deftdevs.bootstrapi.commons.model.type._AllModelStatus;
import com.deftdevs.bootstrapi.commons.util.ServiceResultUtil;

import java.util.LinkedHashMap;
import java.util.Map;


public interface PermissionsService {

    default ServiceResult<PermissionsModel> setPermissions(final PermissionsModel permissionsModel) {
        final PermissionsModel result = new PermissionsModel();
        final Map<String, _AllModelStatus> status = new LinkedHashMap<>();

        ServiceResultUtil.setSubEntity(status, PermissionsGlobalModel.class, permissionsModel.getGlobal(),
                this::setPermissionsGlobal, result::setGlobal);

        return new ServiceResult<>(result, status);
    }

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
