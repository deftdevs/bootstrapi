package de.aservo.atlassian.confapi.exception.mapper;

import de.aservo.atlassian.confapi.model.ErrorCollection;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    public Response toResponse(RuntimeException e) {
        final ErrorCollection errorCollection = new ErrorCollection();
        errorCollection.addErrorMessage(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorCollection).build();
    }

}
