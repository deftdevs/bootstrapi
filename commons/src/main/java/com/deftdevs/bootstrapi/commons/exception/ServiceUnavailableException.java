package de.aservo.confapi.commons.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

public class ServiceUnavailableException extends WebApplicationException {

    static final Response.Status STATUS = SERVICE_UNAVAILABLE;
    static final String HEADER_RETRY_AFTER = "Retry-After";

    public ServiceUnavailableException(String message) {
        this(new Exception(message), null);
    }

    public ServiceUnavailableException(Throwable cause) {
        this(cause, null);
    }

    public ServiceUnavailableException(String message, Integer retryAfterInSeconds) {
        this(new Exception(message), retryAfterInSeconds);
    }

    public ServiceUnavailableException(Throwable cause, Integer retryAfterInSeconds) {
        super(cause, response(retryAfterInSeconds));
    }

    static Response response(Integer retryAfterInSeconds) {
        final Response.ResponseBuilder responseBuilder = Response.status(STATUS);

        if (retryAfterInSeconds != null) {
            responseBuilder.header(HEADER_RETRY_AFTER, String.valueOf(retryAfterInSeconds));
        }

        return responseBuilder.build();
    }

}
