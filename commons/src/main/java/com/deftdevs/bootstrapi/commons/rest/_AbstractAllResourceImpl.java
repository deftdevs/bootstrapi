package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.rest.api._AllResource;
import com.deftdevs.bootstrapi.commons.service.api._AllService;

import javax.ws.rs.core.Response;

public abstract class _AbstractAllResourceImpl<_AllBean>
        implements _AllResource<_AllBean> {

    private final _AllService<_AllBean> allService;

    public _AbstractAllResourceImpl(
            final _AllService<_AllBean> allService) {

        this.allService = allService;
    }

    public Response setAll(
            final _AllBean allBean) {

        return Response.ok(allService.setAll(allBean)).build();
    }
}
