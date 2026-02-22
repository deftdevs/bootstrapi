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

        return UserModel.builder()
            .username(user.getName())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .fullName(user.getDisplayName())
            .email(user.getEmailAddress())
            .active(user.isActive())
            .build();
    }

    private UserModelUtil() {
    }

}
