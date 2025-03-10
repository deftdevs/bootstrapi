package com.deftdevs.bootstrapi.commons.exception.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

public class InternalServerErrorException extends WebApplicationException {

    public static final Response.Status STATUS = INTERNAL_SERVER_ERROR;

    public InternalServerErrorException(String message) {
        super(new Exception(message), STATUS);
    }

    public InternalServerErrorException(Throwable cause) {
        super(cause, STATUS);
    }

}
