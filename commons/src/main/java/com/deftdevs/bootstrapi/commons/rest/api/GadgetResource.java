package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.GadgetModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface GadgetResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.GADGET },
            summary = "Get a gadget",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = GadgetModel.class)),
                            description = "Returns the requested gadget."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getGadget(
            @PathParam("id") final long id);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.GADGET },
            summary = "Add a gadget",
            description = "Upon successful request, returns a `GadgetModel` object of the created gadget.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = GadgetModel.class)),
                            description = "Returns the added gadget."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response createGadget(
            @NotNull final GadgetModel gadgetModel);

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.GADGET },
            summary = "Update a gadget",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = GadgetModel.class)),
                            description = "Returns the updated gadget."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response updateGadget(
            @PathParam("id") final long id,
            @NotNull final GadgetModel gadgetModel);

    @DELETE
    @Path("{id}")
    @Operation(
            tags = { BootstrAPI.GADGET },
            summary = "Delete a gadget",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Returns an empty body."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response deleteGadget(
            @PathParam("id") final long id);

}
