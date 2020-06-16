package de.aservo.confapi.crowd.rest;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.crowd.helper.CrowdWebAuthenticationHelper;
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

@Path(ConfAPI.DIRECTORY)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class DirectoryResource {

    @ComponentImport
    private final DirectoryManager directoryManager;

    private final CrowdWebAuthenticationHelper crowdWebAuthenticationHelper;

    @Inject
    public DirectoryResource(
            final DirectoryManager directoryManager,
            final CrowdWebAuthenticationHelper crowdWebAuthenticationHelper) {

        this.directoryManager = directoryManager;
        this.crowdWebAuthenticationHelper = crowdWebAuthenticationHelper;
    }

    @GET
    @Path("{id}")
    public Response getDirectory(
            @PathParam("id") final long id) {

        crowdWebAuthenticationHelper.mustBeSysAdmin();

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

        crowdWebAuthenticationHelper.mustBeSysAdmin();

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
