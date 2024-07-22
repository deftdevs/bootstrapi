package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
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
        final List<AbstractDirectoryBean> directoryBeans = directoriesService.getDirectories();
        return Response.ok(directoryBeans).build();
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
            final List<AbstractDirectoryBean> directories) {

        List<AbstractDirectoryBean> directoryBeans = directoriesService.setDirectories(directories, testConnection);
        return Response.ok(directoryBeans).build();
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
