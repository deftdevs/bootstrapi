package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.PermissionsGlobalModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface PermissionsResource {

    @GET
    @Path(BootstrAPI.PERMISSIONS_GLOBAL)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.PERMISSIONS },
            summary = "Get global permissions configuration",
            description = "Get the global permissions for ... TODO",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PermissionsGlobalModel.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getPermissionGlobal();

    @PUT
    @Path(BootstrAPI.PERMISSIONS_GLOBAL)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.PERMISSIONS },
            summary = "Set global permissions configuration",
            description = "Set the global permissions for ... TODO",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PermissionsGlobalModel.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setPermissionGlobal(
            @NotNull PermissionsGlobalModel permissionsGlobalModel);

}
