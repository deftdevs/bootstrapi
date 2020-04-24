package de.aservo.atlassian.confapi.junit;

import static de.aservo.atlassian.confapi.util.StringUtil.baseName;
import static org.junit.Assert.assertTrue;

public abstract class AbstractTest {

    private static final String TEST_CLASS_SUFFIX = "Test";

    private Class<?> baseClass = null;

    public Class<?> getBaseClass() {
        if (baseClass == null) {
            baseClass = findBaseClass();
        }

        return baseClass;
    }

    private Class<?> findBaseClass() {
        final String testClassName = getClass().getCanonicalName();
        assertTrue("The test class should end with suffix Test",
                testClassName.endsWith(TEST_CLASS_SUFFIX));

        final Class<?> baseClass;
        final String baseClassName = baseName(testClassName, TEST_CLASS_SUFFIX);

        try {
            baseClass = Class.forName(baseClassName);
        } catch (ClassNotFoundException ignored) {
            throw new AssertionError("The base class of this test should be named " + baseClassName);
        }

        return baseClass;
    }

}
