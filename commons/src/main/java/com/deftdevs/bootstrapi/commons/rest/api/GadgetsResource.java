package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.GadgetModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface GadgetsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.GADGETS },
            summary = "Get all gadgets",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GadgetModel.class))),
                            description = "Returns all gadgets."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getGadgets();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.GADGETS },
            summary = "Set a list of gadgets",
            description = "NOTE: This will only create gadgets that does not exist yet as there is no real 'update'.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GadgetModel.class))),
                            description = "Returns all gadgets."
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setGadgets(
            @NotNull final List<GadgetModel> gadgetModels);

    @DELETE
    @Operation(
            tags = { BootstrAPI.GADGETS },
            summary = "Delete all gadgets",
            description = "NOTE: The 'force' parameter must be set to 'true' in order to execute this request.",
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
    Response deleteGadgets(
            @QueryParam("force") final boolean force);

}
