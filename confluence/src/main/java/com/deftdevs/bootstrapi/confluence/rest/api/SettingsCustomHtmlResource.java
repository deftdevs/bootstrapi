package com.deftdevs.bootstrapi.confluence.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.confluence.model.SettingsCustomHtmlModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface SettingsCustomHtmlResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Get the custom HTML",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsCustomHtmlModel.class)),
                            description = "Returns the custom HTML"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getCustomHtml();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Set the custom HTML",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsCustomHtmlModel.class)),
                            description = "Returns the updated custom HTML"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setCustomHtml(
            final SettingsCustomHtmlModel bean);

}
