package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPermissionsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;

import javax.ws.rs.Path;

@Path(BootstrAPI.PERMISSIONS)
@SystemAdminOnly
public class PermissionsResourceImpl extends AbstractPermissionsResourceImpl {

    public PermissionsResourceImpl(
            final PermissionsService permissionsService) {

        super(permissionsService);
    }

}
