package de.aservo.confapi.crowd.rest.api;

import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.crowd.model.SessionConfigBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface SessionConfigResource {

    static final String SESSION_CONFIG = "session-config";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {SESSION_CONFIG},
            summary = "Get the session config",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SessionConfigBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getSessionConfig();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {SESSION_CONFIG},
            summary = "Set the session config",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = SessionConfigBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setSessionConfig(
            SessionConfigBean sessionConfigBean);

}
