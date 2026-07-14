package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.PluginModel;
import com.deftdevs.bootstrapi.commons.model.UpmModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public interface UpmResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.UPM },
            summary = "Get all installed plugins",
            description = "Returns every installed plugin (bundled and user-installed) with its version and enabled state, keyed by plugin key",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = PluginModel.class)),
                            description = "Returns a map of all installed plugins, keyed by plugin key"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    Response getPlugins();

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.UPM },
            summary = "Apply a UPM configuration",
            description = "Resolves, installs and enables (or disables) the given plugins. Every plugin references one of"
                    + " the named resolvers by key: 'marketplace' type resolvers look the artifact up through the"
                    + " Marketplace REST API from the plugin key and version, 'maven' type resolvers derive it from the"
                    + " plugin's Maven coordinates and the standard repository layout. A resolver's base URL may point to"
                    + " a proxying repository (e.g. an Artifactory generic remote), and each resolver supports basic-auth"
                    + " credentials and an optional web proxy. Plugins already installed in the requested version are"
                    + " skipped, so re-applying the same configuration is safe.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = UpmModel.class)),
                            description = "Returns the applied plugins. The per-plugin outcome is reported in the"
                                    + " 'status' map, keyed by plugin key. The resolvers are not echoed."
                    ),
                    @ApiResponse(
                            responseCode = "4XX", content = @Content(schema = @Schema(implementation = UpmModel.class)),
                            description = "One or more plugins failed to apply. The response code is the highest per-plugin"
                                    + " status code; inspect the 'status' map in the response body."
                    ),
                    @ApiResponse(
                            responseCode = "5XX", content = @Content(schema = @Schema(implementation = UpmModel.class)),
                            description = "One or more plugins failed to apply. The response code is the highest per-plugin"
                                    + " status code; inspect the 'status' map in the response body."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    Response setUpm(
            final UpmModel upmModel);

}
