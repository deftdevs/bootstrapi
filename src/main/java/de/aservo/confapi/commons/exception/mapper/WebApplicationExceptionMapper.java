package de.aservo.confapi.commons.exception.mapper;

import de.aservo.confapi.commons.model.ErrorCollection;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    public Response toResponse(WebApplicationException e) {
        final ErrorCollection errorCollection = new ErrorCollection();
        // there is no way around the cause in the WebApplicationException so that messages always start with the exception name
        errorCollection.addErrorMessage(e.getMessage().replaceFirst("([^:]*: )", ""));
        return Response.status(e.getResponse().getStatus()).entity(errorCollection).build();
    }

}
