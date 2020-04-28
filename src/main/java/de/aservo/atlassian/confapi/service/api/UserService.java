package de.aservo.atlassian.confapi.service.api;

import de.aservo.atlassian.confapi.exception.BadRequestException;
import de.aservo.atlassian.confapi.exception.NotFoundException;
import de.aservo.atlassian.confapi.model.UserBean;

import javax.validation.constraints.NotNull;

public interface UserService {

    /**
     * Get the user.
     *
     * @param userName the user name
     * @return the user bean
     * @throws NotFoundException the user bean could not be found
     */
    public UserBean getUser(
            @NotNull final String userName) throws NotFoundException;

    /**
     * Update the user.
     *
     * @param userName the user name
     * @param userBean the user bean
     * @return the updated user bean
     * @throws NotFoundException   the user bean could not be found
     * @throws BadRequestException the user bean could not be updated
     */
    public UserBean updateUser(
            @NotNull final String userName,
            @NotNull final UserBean userBean) throws NotFoundException, BadRequestException;

    /**
     * Update the user password.
     *
     * @param userName the user name
     * @param password the password
     * @return the updated user bean
     * @throws NotFoundException   the user bean could not be found
     * @throws BadRequestException the user password could not be updated
     */
    public UserBean updatePassword(
            @NotNull final String userName,
            @NotNull final String password) throws NotFoundException, BadRequestException;

}
