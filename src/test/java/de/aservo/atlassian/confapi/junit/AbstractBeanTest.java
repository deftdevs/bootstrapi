package de.aservo.atlassian.confapi.junit;

import com.google.common.base.CaseFormat;
import org.junit.Test;

import javax.xml.bind.annotation.XmlRootElement;

import static org.junit.Assert.*;

public abstract class AbstractBeanTest extends AbstractTest {

    private static final String CLASS_SUFFIX = "Bean";

    @Test
    public void beanClassNameShouldEndWithSuffixBean() {
        final String beanClassName = getBaseClass().getSimpleName();
        assertTrue("The model class name should end with suffix " + CLASS_SUFFIX,
                beanClassName.endsWith(CLASS_SUFFIX));
    }

    @Test
    public void beanClassNameAndXmlRootElementShouldMatch() {
        final String beanClassName = getBaseClass().getSimpleName();
        final String beanClassBaseName = beanClassName.substring(0, beanClassName.length() - CLASS_SUFFIX.length());
        final XmlRootElement xmlRootElement = getBaseClass().getAnnotation(XmlRootElement.class);
        assertNotNull(xmlRootElement);
        assertEquals("The model class camel-case base name and the xml root element kebab-case base name should match",
                CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, beanClassBaseName), xmlRootElement.name());
    }

}
