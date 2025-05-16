package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.model.user.ImmutableUser;
import com.atlassian.crowd.model.user.User;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserModelUtilTest {

    @Test
    public void testToUserModel() {
        final User user = getTestUser();
        final UserModel userModel = UserModelUtil.toUserModel(user);

        assertNotNull(userModel);
        assertEquals(user.getName(), userModel.getUsername());
        assertEquals(user.getDisplayName(), userModel.getFullName());
        assertEquals(user.getEmailAddress(), userModel.getEmail());
    }

    private User getTestUser() {
        return ImmutableUser.builder(1, "test")
                .displayName("Test User")
                .emailAddress("test@example.com")
                .build();
    }
}
