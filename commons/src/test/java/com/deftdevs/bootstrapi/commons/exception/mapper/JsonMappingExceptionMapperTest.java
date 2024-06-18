package com.deftdevs.bootstrapi.commons.exception.mapper;

import com.deftdevs.bootstrapi.commons.model.ErrorCollection;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonMappingExceptionMapperTest {

    private static final String MESSAGE = "Message";

    @Test
    void testResponse() {
        final JsonMappingExceptionMapper jsonMappingExceptionMapper = new JsonMappingExceptionMapper();
        final JsonMappingException jsonMappingException = new JsonMappingException(MESSAGE);
        final Response response = jsonMappingExceptionMapper.toResponse(jsonMappingException);
        final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();

        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus(), "Json mapping exceptions should be mapped to bad request exceptions");
        assertEquals(1, errorCollection.getErrorMessages().size(), "The response error collection size is wrong");
    }

}
