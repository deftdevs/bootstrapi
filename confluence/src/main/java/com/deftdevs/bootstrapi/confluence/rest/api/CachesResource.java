package com.deftdevs.bootstrapi.confluence.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.confluence.model.CacheBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface CachesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {BootstrAPI.CACHE},
            summary = "Read all cache information",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CacheBean.class))),
                            description = "Returns all information for current cache configuration."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response getCaches();

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {BootstrAPI.CACHE},
            summary = "Read cache information for a specified cache",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = CacheBean.class)),
                            description = "Returns configuration for a given cache."),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response getCache(@PathParam("name") final String name);

    @PUT
    @Path("{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {BootstrAPI.CACHE},
            summary = "Update an existing cache-size. Only Setting maxObjectCount is supported.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = CacheBean.class)),
                            description = "Returns the modified Cache."
                    ),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response updateCache(@PathParam("name") final String name, final CacheBean cache);

    @POST
    @Path("{name}/" + BootstrAPI.CACHE_FLUSH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = {BootstrAPI.CACHE},
            summary = "Flushes a cache",
            description = "Empties the specified cache",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = CacheBean.class)),
                            description = "Returns the emptied Cache."
                    ),
                    @ApiResponse(
                            content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    )
            }
    )
    Response flushCache(@PathParam("name") final String name);
}
