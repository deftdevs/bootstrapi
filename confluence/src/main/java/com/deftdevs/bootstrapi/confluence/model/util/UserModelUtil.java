package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.user.User;
import com.deftdevs.bootstrapi.commons.model.UserModel;

public class UserModelUtil {

    public static UserModel toUserModel(
            final User user) {

        return UserModel.builder()
            .username(user.getName())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .build();
    }

    // Make sure to return a ConfluenceUser here to that unit tests are working
    public static User toUser(
            final UserModel userModel) {

        return new ConfluenceUserImpl(
                userModel.getUsername(),
                userModel.getFullName(),
                userModel.getEmail()
        );
    }

    private UserModelUtil() {
    }

}
