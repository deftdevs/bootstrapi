package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public interface LicensesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.LICENSES },
            summary = "Get all licenses information",
            description = "Upon successful request, returns a `LicensesModel` object containing license details. Be aware that `products` collection of the `LicenseModel` contains the product display names, not the product key names",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseModel.class))),
                            description = "Returns a list of all licenses (NOTE: for all applications except Jira this will return a single license)"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getLicenses();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.LICENSES },
            summary = "Set a list of licenses",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseModel.class))),
                            description = "Returns all license details"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setLicenses(
            @NotNull final List<String> licenseKeys);

}
