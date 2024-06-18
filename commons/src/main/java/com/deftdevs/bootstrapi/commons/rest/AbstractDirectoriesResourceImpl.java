package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
import com.deftdevs.bootstrapi.commons.model.DirectoriesBean;
import com.deftdevs.bootstrapi.commons.rest.api.DirectoriesResource;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractDirectoriesResourceImpl implements DirectoriesResource {

    private final DirectoriesService directoriesService;

    public AbstractDirectoriesResourceImpl(DirectoriesService directoriesService) {
        this.directoriesService = checkNotNull(directoriesService);
    }

    @Override
    public Response getDirectories() {
        final DirectoriesBean directoriesBean = directoriesService.getDirectories();
        return Response.ok(directoriesBean).build();
    }

    @Override
    public Response getDirectory(
            final long id) {
        final AbstractDirectoryBean directoryBean = directoriesService.getDirectory(id);
        return Response.ok(directoryBean).build();
    }

    @Override
    public Response setDirectories (
            final boolean testConnection,
            final DirectoriesBean directories) {

        DirectoriesBean directoriesBean = directoriesService.setDirectories(directories, testConnection);
        return Response.ok(directoriesBean).build();
    }

    @Override
    public Response setDirectory (
            final long id,
            final boolean testConnection,
            final AbstractDirectoryBean directory) {

        AbstractDirectoryBean directoriesBean = directoriesService.setDirectory(id, directory, testConnection);
        return Response.ok(directoriesBean).build();
    }

    @Override
    public Response addDirectory (
            final boolean testConnection,
            final AbstractDirectoryBean directory) {

        AbstractDirectoryBean addedDirectoryBean = directoriesService.addDirectory(directory, testConnection);
        return Response.ok(addedDirectoryBean).build();
    }

    @Override
    public Response deleteDirectories(
            final boolean force) {
        directoriesService.deleteDirectories(force);
        return Response.ok().build();
    }

    @Override
    public Response deleteDirectory(
            final long id) {
        directoriesService.deleteDirectory(id);
        return Response.ok().build();
    }
}
