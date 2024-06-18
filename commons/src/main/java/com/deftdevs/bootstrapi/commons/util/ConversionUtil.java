package de.aservo.confapi.commons.util;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ConversionUtil {

    private ConversionUtil() {}

    public static long toLong(String value) {
        return isNotBlank(value) ? Long.parseLong(value) : 0;
    }

    public static int toInt(String value) {
        return isNotBlank(value) ? Integer.parseInt(value) : 0;
    }

    public static double toDouble(String value) {
        return isNotBlank(value) ? Double.parseDouble(value) : 0;
    }

    public static boolean toBoolean(String value) {
        if (isNotBlank(value)) {
            String val = value.trim().toLowerCase();
            return val.equals("1") || val.equals("true") || val.equals("yes") || val.equals("ja");
        }
        return false;
    }
}
