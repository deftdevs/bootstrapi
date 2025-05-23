package com.deftdevs.bootstrapi.crowd.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.crowd.model.SettingsBrandingLoginPageModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public interface SettingsBrandingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(BootstrAPI.SETTINGS_BRANDING_LOGIN_PAGE)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Get the login-page settings",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBrandingLoginPageModel.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getLoginPage();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(BootstrAPI.SETTINGS_BRANDING_LOGIN_PAGE)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Set the login-page settings",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBrandingLoginPageModel.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setLoginPage(
            SettingsBrandingLoginPageModel settingsBrandingLoginPageModel);

    @PUT
    @Consumes(BootstrAPI.MEDIA_TYPE_IMAGE)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(BootstrAPI.LOGO)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Set the logo",
            responses = {
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setLogo(InputStream inputStream);
}
