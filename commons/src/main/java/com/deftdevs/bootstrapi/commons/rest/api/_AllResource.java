package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;

public interface _AllResource<_AllModel extends _AllModelAccessor> {

    /**
     * Product implementations must override this method and declare the
     * {@code @Operation} annotation with their concrete {@code _AllModel} as
     * the response schema. The annotation is not declared here because
     * OpenAPI only reads the overriding method's annotations, so an
     * interface-level copy would be shadowed and silently drift.
     */
    @PUT
    Response setAll(
            @NotNull final _AllModel bean);

}
