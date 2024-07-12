package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.model.user.ImmutableUser;
import com.atlassian.crowd.model.user.User;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
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
