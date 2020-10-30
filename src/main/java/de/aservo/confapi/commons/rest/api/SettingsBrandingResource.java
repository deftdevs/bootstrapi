package de.aservo.confapi.commons.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.SettingsBrandingColourSchemeBean;
import de.aservo.confapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

public interface SettingsBrandingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.SETTINGS },
            summary = "Get the colour scheme",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBrandingColourSchemeBean.class)),
                            description = "Returns the colour scheme"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getColourScheme();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.SETTINGS },
            summary = "Set the colour scheme",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsBrandingColourSchemeBean.class)),
                            description = "Returns the updated colour scheme"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setColourScheme(
            @NotNull final SettingsBrandingColourSchemeBean bean);

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Operation(
            tags = { ConfAPI.SETTINGS },
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
    Response getLogo();

    @PUT
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.SETTINGS },
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
    Response setLogo(
            @NotNull final InputStream binaryInputStream);

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Operation(
            tags = { ConfAPI.SETTINGS },
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
    Response getFavicon();

    @PUT
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.SETTINGS },
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
    Response setFavicon(
            @NotNull final InputStream binaryInputStream);
}
