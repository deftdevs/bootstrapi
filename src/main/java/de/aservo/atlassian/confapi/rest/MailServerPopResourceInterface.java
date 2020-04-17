package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.model.MailServerPopBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;

public interface MailServerPopResourceInterface {

    @Operation(
            tags = { ConfAPI.MAIL_SERVER },
            summary = "Get the default POP mail server",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerPopBean.class))),
                    @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    public Response getMailServerPop();

    @Operation(
            tags = { ConfAPI.MAIL_SERVER },
            summary = "Set the default POP mail server",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MailServerPopBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    public Response setMailServerPop(
            @Nonnull final MailServerPopBean bean);

}
