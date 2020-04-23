package de.aservo.atlassian.confapi.model;

import com.atlassian.user.impl.DefaultUser;
import de.aservo.atlassian.confapi.junit.AbstractBeanTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserBeanTest extends AbstractBeanTest {

    @Test
    public void testParameterConstructor() {
        DefaultUser user = new DefaultUser("test", "test user", "user@user.de");
        UserBean bean = new UserBean(user);

        assertNotNull(bean);
        assertEquals(bean.getUserName(), user.getName());
        assertEquals(bean.getFullName(), user.getFullName());
        assertEquals(bean.getEmail(), user.getEmail());
        assertNull(bean.getPassword());
    }

}
