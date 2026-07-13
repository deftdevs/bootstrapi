package com.deftdevs.bootstrapi.commons.rest.api;

import com.deftdevs.bootstrapi.commons.model.type._AllModelAccessor;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

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
