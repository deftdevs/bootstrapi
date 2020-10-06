package de.aservo.confapi.commons.rest.api;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.ErrorCollection;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface LicensesResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Get all licenses information",
            description = "Upon successful request, returns a `LicensesBean` object containing license details. Be aware that `products` collection of the `LicenseBean` contains the product display names, not the product key names",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = LicensesBean.class)),
                            description = ""
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getLicenses();

    @GET
    @Path("{product}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Gets licenses information for a single license",
            description = "Upon successful request, returns a `LicenseBean` object containing license details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = LicenseBean.class)),
                            description = ""
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response getLicense(
            @PathParam("product") @NotNull final String product);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Sets or Updates a set of licenses",
            description = "Existing license details are always cleared before setting the new licenses. Upon successful request, returns a `LicensesBean` object containing license details",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = LicensesBean.class)),
                            description = ""
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setLicenses(
            @NotNull final LicensesBean licensesBean);

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Updates a single license",
            description = "NOTE: Existing license details are always cleared before setting the new licenses.",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = LicenseBean.class)),
                            description = "Returns the updated license details"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response setLicense(
            @NotNull @PathParam("id") final String product,
            @NotNull final LicenseBean licenseBean);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { ConfAPI.LICENSES },
            summary = "Add a license",
            responses = {
                    @ApiResponse(
                            responseCode = "200", content = @Content(schema = @Schema(implementation = LicenseBean.class)),
                            description = "Returns the added license details"
                    ),
                    @ApiResponse(
                            responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
                            description = "Returns a list of error messages."
                    ),
            }
    )
    Response addLicense(
            @NotNull final LicenseBean licenseBean);

}
