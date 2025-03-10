package com.deftdevs.bootstrapi.crowd.util;

import org.junit.jupiter.api.Assertions;

public class AssertUtil {

    public static void assertEquals(Object first, Object second, boolean firstParameterIsExpected) {
        if (firstParameterIsExpected) {
            Assertions.assertEquals(first, second);
        } else {
            Assertions.assertEquals(second, first);
        }
    }

    private AssertUtil() {
    }

}
