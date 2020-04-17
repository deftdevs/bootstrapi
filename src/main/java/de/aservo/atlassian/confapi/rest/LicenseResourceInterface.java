package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.model.LicensesBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.core.Response;

/**
 * The License resource interface.
 */
public interface LicenseResourceInterface {

    /**
     * Returns all licenses.
     *
     * @return the licenses with entity type {@link de.aservo.atlassian.confapi.model.LicensesBean}.
     */
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Get all licenses information",
            description = "Upon successful request, returns a `LicensesBean` object containing license details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LicensesBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getLicenses();

    /**
     * Sets license.
     *
     * @param clear      true, if licenses shall be cleared before setting the new license
     * @param licenseKey the license key to set
     * @return the added license of type {@link de.aservo.atlassian.confapi.model.LicenseBean}.
     */
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Set a new license",
            description = "Existing license details are overwritten. Upon successful request, returns a `LicensesBean` object containing license details",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LicensesBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response setLicense(
            @Parameter(description="Clears license details before updating (Jira only).") final boolean clear,
            final String licenseKey);

}
