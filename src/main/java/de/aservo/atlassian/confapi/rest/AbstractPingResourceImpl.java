package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.rest.api.PingResource;

import javax.ws.rs.core.Response;

public abstract class AbstractPingResourceImpl implements PingResource {

    @Override
    public Response getPing() {
        return Response.ok(PONG).build();
    }

}
