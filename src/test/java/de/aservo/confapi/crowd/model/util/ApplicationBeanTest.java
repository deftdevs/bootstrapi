package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.model.application.Application;
import com.atlassian.crowd.model.application.ApplicationType;
import de.aservo.confapi.crowd.model.util.ApplicationBeanUtil;
import de.aservo.confapi.crowd.model.ApplicationBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static de.aservo.confapi.crowd.model.ApplicationBean.EXAMPLE_1;
import static de.aservo.confapi.crowd.model.util.ApplicationBeanUtil.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationBeanTest {

    @Test
    public void testMapTypesApplication() {
        for (ApplicationType type : ApplicationType.values()) {
            assertEquals(type, toApplicationType(toApplicationBeanType(type)));
        }
        assertNull(toApplicationType(toApplicationBeanType(null)));
    }

    @Test
    public void testMapTypesApplicationBean() {
        for (ApplicationBean.ApplicationType type : ApplicationBean.ApplicationType.values()) {
            assertEquals(type, toApplicationBeanType(toApplicationType(type)));
        }
        assertNull(toApplicationBeanType(toApplicationType(null)));
    }

    @Test
    public void testToApplication() {
        Application application = toApplication(EXAMPLE_1);

        assertEquals(EXAMPLE_1.getName(), application.getName());
        assertEquals(EXAMPLE_1.getType(), toApplicationBeanType(application.getType()));
        assertEquals(EXAMPLE_1.getDescription(), application.getDescription());
        assertEquals(EXAMPLE_1.getActive().booleanValue(), application.isActive());
        assertEquals(application.getCredential().getCredential(), EXAMPLE_1.getPassword());
        assertEquals(EXAMPLE_1.getRemoteAddresses(), toStringCollection(application.getRemoteAddresses()));
    }

    @Test
    public void testToApplicationBean() {
        Application application = toApplication(EXAMPLE_1);
        ApplicationBean applicationBean = toApplicationBean(application);

        assertEquals(application.getName(), applicationBean.getName());
        assertEquals(application.getDescription(), applicationBean.getDescription());
        assertEquals(application.isActive(), applicationBean.getActive().booleanValue());
        assertEquals(toApplicationBeanType(application.getType()), applicationBean.getType());
        assertEquals(application.getRemoteAddresses(), toAddressSet(applicationBean.getRemoteAddresses()));
    }
}
