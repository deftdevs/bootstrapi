package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

public interface CloudTunnelResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.CLOUD },
            summary = "Get the cloud application tunnel",
            description = "The key is never returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = CloudTunnelModel.class)),
                            description = "Returns the cloud application tunnel."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    CloudTunnelModel getCloudTunnel();

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.CLOUD },
            summary = "Set the cloud application tunnel",
            description = "Creates or replaces the cloud application tunnel from the key generated when creating the tunnel"
                    + " in the cloud, optionally with an upstream URL. Applying the current key again is a no-op."
                    + " Fails while the tunnel is managed by a cloud connection.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = CloudTunnelModel.class)),
                            description = "Returns the cloud application tunnel."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    CloudTunnelModel setCloudTunnel(
            final CloudTunnelModel cloudTunnelModel);

    @DELETE
    @Operation(
            tags = { BootstrAPI.CLOUD },
            summary = "Delete the cloud application tunnel",
            description = "Deletes the cloud application tunnel in this instance only, without contacting the cloud."
                    + " Use this for an instance copied from another one, e.g. a staging instance restored from"
                    + " production data, which would otherwise share the tunnel with the original instance."
                    + " Fails while the tunnel is managed by a cloud connection.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Returns an empty body."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    void deleteCloudTunnel();

}
