package com.deftdevs.bootstrapi.crowd.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.crowd.model.AllBean;
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

public interface AllResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {BootstrAPI.ALL},
            summary = "Get the whole configuration",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "When getting whole configuration was successful."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getAll();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {BootstrAPI.ALL},
            summary = "Set the whole configuration",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "When setting whole configuration was successful."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setAll(
            final AllBean allBean);

}
