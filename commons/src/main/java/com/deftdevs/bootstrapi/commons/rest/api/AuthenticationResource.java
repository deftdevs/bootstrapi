package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

public interface AuthenticationResource {

    @GET
    @Path(BootstrAPI.AUTHENTICATION_IDPS)
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.AUTHENTICATION },
            summary = "Get all authentication identity providers",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AbstractAuthenticationIdpModel.class))),
                            description = "Returns all authentication identity providers."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Map<String, ? extends AbstractAuthenticationIdpModel> getAuthenticationIdps();

    @PATCH
    @Path(BootstrAPI.AUTHENTICATION_IDPS)
    @Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.AUTHENTICATION },
            summary = "Set all authentication identity providers",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AbstractAuthenticationIdpModel.class))),
                            description = "Returns the set authentication identity providers."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Map<String, ? extends AbstractAuthenticationIdpModel> setAuthenticationIdps(
            final Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModels);

    @GET
    @Path(BootstrAPI.AUTHENTICATION_SSO)
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.AUTHENTICATION },
            summary = "Get authentication SSO configuration",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AuthenticationSsoModel.class)),
                            description = "Returns the authentication SSO configuration."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    AuthenticationSsoModel getAuthenticationSso();

    @PATCH
    @Path(BootstrAPI.AUTHENTICATION_SSO)
    @Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.AUTHENTICATION },
            summary = "Set authentication SSO configuration",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = AuthenticationSsoModel.class)),
                            description = "Returns the set authentication SSO configuration."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    AuthenticationSsoModel setAuthenticationSso(
            final AuthenticationSsoModel authenticationSsoModel);

}
