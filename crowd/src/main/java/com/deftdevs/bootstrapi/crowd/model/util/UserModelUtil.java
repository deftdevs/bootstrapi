package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.crowd.model.user.User;
import com.deftdevs.bootstrapi.commons.model.UserModel;

import javax.annotation.Nonnull;

public class UserModelUtil {

    /**
     * Build user bean.
     *
     * @param user the user
     * @return the user bean
     */
    @Nonnull
    public static UserModel toUserModel(
            @Nonnull final User user) {

        final UserModel userModel = new UserModel();
        userModel.setUsername(user.getName());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setFullName(user.getDisplayName());
        userModel.setEmail(user.getEmailAddress());
        userModel.setActive(user.isActive());

        return userModel;
    }

    private UserModelUtil() {
    }

}
