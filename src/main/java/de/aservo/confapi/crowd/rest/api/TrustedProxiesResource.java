package de.aservo.confapi.crowd.rest.api;

import com.atlassian.annotations.security.XsrfProtectionExcluded;
import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.crowd.model.TrustedProxiesBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface TrustedProxiesResource {

    static final String TRUSTED_PROXIES = "trusted-proxies";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {TRUSTED_PROXIES},
            summary = "Get the trusted proxies",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TrustedProxiesBean.class))),
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
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TrustedProxiesBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setTrustedProxies(
            TrustedProxiesBean trustedProxiesBean);

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @XsrfProtectionExcluded
    @Operation(
            tags = {TRUSTED_PROXIES},
            summary = "Add a trusted proxy",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TrustedProxiesBean.class))),
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
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TrustedProxiesBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response removeTrustedProxy(
            String trustedProxy);

}
