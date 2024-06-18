package de.aservo.confapi.crowd.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.crowd.model.AllBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface AllResource {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {ConfAPI.ALL},
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
