package com.deftdevs.bootstrapi.crowd.rest.api;

import com.atlassian.annotations.security.XsrfProtectionExcluded;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface TrustedProxiesResource {

    static final String TRUSTED_PROXIES = "trusted-proxies";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {TRUSTED_PROXIES},
            summary = "Get the trusted proxies",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getTrustedProxies();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {TRUSTED_PROXIES},
            summary = "Set the trusted proxies",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setTrustedProxies(
            List<String> trustedProxies);

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @XsrfProtectionExcluded
    @Operation(
            tags = {TRUSTED_PROXIES},
            summary = "Add a trusted proxy",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response addTrustedProxy(
            String trustedProxy);

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {TRUSTED_PROXIES},
            summary = "Remove a trusted proxy",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response removeTrustedProxy(
            String trustedProxy);

}
