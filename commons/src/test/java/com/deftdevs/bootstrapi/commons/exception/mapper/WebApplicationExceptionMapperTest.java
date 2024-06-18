package com.deftdevs.bootstrapi.commons.exception.mapper;

import com.deftdevs.bootstrapi.commons.exception.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebApplicationExceptionMapperTest {

    private static final String MESSAGE = "Space with key 'KEY' does not exist";

    @Test
    void testResponse() {
        final WebApplicationExceptionMapper webApplicationExceptionMapper = new WebApplicationExceptionMapper();
        final NotFoundException notFoundException = new NotFoundException(MESSAGE);
        final Response response = webApplicationExceptionMapper.toResponse(notFoundException);
        final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();
        final String errorMessage = errorCollection.getErrorMessages().iterator().next();

        assertEquals(notFoundException.getResponse().getStatus(), response.getStatus(), "Web application exceptions should be mapped to their own response status");
        assertEquals(1, errorCollection.getErrorMessages().size(), "The response error collection size is wrong");
        assertEquals(MESSAGE, errorMessage);
    }

}
