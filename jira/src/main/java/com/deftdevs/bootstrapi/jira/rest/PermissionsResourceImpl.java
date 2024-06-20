package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractPermissionsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.PERMISSIONS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class PermissionsResourceImpl extends AbstractPermissionsResourceImpl {

    @Inject
    public PermissionsResourceImpl(PermissionsService permissionsService) {
        super(permissionsService);
    }

}
