package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPermissionsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.PERMISSIONS)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
public class PermissionsResourceImpl extends AbstractPermissionsResourceImpl {

    @Inject
    public PermissionsResourceImpl(PermissionsService permissionsService) {
        super(permissionsService);
    }

}
