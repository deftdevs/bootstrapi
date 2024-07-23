package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
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
        final AbstractDirectoryBean directoryBean = directoriesService.getDirectory(id);
        return Response.ok(directoryBean).build();
    }

    @Override
    public Response updateDirectory(
            final long id,
            final boolean testConnection,
            final AbstractDirectoryBean directory) {

        AbstractDirectoryBean resultDirectoryBean  = directoriesService.setDirectory(id, directory, testConnection);
        return Response.ok(resultDirectoryBean).build();
    }

    @Override
    public Response createDirectory(
            final boolean testConnection,
            final AbstractDirectoryBean directory) {

        AbstractDirectoryBean addedDirectoryBean = directoriesService.addDirectory(directory, testConnection);
        return Response.ok(addedDirectoryBean).build();
    }

    @Override
    public Response deleteDirectory(
            final long id) {
        directoriesService.deleteDirectory(id);
        return Response.ok().build();
    }
}
