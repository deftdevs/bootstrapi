package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.core.Response;

public interface PingResourceInterface {

    String PONG = "pong";

    @Operation(
            tags = {ConfAPI.PING},
            summary = "Simple ping method for probing the REST api. Returns 'pong' upon success",
            responses = {
                    @ApiResponse(responseCode = "200"),
            }
    )
    Response getPing();

}
