package de.aservo.confapi.commons.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface PingResource {

    String PONG = "pong";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
            tags = { ConfAPI.PING },
            summary = "Ping method for probing the REST API.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Returns '" + PONG + "'"
                    ),
            }
    )
    Response getPing();

}
