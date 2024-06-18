package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.deftdevs.bootstrapi.confluence.model.PermissionAnonymousAccessBean;
import com.deftdevs.bootstrapi.confluence.rest.api.PermissionsResource;
import com.deftdevs.bootstrapi.confluence.service.api.PermissionsService;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(BootstrAPI.PERMISSIONS)
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class PermissionsResourceImpl implements PermissionsResource {

    private final PermissionsService permissionsService;

    @Inject
    public PermissionsResourceImpl(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @Override
    public Response getPermissionAnonymousAccess() {
        return Response.ok(permissionsService.getPermissionAnonymousAccess()).build();
    }

    @Override
    public Response setPermissionAnonymousAccess(
            @NotNull final PermissionAnonymousAccessBean accessBean) {
        return Response.ok(permissionsService.setPermissionAnonymousAccess(accessBean)).build();
    }
}
