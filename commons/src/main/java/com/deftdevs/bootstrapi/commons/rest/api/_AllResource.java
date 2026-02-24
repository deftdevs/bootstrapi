package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

public interface _AllResource<_AllModel extends _AllModelAccessor> {

    @PUT
    @Operation(
            summary = "Apply a complete configuration",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Configuration applied successfully"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    Response setAll(
            @NotNull final _AllModel bean);

}
