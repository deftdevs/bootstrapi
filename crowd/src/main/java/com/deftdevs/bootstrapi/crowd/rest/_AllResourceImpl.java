package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.rest._AbstractAllResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import com.deftdevs.bootstrapi.crowd.model._AllModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(BootstrAPI._ROOT)
@Tag(name = BootstrAPI._ALL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SystemAdminOnly
public class _AllResourceImpl extends _AbstractAllResourceImpl<_AllModel> {

    @Inject
    public _AllResourceImpl(
            final _AllService<_AllModel> allService) {

        super(allService);
    }

    // overridden to document the concrete response model in the generated OpenAPI spec
    @PUT
    @Operation(
            summary = BootstrAPI._ALL_PUT_SUMMARY,
            description = BootstrAPI._ALL_PUT_RESPONSE_DESCRIPTION
                    + " Crowd does not support applying a POP mail server configuration"
                    + " or security settings.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = _AllModel.class)),
                            description = BootstrAPI._ALL_PUT_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "4XX", content = @Content(schema = @Schema(implementation = _AllModel.class)),
                            description = BootstrAPI._ALL_PUT_FAILURE_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "5XX", content = @Content(schema = @Schema(implementation = _AllModel.class)),
                            description = BootstrAPI._ALL_PUT_FAILURE_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    @Override
    public Response setAll(
            final _AllModel allModel) {

        return super.setAll(allModel);
    }

}
