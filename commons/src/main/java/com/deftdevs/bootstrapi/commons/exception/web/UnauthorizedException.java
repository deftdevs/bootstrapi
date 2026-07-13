package com.deftdevs.bootstrapi.commons.exception.web;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

public class UnauthorizedException extends WebApplicationException {

    private static final Response.Status STATUS = UNAUTHORIZED;

    public UnauthorizedException(String message) {
        super(new Exception(message), STATUS);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause, STATUS);
    }

}
