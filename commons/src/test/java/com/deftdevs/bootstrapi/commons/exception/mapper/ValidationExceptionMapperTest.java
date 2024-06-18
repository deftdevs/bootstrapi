package de.aservo.confapi.commons.exception.mapper;

import de.aservo.confapi.commons.model.ErrorCollection;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationExceptionMapperTest {

    private static final String MESSAGES = "ValidationError1\nValidationError2";

    @Test
    void testResponse() {
        final ValidationExceptionMapper validationExceptionMapper = new ValidationExceptionMapper();
        final ValidationException validationException = new ValidationException(MESSAGES);
        final Response response = validationExceptionMapper.toResponse(validationException);
        final ErrorCollection errorCollection = (ErrorCollection) response.getEntity();

        assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus(), "Validation exceptions should be mapped to bad request exceptions");
        assertEquals(MESSAGES.split("\n").length, errorCollection.getErrorMessages().size(), "The response error collection size is wrong");
    }

}
