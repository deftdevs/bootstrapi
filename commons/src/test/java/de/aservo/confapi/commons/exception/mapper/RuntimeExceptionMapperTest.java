package de.aservo.confapi.commons.exception.mapper;

import de.aservo.confapi.commons.model.ErrorCollection;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RuntimeExceptionMapperTest {

    private static final String MESSAGE = "Message";

    @Test
    void testResponse() {
        final RuntimeExceptionMapper runtimeExceptionMapper = new RuntimeExceptionMapper();
        final RuntimeException runtimeException = new NullPointerException(MESSAGE);
        final Response response = runtimeExceptionMapper.toResponse(runtimeException);
        final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();

        assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus(), "Runtime exceptions should be mapped to internal server error");
        assertEquals(1, errorCollection.getErrorMessages().size(), "The response error collection size is wrong");
    }

}
