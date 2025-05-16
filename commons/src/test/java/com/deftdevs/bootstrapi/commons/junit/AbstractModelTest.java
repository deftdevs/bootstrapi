package com.deftdevs.bootstrapi.commons.junit;

import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlRootElement;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractModelTest extends AbstractTest {

    private static final String CLASS_SUFFIX = "Model";

    @Test
    @Disabled
    void beanClassNameShouldEndWithSuffixModel() {
        final String beanClassName = getBaseClass().getSimpleName();
        assertTrue(beanClassName.endsWith(CLASS_SUFFIX), "The model class name should end with suffix " + CLASS_SUFFIX);
    }

    @Test
    @Disabled
    void beanClassNameAndXmlRootElementShouldMatch() {
        final String beanClassName = getBaseClass().getSimpleName();
        final String beanClassBaseName = beanClassName.substring(0, beanClassName.length() - CLASS_SUFFIX.length());
        final XmlRootElement xmlRootElement = getBaseClass().getAnnotation(XmlRootElement.class);
        assertNotNull(xmlRootElement);
        assertEquals(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, beanClassBaseName), xmlRootElement.name(),
                "The model class camel-case base name and the xml root element kebab-case base name should match");
    }

}
