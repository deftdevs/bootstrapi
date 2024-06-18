package com.deftdevs.bootstrapi.crowd.util;

import org.junit.Assert;

public class AssertUtil {

    public static void assertEquals(Object first, Object second, boolean firstParameterIsExpected) {
        if (firstParameterIsExpected) {
            Assert.assertEquals(first, second);
        } else {
            Assert.assertEquals(second, first);
        }
    }

    private AssertUtil() {
    }

}
