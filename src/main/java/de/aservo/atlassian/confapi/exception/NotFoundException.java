package de.aservo.atlassian.confapi.exception;

import de.aservo.atlassian.confapi.exception.api.AbstractClientErrorWebException;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class NotFoundException extends AbstractClientErrorWebException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public Response.Status getStatus() {
        return NOT_FOUND;
    }

}
