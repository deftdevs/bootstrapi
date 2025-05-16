package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.user.User;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserModelUtilTest {

    @Test
    void testToUserModel() {
        final ConfluenceUser user = new ConfluenceUserImpl("user", "User Name", "user@localhost");
        final UserModel userModel = UserModelUtil.toUserModel(user);

        assertEquals(user.getName(), userModel.getUsername());
        assertEquals(user.getFullName(), userModel.getFullName());
        assertEquals(user.getEmail(), userModel.getEmail());
    }

    @Test
    void testToUser() {
        final UserModel userModel = UserModel.EXAMPLE_1;
        final User user = UserModelUtil.toUser(userModel);

        assertEquals(userModel.getUsername(), user.getName());
        assertEquals(userModel.getFullName(), user.getFullName());
        assertEquals(userModel.getEmail(), user.getEmail());
    }

}
