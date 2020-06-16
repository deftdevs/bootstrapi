package de.aservo.confapi.crowd.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface DirectoriesResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getDirectory(
            @PathParam("id") final long id);

}
