package de.aservo.atlassian.confapi.rest.api;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.model.MailServerPopBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface MailServerPopResource {

    @GET
    @Path(ConfAPI.MAIL_SERVER_POP)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.MAIL_SERVER },
            summary = "Get the default POP mail server",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerPopBean.class))),
                    @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getMailServerPop();

    @PUT
    @Path(ConfAPI.MAIL_SERVER_POP)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.MAIL_SERVER },
            summary = "Set the default POP mail server",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerPopBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setMailServerPop(
            @NotNull final MailServerPopBean bean);

}
