package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
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

public interface MailServerSmtpResource {

    @GET
    @Path(BootstrAPI.MAIL_SERVER_SMTP)
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.MAIL_SERVER },
            summary = "Get the default SMTP mail server",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerSmtpModel.class)),
                            description = "Returns the default SMTP mail server's details."
                    ),
                    @ApiResponse(
                            responseCode = "204", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns an error message explaining that no default SMTP mail server is configured."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getMailServerSmtp();

    @PUT
    @Path(BootstrAPI.MAIL_SERVER_SMTP)
    @Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.MAIL_SERVER },
            summary = "Set the default SMTP mail server",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerSmtpModel.class)),
                            description = "Returns the default SMTP mail server's details."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setMailServerSmtp(
            final MailServerSmtpModel bean);

    @DELETE
    @Path(BootstrAPI.MAIL_SERVER_SMTP)
    @Operation(
            tags = { BootstrAPI.MAIL_SERVER },
            summary = "Remove the default SMTP mail server",
            description = "Removes the default SMTP mail server if one is configured; does nothing otherwise.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "The default SMTP mail server has been removed or none was configured."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response deleteMailServerSmtp();

}
