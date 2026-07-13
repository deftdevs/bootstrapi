package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.model.AbstractSettingsModel;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

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
