package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.rest.AbstractSettingsResourceImpl;
import com.deftdevs.bootstrapi.jira.model.SettingsModel;
import com.deftdevs.bootstrapi.jira.service.api.JiraSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(BootstrAPI.SETTINGS)
@Tag(name = BootstrAPI.SETTINGS)
@Consumes({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
@Produces({MediaType.APPLICATION_JSON, BootstrAPI.MEDIA_TYPE_YAML, BootstrAPI.MEDIA_TYPE_YAML_LEGACY, BootstrAPI.MEDIA_TYPE_YAML_TEXT})
@SystemAdminOnly
public class SettingsResourceImpl extends AbstractSettingsResourceImpl<SettingsModel> {

    @Inject
    public SettingsResourceImpl(
            final JiraSettingsService settingsService) {

        super(settingsService);
    }

    // overridden to document the concrete response model in the generated OpenAPI spec
    @GET
    @Operation(
            summary = BootstrAPI.SETTINGS_GET_SUMMARY,
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsModel.class)),
                            description = BootstrAPI.SETTINGS_GET_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    @Override
    public Response getSettings() {
        return super.getSettings();
    }

    @PUT
    @Operation(
            summary = BootstrAPI.SETTINGS_PUT_SUMMARY,
            description = BootstrAPI.SETTINGS_PUT_RESPONSE_DESCRIPTION,
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = SettingsModel.class)),
                            description = BootstrAPI.SETTINGS_PUT_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "4XX", content = @Content(schema = @Schema(implementation = SettingsModel.class)),
                            description = BootstrAPI._ALL_PUT_FAILURE_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "5XX", content = @Content(schema = @Schema(implementation = SettingsModel.class)),
                            description = BootstrAPI._ALL_PUT_FAILURE_RESPONSE_DESCRIPTION
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = BootstrAPI.ERROR_COLLECTION_RESPONSE_DESCRIPTION
                    ),
            }
    )
    @Override
    public Response setSettings(
            final SettingsModel bean) {

        return super.setSettings(bean);
    }

}
