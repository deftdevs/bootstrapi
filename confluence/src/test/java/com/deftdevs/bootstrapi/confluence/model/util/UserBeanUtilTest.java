package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.user.User;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserBeanUtilTest {

    @Test
    void testToUserBean() {
        final ConfluenceUser user = new ConfluenceUserImpl("user", "User Name", "user@localhost");
        final UserBean userBean = UserBeanUtil.toUserBean(user);

        assertEquals(user.getName(), userBean.getUsername());
        assertEquals(user.getFullName(), userBean.getFullName());
        assertEquals(user.getEmail(), userBean.getEmail());
    }

    @Test
    void testToUser() {
        final UserBean userBean = UserBean.EXAMPLE_1;
        final User user = UserBeanUtil.toUser(userBean);

        assertEquals(userBean.getUsername(), user.getName());
        assertEquals(userBean.getFullName(), user.getFullName());
        assertEquals(userBean.getEmail(), user.getEmail());
    }

}
