package de.aservo.confapi.commons.exception;

import de.aservo.confapi.commons.junit.AbstractExceptionTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static de.aservo.confapi.commons.exception.ServiceUnavailableException.HEADER_RETRY_AFTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class ServiceUnavailableExceptionTest extends AbstractExceptionTest {

    @Test
    void testCreateResponseWithRetryAfterHeader() {
        final int retryAfterInSeconds = 60;
        final Response response = ServiceUnavailableException.response(retryAfterInSeconds);
        assertEquals(String.valueOf(retryAfterInSeconds), response.getMetadata().getFirst(HEADER_RETRY_AFTER));
    }

    @Test
    void testCreateResponseWithoutRetryAfterHeader() {
        final Response response = ServiceUnavailableException.response(null);
        assertFalse(response.getMetadata().containsKey(HEADER_RETRY_AFTER));
    }

}
