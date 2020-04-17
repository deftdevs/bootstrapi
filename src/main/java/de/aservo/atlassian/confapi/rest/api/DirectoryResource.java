package de.aservo.atlassian.confapi.rest.api;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import de.aservo.atlassian.confapi.model.DirectoryBean;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;

public interface DirectoryResource {

    @Operation(
            tags = { ConfAPI.DIRECTORIES },
            summary = "Get the list of user directories",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = DirectoryBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response getDirectories();

    @Operation(
            tags = { ConfAPI.DIRECTORIES },
            summary = "Add a new directory",
            description = "Any existing directory with the same name will be removed before adding the new one",
            responses = {
                    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = DirectoryBean.class))),
                    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorCollection.class)))
            }
    )
    Response addDirectory(
            boolean testConnection,
            @Nonnull final DirectoryBean directory);

}
