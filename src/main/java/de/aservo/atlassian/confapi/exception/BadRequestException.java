package de.aservo.atlassian.confapi.exception;

import de.aservo.atlassian.confapi.exception.api.AbstractClientErrorWebException;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class BadRequestException extends AbstractClientErrorWebException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    @Override
    public Response.Status getStatus() {
        return BAD_REQUEST;
    }

}
