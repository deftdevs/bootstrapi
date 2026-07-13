package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.MailServerPopModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public interface MailServerPopResource {

    @GET
    @Path(BootstrAPI.MAIL_SERVER_POP)
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.MAIL_SERVER },
            summary = "Get the default POP mail server",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerPopModel.class)),
                            description = "Returns the default POP mail server's details."
                    ),
                    @ApiResponse(
                            responseCode = "204", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns an error message explaining that no default POP mail server is configured."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getMailServerPop();

    @PUT
    @Path(BootstrAPI.MAIL_SERVER_POP)
    @Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.MAIL_SERVER },
            summary = "Set the default POP mail server",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerPopModel.class)),
                            description = "Returns the default POP mail server's details."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setMailServerPop(
            final MailServerPopModel bean);

    @DELETE
    @Path(BootstrAPI.MAIL_SERVER_POP)
    @Operation(
            tags = { BootstrAPI.MAIL_SERVER },
            summary = "Remove the default POP mail server",
            description = "Removes the default POP mail server if one is configured; does nothing otherwise.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "The default POP mail server has been removed or none was configured."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response deleteMailServerPop();

}
