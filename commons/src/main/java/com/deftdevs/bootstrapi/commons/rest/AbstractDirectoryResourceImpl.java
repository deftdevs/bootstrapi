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
    public AbstractDirectoryModel getDirectory(
            final long id) {

        return directoriesService.getDirectory(id);
    }

    @Override
    public AbstractDirectoryModel updateDirectory(
            final long id,
            final AbstractDirectoryModel directory) {

        return directoriesService.setDirectory(id, directory);
    }

    @Override
    public AbstractDirectoryModel createDirectory(
            final AbstractDirectoryModel directory) {

        return directoriesService.addDirectory(directory);
    }

    @Override
    public void deleteDirectory(
            final long id) {

        directoriesService.deleteDirectory(id);
    }
}
