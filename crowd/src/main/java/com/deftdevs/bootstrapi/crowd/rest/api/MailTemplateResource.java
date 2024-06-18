package com.deftdevs.bootstrapi.crowd.rest.api;

import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.crowd.model.MailTemplatesBean;
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

public interface MailTemplateResource {

    static final String MAIL_TEMPLATES = "mail-templates";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {MAIL_TEMPLATES},
            summary = "Get the mail templates",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MailTemplatesBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getMailTemplates();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {MAIL_TEMPLATES},
            summary = "Set the mail templates",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = MailTemplatesBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setMailTemplates(
            MailTemplatesBean mailTemplatesBean);

}
