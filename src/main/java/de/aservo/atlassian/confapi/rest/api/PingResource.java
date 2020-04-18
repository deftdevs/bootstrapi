package de.aservo.atlassian.confapi.rest.api;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface PingResource {

    String PONG = "pong";

    @GET
    @Operation(
            tags = {ConfAPI.PING},
            summary = "Simple ping method for probing the REST api. Returns 'pong' upon success",
            responses = {
                    @ApiResponse(responseCode = "200"),
            }
    )
    Response getPing();

}
