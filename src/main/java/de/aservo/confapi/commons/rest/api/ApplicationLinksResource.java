package de.aservo.confapi.commons.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.ApplicationLinkBean;
import de.aservo.confapi.commons.model.ApplicationLinksBean;
import de.aservo.confapi.commons.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ApplicationLinksResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.APPLICATION_LINKS },
            summary = "Get all application links",
            description = "Upon successful request, returns a `ApplicationLinksBean` object containing all application links",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ApplicationLinksBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response getApplicationLinks();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.APPLICATION_LINKS },
            summary = "Set a new set of application links",
            description = "Upon successful request, returns a `ApplicationLinksBean` object containing all application links",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ApplicationLinksBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response setApplicationLinks(
            @QueryParam("ignore-setup-errors") @DefaultValue("false") final boolean ignoreSetupErrors,
            @NotNull final ApplicationLinksBean linksBean);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.APPLICATION_LINKS },
            summary = "Add a single application link",
            description = "Upon successful request, returns the added `ApplicationLinkBean` object",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ApplicationLinkBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response addApplicationLink(
            @QueryParam("ignore-setup-errors") @DefaultValue("false") final boolean ignoreSetupErrors,
            @NotNull final ApplicationLinkBean linkBean);

}
