package de.aservo.atlassian.confapi.exception.api;

import javax.ws.rs.core.Response;

public abstract class AbstractWebException extends Exception {

    public AbstractWebException(String message) {
        super(message);
    }

    public AbstractWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractWebException(Throwable cause) {
        super(cause);
    }

    public abstract Response.Status getStatus();

}
