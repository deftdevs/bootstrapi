package com.deftdevs.bootstrapi.confluence.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.confluence.model.SettingsBrandingColorSchemeModel;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

public interface SettingsBrandingResource {

    @GET
    @Path(BootstrAPI.COLOR_SCHEME)
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
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
    Response getSettingsBrandingColorScheme();

    @PUT
    @Path(BootstrAPI.COLOR_SCHEME)
    @Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
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
    Response setSettingsBrandingColorScheme(
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
    Response getSettingsBrandingLogo();

    @PUT
    @Path(BootstrAPI.LOGO)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
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
    Response setSettingsBrandingLogo(
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
    Response getSettingsBrandingFavicon();

    @PUT
    @Path(BootstrAPI.FAVICON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
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
    Response setSettingsBrandingFavicon(
            final InputStream binaryInputStream);
}
