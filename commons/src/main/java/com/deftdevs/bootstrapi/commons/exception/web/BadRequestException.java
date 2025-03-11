package com.deftdevs.bootstrapi.commons.exception.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class BadRequestException extends WebApplicationException {

    private static final Response.Status STATUS = BAD_REQUEST;

    public BadRequestException(String message) {
        super(new Exception(message), STATUS);
    }

    public BadRequestException(Throwable cause) {
        super(cause, STATUS);
    }

}
