package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.http.PATCH;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface AuthenticationResource<IB extends AbstractAuthenticationIdpBean, SB extends AuthenticationSsoBean> {

    @GET
    @Path(BootstrAPI.AUTHENTICATION_IDPS)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.AUTHENTICATION },
            summary = "Get all authentication identity providers",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AbstractAuthenticationIdpBean.class))),
                            description = "Returns all authentication identity providers."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response getAuthenticationIdps();

    @PATCH
    @Path(BootstrAPI.AUTHENTICATION_IDPS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.AUTHENTICATION },
            summary = "Set all authentication identity providers",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AbstractAuthenticationIdpBean.class))),
                            description = "Returns the set authentication identity providers."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response setAuthenticationIdps(
            final List<IB> authenticationIdpBeans);

    @GET
    @Path(BootstrAPI.AUTHENTICATION_SSO)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.AUTHENTICATION },
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
    @Path(BootstrAPI.AUTHENTICATION_SSO)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.AUTHENTICATION },
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
            final SB authenticationSsoBean);

}
