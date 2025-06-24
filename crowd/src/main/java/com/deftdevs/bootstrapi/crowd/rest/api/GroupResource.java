package com.deftdevs.bootstrapi.crowd.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface GroupResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.GROUP },
            summary = "Get a group",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = GroupModel.class)),
                            description = "Returns the requested group details"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getGroup(
            @QueryParam("directoryId") final long directoryId,
            @QueryParam("name") final String groupName);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.GROUP },
            summary = "Create a group",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = GroupModel.class)),
                            description = "Returns the updated group details"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response createGroup(
            @QueryParam("directoryId") final long directoryId,
            final GroupModel groupModel);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.GROUP },
            summary = "Update a group",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = GroupModel.class)),
                            description = "Returns the updated group details"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response updateGroup(
            @QueryParam("directoryId") final long directoryId,
            @QueryParam("name") final String groupName,
            final GroupModel groupModel);

}
