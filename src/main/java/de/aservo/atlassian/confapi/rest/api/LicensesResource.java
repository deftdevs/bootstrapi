package de.aservo.atlassian.confapi.rest.api;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.model.LicenseBean;
import de.aservo.atlassian.confapi.model.LicensesBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface LicensesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Get all licenses information",
            description = "Upon successful request, returns a `LicensesBean` object containing license details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LicensesBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response getLicenses();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Set a new set of license",
            description = "Existing license details are overwritten. Upon successful request, returns a `LicensesBean` object containing license details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LicensesBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response setLicenses(
            @Parameter(description="Clear license details before updating (Jira only).") @QueryParam ("clear") @DefaultValue("false") final boolean clear,
            @NotNull final LicensesBean licensesBean);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Set a new license",
            description = "Existing license details are overwritten. Upon successful request, returns a `LicensesBean` object containing license details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LicensesBean.class))),
                    @ApiResponse(responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class))),
            }
    )
    Response setLicense(
            @Parameter(description="Clear license details before updating (Jira only).") @QueryParam ("clear") @DefaultValue("false") final boolean clear,
            @NotNull final LicenseBean licenseBean);

}
