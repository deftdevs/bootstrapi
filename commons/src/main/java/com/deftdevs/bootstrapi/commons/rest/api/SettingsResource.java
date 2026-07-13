package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.model.AbstractSettingsModel;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;

/**
 * Product implementations must override these methods and declare the
 * {@code @Operation} annotations with their concrete settings model as the
 * response schema. The annotations are not declared here because OpenAPI
 * only reads the overriding method's annotations, so an interface-level
 * copy would be shadowed and silently drift.
 */
public interface SettingsResource<S extends AbstractSettingsModel> {

    @GET
    Response getSettings();

    @PUT
    Response setSettings(
            @NotNull final S bean);

}
