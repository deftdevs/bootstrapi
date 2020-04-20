package de.aservo.atlassian.confapi.rest.api;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.SettingsBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

public interface SettingsResource {

    @GET
    @Operation(
            tags = { ConfAPI.SETTINGS },
            summary = "Get the application settings",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBean.class))),
            }
    )
    Response getSettings();

    @PUT
    @Operation(
            tags = { ConfAPI.SETTINGS },
            summary = "Set the application settings",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBean.class))),
            }
    )
    Response setSettings(
            @NotNull final SettingsBean bean);

}
