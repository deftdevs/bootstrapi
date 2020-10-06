package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.UserBean;

import javax.validation.constraints.NotNull;

public interface UsersService {

    /**
     * Get the user.
     *
     * @param username the user name
     * @return the user bean
     */
    UserBean getUser(
            @NotNull final String username);

    /**
     * Update the user.
     *
     * @param username the user name
     * @param userBean the user bean
     * @return the updated user bean
     */
    UserBean updateUser(
            @NotNull final String username,
            @NotNull final UserBean userBean);

    /**
     * Update the user password.
     *
     * @param username the user name
     * @param password the password
     * @return the updated user bean
     */
    UserBean updatePassword(
            @NotNull final String username,
            @NotNull final String password);

}
