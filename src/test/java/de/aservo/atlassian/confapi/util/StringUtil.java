package de.aservo.atlassian.confapi.util;

public class StringUtil {

    public static String baseName(
            final String name,
            final String suffix) {

        return name.substring(0, name.length() - suffix.length());
    }

    private StringUtil() {
    }

}
