package de.aservo.atlassian.confapi.exception.api;

public abstract class AbstractClientErrorWebException extends AbstractWebException {

    public AbstractClientErrorWebException(String message) {
        super(message);
    }

    public AbstractClientErrorWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractClientErrorWebException(Throwable cause) {
        super(cause);
    }

}
