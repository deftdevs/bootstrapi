package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import com.deftdevs.bootstrapi.commons.rest.api.PermissionsResource;
import com.deftdevs.bootstrapi.commons.service.api.PermissionsService;

import javax.ws.rs.core.Response;

public abstract class AbstractPermissionsResourceImpl implements PermissionsResource {

    private final PermissionsService permissionsService;

    public AbstractPermissionsResourceImpl(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @Override
    public Response getPermissionGlobal() {
        return Response.ok(permissionsService.getPermissionsGlobal()).build();
    }

    @Override
    public Response setPermissionGlobal(
            final PermissionsGlobalModel permissionsGlobalModel) {
        return Response.ok(permissionsService.setPermissionsGlobal(permissionsGlobalModel)).build();
    }

}
