package de.aservo.atlassian.confapi.exception.api;

public abstract class AbstractServerErrorWebException extends AbstractWebException {

    public AbstractServerErrorWebException(String message) {
        super(message);
    }

    public AbstractServerErrorWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractServerErrorWebException(Throwable cause) {
        super(cause);
    }

}
