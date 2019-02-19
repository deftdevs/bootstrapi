package de.aservo.atlassian.crowd.confapi.rest;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import de.aservo.atlassian.crowd.confapi.CrowdWebAuthenticationHelper;
import de.aservo.atlassian.crowd.confapi.bean.DirectoryAttributesBean;
import de.aservo.atlassian.crowd.confapi.bean.DirectoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Directory resource.
 */
@Path("/directory")
@AnonymousAllowed
@Produces(MediaType.APPLICATION_JSON)
@Component
public class DirectoryResource {

    @ComponentImport
    private final DirectoryManager directoryManager;

    private final CrowdWebAuthenticationHelper crowdWebAuthenticationHelper;

    /**
     * Constructor.
     */
    @Autowired
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
                directoryBean = new DirectoryBean(directory);
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
                directoryAttributesBean = new DirectoryAttributesBean(directory);
            }
        } catch (DirectoryNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(directoryAttributesBean).build();
    }

}
