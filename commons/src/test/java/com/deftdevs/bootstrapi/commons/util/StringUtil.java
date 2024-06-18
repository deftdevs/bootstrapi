package de.aservo.confapi.commons.util;

public class StringUtil {

    public static String baseName(
            final String name,
            final String suffix) {

        return name.substring(0, name.length() - suffix.length());
    }

    private StringUtil() {
    }

}
