package com.deftdevs.bootstrapi.commons.exception.web.mapper;

import com.deftdevs.bootstrapi.commons.model.ErrorCollection;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.xml.bind.ValidationException;
import java.util.Arrays;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    public Response toResponse(ValidationException e) {
        final ErrorCollection errorCollection = new ErrorCollection();
        errorCollection.addErrorMessages(Arrays.asList(e.getMessage().split("\n")));
        return Response.status(Response.Status.BAD_REQUEST).entity(errorCollection).build();
    }

}
