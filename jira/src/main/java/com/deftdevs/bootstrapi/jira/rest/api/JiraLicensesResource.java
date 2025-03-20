package com.deftdevs.bootstrapi.jira.rest.api;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.rest.api.LicensesResource;
import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

public interface JiraLicensesResource extends LicensesResource {

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            tags = { BootstrAPI.LICENSES },
            summary = "Set licenses"
            // description = "Upon successful request, returns a `LicensesBean` object containing license details. Be aware that `products` collection of the `LicenseBean` contains the product display names, not the product key names",
            // responses = {
            //         @ApiResponse(
            //                 responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LicenseBean.class))),
            //                 description = "Returns a list of all licenses (NOTE: for all applications except Jira this will return a single license)"
            //         ),
            //         @ApiResponse(
            //                 responseCode = "default", content = @Content(schema = @Schema(implementation = ErrorCollection.class)),
            //                 description = "Returns a list of error messages."
            //         ),
            // }
    )
    Response setLicenses(
            Collection<LicenseBean> licenseBeans);

}
