package com.deftdevs.bootstrapi.commons.cli;

/**
 * Environment access for the setup CLIs. System properties take precedence
 * over environment variables so tests (and ad hoc runs) can override values
 * without touching the process environment.
 */
public final class SetupEnv {

    public static String require(
            final String name) {

        final String value = get(name, null);
        if (value == null || value.isBlank()) {
            throw new SetupException("Required environment variable '" + name + "' is not set");
        }
        return value;
    }

    public static String get(
            final String name,
            final String defaultValue) {

        final String property = System.getProperty(name);
        if (property != null) {
            return property;
        }

        final String env = System.getenv(name);
        return env != null ? env : defaultValue;
    }

    private SetupEnv() {
    }
}
