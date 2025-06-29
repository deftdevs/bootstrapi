package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.MailServerSmtpModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface MailServerSmtpResource {

    @GET
    @Path(BootstrAPI.MAIL_SERVER_SMTP)
    @Produces(MediaType.APPLICATION_JSON)
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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

}
