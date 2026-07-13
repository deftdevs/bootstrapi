package com.deftdevs.bootstrapi.crowd.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

public interface GroupsResource {

    @PATCH
    @Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
    @Operation(
            tags = { BootstrAPI.GROUPS },
            summary = "Set groups",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = GroupModel.class)),
                            description = "Returns the updated groups details"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setGroups(
            @QueryParam("directoryId") final long directoryId,
            final Map<String, GroupModel> groupModels);

}
