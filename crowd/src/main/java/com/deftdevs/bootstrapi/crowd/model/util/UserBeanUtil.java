package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.model.user.User;
import de.aservo.confapi.commons.model.UserBean;

import javax.annotation.Nonnull;

public class UserBeanUtil {

    /**
     * Build user bean.
     *
     * @param user the user
     * @return the user bean
     */
    @Nonnull
    public static UserBean toUserBean(
            @Nonnull final User user) {

        final UserBean userBean = new UserBean();
        userBean.setUsername(user.getName());
        userBean.setFirstName(user.getFirstName());
        userBean.setLastName(user.getLastName());
        userBean.setFullName(user.getDisplayName());
        userBean.setEmail(user.getEmailAddress());
        userBean.setActive(user.isActive());

        return userBean;
    }

    private UserBeanUtil() {
    }

}
