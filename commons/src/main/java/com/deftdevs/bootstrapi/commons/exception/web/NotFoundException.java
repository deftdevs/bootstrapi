package com.deftdevs.bootstrapi.commons.exception.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

public class NotFoundException extends WebApplicationException {

    private static final Response.Status STATUS = NOT_FOUND;

    public NotFoundException(String message) {
        super(new Exception(message), STATUS);
    }

    public NotFoundException(String entity, String name) {
        this(String.format("%s with name '%s' could not be found", entity, name));
    }

    public NotFoundException(String entity, long id) {
        this(String.format("%s with ID '%d' could not be found", entity, id));
    }

    public NotFoundException(Throwable cause) {
        super(cause, STATUS);
    }

}
