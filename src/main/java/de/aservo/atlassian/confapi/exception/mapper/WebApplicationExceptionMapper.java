package de.aservo.atlassian.confapi.exception.mapper;

import de.aservo.atlassian.confapi.model.ErrorCollection;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    public Response toResponse(WebApplicationException e) {
        final ErrorCollection errorCollection = new ErrorCollection();
        errorCollection.addErrorMessage(e.getMessage());
        return Response.status(e.getResponse().getStatus()).entity(errorCollection).build();
    }

}
