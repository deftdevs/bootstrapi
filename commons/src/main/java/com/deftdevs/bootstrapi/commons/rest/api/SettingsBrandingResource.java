package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public interface SettingsBrandingResource {

    @GET
    @Path(BootstrAPI.COLOR_SCHEME)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Get the color scheme",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBrandingColorSchemeModel.class)),
                            description = "Returns the color scheme"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getBrandingColorScheme();

    @PUT
    @Path(BootstrAPI.COLOR_SCHEME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Set the color scheme",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBrandingColorSchemeModel.class)),
                            description = "Returns the updated color scheme"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setBrandingColorScheme(
            final SettingsBrandingColorSchemeModel bean);

    @GET
    @Path(BootstrAPI.LOGO)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Get the logo",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = InputStream.class)),
                            description = "Returns the logo binary"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getBrandingLogo();

    @PUT
    @Path(BootstrAPI.LOGO)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Set the logo",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "logo successfully set"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setBrandingLogo(
            final InputStream binaryInputStream);

    @GET
    @Path(BootstrAPI.FAVICON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Get the favicon",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = InputStream.class)),
                            description = "Returns the favicon binary"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getBrandingFavicon();

    @PUT
    @Path(BootstrAPI.FAVICON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.SETTINGS },
            summary = "Set the favicon",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "favicon successfully set"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setBrandingFavicon(
            final InputStream binaryInputStream);
}
