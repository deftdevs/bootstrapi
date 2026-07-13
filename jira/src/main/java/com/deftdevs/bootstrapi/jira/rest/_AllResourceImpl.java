package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.rest._AbstractAllResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api._AllService;
import com.deftdevs.bootstrapi.jira.model._AllModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(BootstrAPI._ROOT)
@Tag(name = BootstrAPI._ALL)
@Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
@Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
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
            description = BootstrAPI._ALL_PUT_RESPONSE_DESCRIPTION,
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
