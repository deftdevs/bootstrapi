package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface DirectoriesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.DIRECTORIES },
            summary = "Get all user directories",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AbstractDirectoryModel.class))),
                            description = "Returns all directories."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getDirectories();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.DIRECTORIES },
            summary = "Set a list of user directories",
            description = "NOTE: All existing directories with the same 'name' attribute are updated.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AbstractDirectoryModel.class))),
                            description = "Returns all directories."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setDirectories(
            @QueryParam("test-connection") @DefaultValue("false") final boolean testConnection,
            @NotNull final List<AbstractDirectoryModel> directories);

    @DELETE
    @Operation(
            tags = { BootstrAPI.DIRECTORIES },
            summary = "Delete all user directories",
            description = "NOTE: The 'force' parameter must be set to 'true' in order to execute this request.",
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
    Response deleteDirectories(
            @QueryParam("force") final boolean force);

}
