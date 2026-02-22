package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

public interface ApplicationLinkResource {

    @GET
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.APPLICATION_LINK },
            summary = "Get an application link",
            description = "Upon successful request, ",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = ApplicationLinkModel.class)),
                            description = "Returns the requested application link."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    ApplicationLinkModel getApplicationLink(
            @PathParam("uuid") final UUID uuid);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.APPLICATION_LINK },
            summary = "Create an application link",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = ApplicationLinkModel.class)),
                            description = "Returns the added application link."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    ApplicationLinkModel createApplicationLink(
            final ApplicationLinkModel linkModel);

    @PUT
    @Path("{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.APPLICATION_LINK },
            summary = "Update an application link",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = ApplicationLinkModel.class)),
                            description = "Returns the updated application link."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    ApplicationLinkModel updateApplicationLink(
            @PathParam("uuid") final UUID uuid,
            final ApplicationLinkModel applicationLinkModel);

    @DELETE
    @Path("{uuid}")
    @Operation(
            tags = { BootstrAPI.APPLICATION_LINK },
            summary = "Delete an application link",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Returns an empty body."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    void deleteApplicationLink(
            @PathParam("uuid") final UUID uuid);

}
