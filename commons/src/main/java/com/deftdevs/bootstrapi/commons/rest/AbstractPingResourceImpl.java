package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.rest.api.PingResource;

import javax.ws.rs.core.Response;

public abstract class AbstractPingResourceImpl implements PingResource {

    @Override
    public Response getPing() {
        return Response.ok(PONG).build();
    }

}
