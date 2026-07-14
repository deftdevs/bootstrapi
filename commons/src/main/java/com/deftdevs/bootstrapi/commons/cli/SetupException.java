package com.deftdevs.bootstrapi.commons.cli;

public class SetupException extends RuntimeException {

    public SetupException(
            final String message) {

        super(message);
    }

    public SetupException(
            final String message,
            final Throwable cause) {

        super(message, cause);
    }
}
