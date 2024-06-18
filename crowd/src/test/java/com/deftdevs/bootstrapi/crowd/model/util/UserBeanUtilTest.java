package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.model.user.ImmutableUser;
import com.atlassian.crowd.model.user.User;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserBeanUtilTest {

    @Test
    public void testToUserBean() {
        final User user = getTestUser();
        final UserBean userBean = UserBeanUtil.toUserBean(user);

        assertNotNull(userBean);
        assertEquals(user.getName(), userBean.getUsername());
        assertEquals(user.getDisplayName(), userBean.getFullName());
        assertEquals(user.getEmailAddress(), userBean.getEmail());
    }

    private User getTestUser() {
        return ImmutableUser.builder(1, "test")
                .displayName("Test User")
                .emailAddress("test@example.com")
                .build();
    }
}
