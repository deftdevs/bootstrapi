package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.SettingsSecurityModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

public interface SettingsSecurityResource<B extends SettingsSecurityModel> {

    @GET
    @Operation(
            summary = BootstrAPI.SETTINGS_SECURITY_GET_SUMMARY,
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsSecurityModel.class)),
                            description = BootstrAPI.SETTINGS_SECURITY_GET_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    Response getSettingsSecurity();

    @PUT
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = BootstrAPI.SETTINGS_SECURITY_PUT_SUMMARY,
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsSecurityModel.class)),
                            description = BootstrAPI.SETTINGS_SECURITY_PUT_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    Response setSettingsSecurity(
            final B bean);

}
