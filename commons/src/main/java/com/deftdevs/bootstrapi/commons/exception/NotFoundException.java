package de.aservo.confapi.commons.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class NotFoundException extends WebApplicationException {

    private static final Response.Status STATUS = NOT_FOUND;

    public NotFoundException(String message) {
        super(new Exception(message), STATUS);
    }

    public NotFoundException(Throwable cause) {
        super(cause, STATUS);
    }

}
