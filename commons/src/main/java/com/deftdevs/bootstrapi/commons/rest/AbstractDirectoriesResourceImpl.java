package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.rest.api.DirectoriesResource;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import java.util.Map;

public abstract class AbstractDirectoriesResourceImpl implements DirectoriesResource {

    private final DirectoriesService directoriesService;

    public AbstractDirectoriesResourceImpl(
            final DirectoriesService directoriesService) {

        this.directoriesService = directoriesService;
    }

    @Override
    public Map<String, ? extends AbstractDirectoryModel> getDirectories() {
        return directoriesService.getDirectories();
    }

    @Override
    public Map<String, ? extends AbstractDirectoryModel> setDirectories (
            final Map<String, ? extends AbstractDirectoryModel> directories) {

        return directoriesService.setDirectories(directories);
    }

    @Override
    public void deleteDirectories(
            final boolean force) {

        directoriesService.deleteDirectories(force);
    }

}
