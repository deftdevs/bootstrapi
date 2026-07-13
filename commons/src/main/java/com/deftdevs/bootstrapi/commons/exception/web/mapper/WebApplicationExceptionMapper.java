package com.deftdevs.bootstrapi.commons.exception.web.mapper;

import com.deftdevs.bootstrapi.commons.model.ErrorCollection;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    public Response toResponse(WebApplicationException e) {
        final ErrorCollection errorCollection = new ErrorCollection();

        if (e == null) {
            errorCollection.addErrorMessage("Unknown error");
            return Response.serverError().entity(errorCollection).build();
        }

        final String message;

        if (e.getCause() != null) {
            message = e.getCause().getMessage();
        } else {
            message = e.getMessage();
        }

        if (message != null) {
            // there is no way around the cause in the WebApplicationException so that messages always start
            // with the exception name, which will be stripped with the following regex
            errorCollection.addErrorMessage(message.replaceFirst("([^:]*: )", ""));
        } else {
            errorCollection.addErrorMessage(e.getClass().getSimpleName());
        }

        return Response.fromResponse(e.getResponse()).entity(errorCollection).build();
    }

}
