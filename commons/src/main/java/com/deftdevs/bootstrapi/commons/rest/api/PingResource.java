package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
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
            tags = { BootstrAPI.PING },
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
