package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPermissionsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.PERMISSIONS)
@SystemAdminOnly
public class PermissionsResourceImpl extends AbstractPermissionsResourceImpl {

    @Inject
    public PermissionsResourceImpl(
            final PermissionsService permissionsService) {

        super(permissionsService);
    }

}
