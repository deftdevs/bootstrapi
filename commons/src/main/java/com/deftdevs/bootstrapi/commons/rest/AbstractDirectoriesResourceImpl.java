package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.rest.api.DirectoriesResource;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import javax.ws.rs.core.Response;
import java.util.List;

public abstract class AbstractDirectoriesResourceImpl implements DirectoriesResource {

    private final DirectoriesService directoriesService;

    public AbstractDirectoriesResourceImpl(DirectoriesService directoriesService) {
        this.directoriesService = directoriesService;
    }

    @Override
    public Response getDirectories() {
        final List<AbstractDirectoryModel> directoryModels = directoriesService.getDirectories();
        return Response.ok(directoryModels).build();
    }

    @Override
    public Response setDirectories (
            final boolean testConnection,
            final List<AbstractDirectoryModel> directories) {

        List<AbstractDirectoryModel> directoryModels = directoriesService.setDirectories(directories, testConnection);
        return Response.ok(directoryModels).build();
    }

    @Override
    public Response deleteDirectories(
            final boolean force) {
        directoriesService.deleteDirectories(force);
        return Response.ok().build();
    }

}
