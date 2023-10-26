package de.aservo.confapi.crowd.model.util;

import com.atlassian.crowd.model.user.User;
import de.aservo.confapi.commons.model.UserBean;

import javax.annotation.Nullable;

public class UserBeanUtil {

    /**
     * Build user bean.
     *
     * @param user the user
     * @return the user bean
     */
    @Nullable
    public static UserBean toUserBean(
            @Nullable final User user) {

        if (user == null) {
            return null;
        }

        final UserBean userBean = new UserBean();
        userBean.setUsername(user.getName());
        userBean.setFullName(user.getDisplayName());
        userBean.setEmail(user.getEmailAddress());

        return userBean;
    }

    private UserBeanUtil() {
    }

}
