package de.aservo.atlassian.confapi.service.api;

import de.aservo.atlassian.confapi.model.UserBean;

import javax.validation.constraints.NotNull;

public interface UserService {

    /**
     * Get the user.
     *
     * @param userName the user name
     * @return the user bean
     */
    public UserBean getUser(
            @NotNull final String userName);

    /**
     * Update the user.
     *
     * @param userName the user name
     * @param userBean the user bean
     * @return the updated user bean
     */
    public UserBean updateUser(
            @NotNull final String userName,
            @NotNull final UserBean userBean);

    /**
     * Update the user password.
     *
     * @param userName the user name
     * @param password the password
     * @return the updated user bean
     */
    public UserBean updatePassword(
            @NotNull final String userName,
            @NotNull final String password);

}
