package de.aservo.atlassian.confapi.exception.api;

public abstract class AbstractSuccessWebException extends AbstractWebException {

    public AbstractSuccessWebException(String message) {
        super(message);
    }

    public AbstractSuccessWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractSuccessWebException(Throwable cause) {
        super(cause);
    }

}
