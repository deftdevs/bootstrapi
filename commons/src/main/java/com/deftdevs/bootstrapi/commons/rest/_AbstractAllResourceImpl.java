package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.rest.api._AllResource;
import com.deftdevs.bootstrapi.commons.service.api._AllService;

import javax.ws.rs.core.Response;

public abstract class _AbstractAllResourceImpl<_AllModel>
        implements _AllResource<_AllModel> {

    private final _AllService<_AllModel> allService;

    public _AbstractAllResourceImpl(
            final _AllService<_AllModel> allService) {

        this.allService = allService;
    }

    public Response setAll(
            final _AllModel allModel) {

        return Response.ok(allService.setAll(allModel)).build();
    }
}
