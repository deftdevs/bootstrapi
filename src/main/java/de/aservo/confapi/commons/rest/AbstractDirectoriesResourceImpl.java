package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.DirectoriesBean;
import de.aservo.confapi.commons.model.DirectoryBean;
import de.aservo.confapi.commons.rest.api.DirectoriesResource;
import de.aservo.confapi.commons.service.api.DirectoryService;

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
