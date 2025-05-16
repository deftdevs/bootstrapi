package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.SettingsModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

public interface SettingsResource<B extends SettingsModel> {

    @GET
    @Operation(
            summary = BootstrAPI.SETTINGS_GENERAL_GET_SUMMARY,
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsModel.class)),
                            description = BootstrAPI.SETTINGS_GENERAL_GET_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    Response getSettings();

    @PUT
    @Operation(
            summary = BootstrAPI.SETTINGS_GENERAL_PUT_SUMMARY,
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsModel.class)),
                            description = BootstrAPI.SETTINGS_GENERAL_PUT_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    Response setSettings(
            @NotNull final B bean);

}
