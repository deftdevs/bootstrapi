package com.deftdevs.bootstrapi.commons.junit;

import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlRootElement;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractModelTest extends AbstractTest {

    private static final String CLASS_SUFFIX = "Model";

    @Test
    void beanClassNameShouldEndWithSuffixModel() {
        final String beanClassName = getBaseClass().getSimpleName();
        assertTrue(beanClassName.endsWith(CLASS_SUFFIX), "The model class name should end with suffix " + CLASS_SUFFIX);
    }

    @Test
    void beanClassNameAndXmlRootElementShouldMatch() {
        final XmlRootElement xmlRootElement = getBaseClass().getAnnotation(XmlRootElement.class);
        assertNotNull(xmlRootElement);
        assertEquals(getExpectedXmlRootElementName(), xmlRootElement.name(),
                "The xml root element name should match the expected name");
    }

    /**
     * The kebab-case version of the model class base name by default. Tests of
     * models with an intentionally different xml root element name, for example
     * type discriminators or config keys, override this with the same BootstrAPI
     * constant the model annotation uses.
     */
    protected String getExpectedXmlRootElementName() {
        final String beanClassName = getBaseClass().getSimpleName();
        final String beanClassBaseName = beanClassName.substring(0, beanClassName.length() - CLASS_SUFFIX.length());
        return beanClassBaseName.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase(Locale.ROOT);
    }

}
