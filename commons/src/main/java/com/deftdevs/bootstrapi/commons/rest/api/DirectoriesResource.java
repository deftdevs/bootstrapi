package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Map;

public interface DirectoriesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.DIRECTORIES },
            summary = "Get all user directories",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(type = "object", additionalPropertiesSchema = AbstractDirectoryModel.class)),
                            description = "Returns directories mapped by their name."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Map<String, ? extends AbstractDirectoryModel> getDirectories();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.DIRECTORIES },
            summary = "Set directories mapped by their name.",
            description = "NOTE: All existing directories with the same 'name' attribute are updated.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(type = "object", additionalPropertiesSchema = AbstractDirectoryModel.class)),
                            description = "Returns directories mapped by their name."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Map<String, ? extends AbstractDirectoryModel> setDirectories(
            final Map<String, ? extends AbstractDirectoryModel> directories);

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
    void deleteDirectories(
            @QueryParam("force") final boolean force);

}
