package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.AbstractDirectoryBean;
import de.aservo.confapi.commons.model.DirectoriesBean;
import de.aservo.confapi.commons.rest.api.DirectoriesResource;
import de.aservo.confapi.commons.service.api.DirectoryService;

import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractDirectoriesResourceImpl implements DirectoriesResource {

    private final DirectoryService directoryService;

    public AbstractDirectoriesResourceImpl(DirectoryService directoryService) {
        this.directoryService = checkNotNull(directoryService);
    }

    @Override
    public Response getDirectories() {
        final DirectoriesBean directoriesBean = directoryService.getDirectories();
        return Response.ok(directoriesBean).build();
    }

    @Override
    public Response setDirectories (
            final boolean testConnection,
            final DirectoriesBean directories) {

        DirectoriesBean directoriesBean = directoryService.setDirectories(directories, testConnection);
        return Response.ok(directoriesBean).build();
    }

    @Override
    public Response addDirectory (
            final boolean testConnection,
            final AbstractDirectoryBean directory) {

        AbstractDirectoryBean addedDirectoryBean = directoryService.addDirectory(directory, testConnection);
        return Response.ok(addedDirectoryBean).build();
    }
}
