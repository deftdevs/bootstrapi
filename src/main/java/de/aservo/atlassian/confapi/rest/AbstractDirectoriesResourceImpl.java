package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.model.DirectoriesBean;
import de.aservo.atlassian.confapi.model.DirectoryBean;
import de.aservo.atlassian.confapi.rest.api.DirectoriesResource;
import de.aservo.atlassian.confapi.service.api.DirectoryService;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractDirectoriesResourceImpl implements DirectoriesResource {

    private final DirectoryService directoryService;

    public AbstractDirectoriesResourceImpl(DirectoryService directoryService) {
        this.directoryService = checkNotNull(directoryService);
    }

    @Override
    public Response getDirectories() {
        final DirectoriesBean directoriesBean = new DirectoriesBean(directoryService.getDirectories());
        return Response.ok(directoriesBean).build();
    }

    @Override
    public Response setDirectory(
            final boolean testConnection,
            @Nonnull final DirectoryBean directory) {

        DirectoryBean addDirectory = directoryService.setDirectory(directory, testConnection);
        return Response.ok(addDirectory).build();
    }

}
