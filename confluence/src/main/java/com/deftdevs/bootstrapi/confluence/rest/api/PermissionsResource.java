package com.deftdevs.bootstrapi.confluence.rest.api;

import com.deftdevs.bootstrapi.confluence.model.PermissionAnonymousAccessBean;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public interface PermissionsResource {

    @GET
    @Path(BootstrAPI.PERMISSION_ANONYMOUS_ACCESS)
    @Operation(
            tags = { BootstrAPI.PERMISSIONS },
            summary = "Retrieve current anonymous access configuration",
            description = "Gets the current global permissions for anonymous access to public pages and user profiles",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PermissionAnonymousAccessBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getPermissionAnonymousAccess();

    @PUT
    @Path(BootstrAPI.PERMISSION_ANONYMOUS_ACCESS)
    @Operation(
            tags = { BootstrAPI.PERMISSIONS },
            summary = "Set anonymous access configuration",
            description = "Sets global permissions for anonymous access to public pages and user profiles",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = PermissionAnonymousAccessBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setPermissionAnonymousAccess(
            @NotNull PermissionAnonymousAccessBean accessBean);

}
