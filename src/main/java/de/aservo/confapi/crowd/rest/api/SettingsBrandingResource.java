package de.aservo.confapi.crowd.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.crowd.model.SettingsBrandingLoginPageBean;
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
    @Path(ConfAPI.SETTINGS_BRANDING_LOGIN_PAGE)
    @Operation(
            tags = { ConfAPI.SETTINGS },
            summary = "Get the login-page settings",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBrandingLoginPageBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getLoginPage();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(ConfAPI.SETTINGS_BRANDING_LOGIN_PAGE)
    @Operation(
            tags = { ConfAPI.SETTINGS },
            summary = "Set the login-page settings",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBrandingLoginPageBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setLoginPage(
            SettingsBrandingLoginPageBean settingsBrandingLoginPageBean);

    @PUT
    @Consumes(ConfAPI.MEDIA_TYPE_IMAGE)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(ConfAPI.LOGO)
    @Operation(
            tags = { ConfAPI.SETTINGS },
            summary = "Set the logo",
            responses = {
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setLogo(InputStream inputStream);
}
