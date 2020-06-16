package de.aservo.confapi.crowd.rest;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.crowd.model.DirectoryAttributesBean;
import de.aservo.confapi.crowd.model.DirectoryBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(ConfAPI.DIRECTORIES)
@Produces(MediaType.APPLICATION_JSON)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class DirectoriesResource {

    @ComponentImport
    private final DirectoryManager directoryManager;

    @Inject
    public DirectoriesResource(
            final DirectoryManager directoryManager) {

        this.directoryManager = directoryManager;
    }

    @GET
    @Path("{id}")
    public Response getDirectory(
            @PathParam("id") final long id) {

        DirectoryBean directoryBean = null;

        try {
            final Directory directory = directoryManager.findDirectoryById(id);

            if (directory != null) {
                directoryBean = DirectoryBean.from(directory);
            }
        } catch (DirectoryNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(directoryBean).build();
    }

    @GET
    @Path("{id}/attributes")
    public Response getDirectoryAttributes(
            @PathParam("id") final long id) {

        DirectoryAttributesBean directoryAttributesBean = null;

        try {
            final Directory directory = directoryManager.findDirectoryById(id);

            if (directory != null) {
                directoryAttributesBean = DirectoryAttributesBean.from(directory);
            }
        } catch (DirectoryNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(directoryAttributesBean).build();
    }

}
