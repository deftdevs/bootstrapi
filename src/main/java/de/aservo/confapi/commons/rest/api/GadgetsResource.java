package de.aservo.confapi.commons.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.commons.model.GadgetBean;
import de.aservo.confapi.commons.model.GadgetsBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface GadgetsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.GADGETS },
            summary = "Get all gadgets",
            description = "Upon successful request, returns a `GadgetsBean` object containing gadgets details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GadgetsBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response getGadgets();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.GADGETS },
            summary = "Gets a single gadget",
            description = "Upon successful request, returns a `GadgetsBean` object containing gadgets details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GadgetBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response getGadget(
            @PathParam("id") final long id);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.GADGETS },
            summary = "Sets or updates a new list of gadgets",
            description = "Upon successful request, returns a `GadgetsBean` object containing gadgets details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GadgetsBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response setGadgets(
            @NotNull final GadgetsBean gadgetsBean);

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.GADGETS },
            summary = "Updates a single gadget",
            description = "Upon successful request, returns a `GadgetBean` object containing gadget details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GadgetBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response setGadget(
            @PathParam("id") final long id,
            @NotNull final GadgetBean gadgetBean);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.GADGETS },
            summary = "Adds a single gadget",
            description = "Upon successful request, returns the added `GadgetBean` object containing gadget details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = GadgetBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response addGadget(
            @NotNull final GadgetBean gadgetBean);

    @DELETE
    @Operation(
            tags = { ConfAPI.GADGETS },
            summary = "Deletes all gadgets. NOTE: The 'force' parameter must be set to 'true' in order to execute this request.",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response deleteGadgets(
            @QueryParam("force") final boolean force);

    @DELETE
    @Path("{id}")
    @Operation(
            tags = { ConfAPI.GADGETS },
            summary = "Deletes a single gadget",
            responses = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response deleteGadget(
            @PathParam("id") final long id);
}
