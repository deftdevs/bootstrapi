package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface DirectoryResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.DIRECTORY },
            summary = "Get a user directory",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AbstractDirectoryModel.class)),
                            description = "Returns the requested directory."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getDirectory(
            @PathParam("id") final long id);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.DIRECTORY },
            summary = "Create a user directory",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AbstractDirectoryModel.class)),
                            description = "Returns the added directory."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response createDirectory(
            @QueryParam("test-connection") @DefaultValue("false") final boolean testConnection,
            @NotNull final AbstractDirectoryModel directory);

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.DIRECTORY },
            summary = "Update a user directory",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AbstractDirectoryModel.class)),
                            description = "Returns the updated directory."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response updateDirectory(
            @PathParam("id") final long id,
            @QueryParam("test-connection") @DefaultValue("false") final boolean testConnection,
            @NotNull final AbstractDirectoryModel directory);

    @DELETE
    @Path("{id}")
    @Operation(
            tags = { BootstrAPI.DIRECTORY },
            summary = "Delete a user directory",
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
    Response deleteDirectory(
            @PathParam("id") final long id);

}
