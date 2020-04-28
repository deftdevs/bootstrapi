package de.aservo.atlassian.confapi.exception;

import de.aservo.atlassian.confapi.exception.api.AbstractSuccessWebException;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;

public class NoContentException extends AbstractSuccessWebException {

    public NoContentException(String message) {
        super(message);
    }

    public NoContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoContentException(Throwable cause) {
        super(cause);
    }

    @Override
    public Response.Status getStatus() {
        return NO_CONTENT;
    }

}
