package de.aservo.atlassian.confapi.exception;

import de.aservo.atlassian.confapi.exception.api.AbstractServerErrorWebException;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

public class InternalServerErrorException extends AbstractServerErrorWebException {

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause);
    }

    @Override
    public Response.Status getStatus() {
        return INTERNAL_SERVER_ERROR;
    }

}
