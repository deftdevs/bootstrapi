package de.aservo.confapi.crowd.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.DirectoriesBean;
import de.aservo.confapi.commons.model.DirectoryGenericBean;
import de.aservo.confapi.commons.model.DirectoryInternalBean;
import de.aservo.confapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface DirectoriesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.DIRECTORIES },
            summary = "Get the list of directories",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = DirectoriesBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getDirectories();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.DIRECTORIES },
            summary = "Get a directory based on it's ID",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(
                            oneOf = {
                                    DirectoryInternalBean.class,
                                    DirectoryGenericBean.class,
                            }
                    ))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getDirectory(
            @PathParam("id") final long id);

}
