package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.rest.api.DirectoryResource;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import javax.ws.rs.core.Response;

public abstract class AbstractDirectoryResourceImpl implements DirectoryResource {

    private final DirectoriesService directoriesService;

    public AbstractDirectoryResourceImpl(DirectoriesService directoriesService) {
        this.directoriesService = directoriesService;
    }

    @Override
    public Response getDirectory(
            final long id) {
        final AbstractDirectoryModel directoryModel = directoriesService.getDirectory(id);
        return Response.ok(directoryModel).build();
    }

    @Override
    public Response updateDirectory(
            final long id,
            final boolean testConnection,
            final AbstractDirectoryModel directory) {

        AbstractDirectoryModel resultDirectoryModel  = directoriesService.setDirectory(id, directory, testConnection);
        return Response.ok(resultDirectoryModel).build();
    }

    @Override
    public Response createDirectory(
            final boolean testConnection,
            final AbstractDirectoryModel directory) {

        AbstractDirectoryModel addedDirectoryModel = directoriesService.addDirectory(directory, testConnection);
        return Response.ok(addedDirectoryModel).build();
    }

    @Override
    public Response deleteDirectory(
            final long id) {
        directoriesService.deleteDirectory(id);
        return Response.ok().build();
    }
}
