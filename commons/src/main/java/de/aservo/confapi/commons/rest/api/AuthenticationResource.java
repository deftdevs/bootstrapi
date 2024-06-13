package de.aservo.confapi.commons.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.http.PATCH;
import de.aservo.confapi.commons.model.AuthenticationIdpsBean;
import de.aservo.confapi.commons.model.AuthenticationSsoBean;
import de.aservo.confapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface AuthenticationResource {

    @GET
    @Path(ConfAPI.AUTHENTICATION_IDPS)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.AUTHENTICATION },
            summary = "Get all authentication identity providers",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AuthenticationIdpsBean.class)),
                            description = "Returns all authentication identity providers."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response getAuthenticationIdps();

    @PATCH
    @Path(ConfAPI.AUTHENTICATION_IDPS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.AUTHENTICATION },
            summary = "Set all authentication identity providers",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AuthenticationIdpsBean.class)),
                            description = "Returns the set authentication identity providers."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response setAuthenticationIdps(
            final AuthenticationIdpsBean authenticationIdpsBean);

    @GET
    @Path(ConfAPI.AUTHENTICATION_SSO)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.AUTHENTICATION },
            summary = "Get authentication SSO configuration",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AuthenticationSsoBean.class)),
                            description = "Returns the authentication SSO configuration."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response getAuthenticationSso();

    @PATCH
    @Path(ConfAPI.AUTHENTICATION_SSO)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.AUTHENTICATION },
            summary = "Set authentication SSO configuration",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AuthenticationSsoBean.class)),
                            description = "Returns the set authentication SSO configuration."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response setAuthenticationSso(
            final AuthenticationSsoBean authenticationSsoBean);

}
