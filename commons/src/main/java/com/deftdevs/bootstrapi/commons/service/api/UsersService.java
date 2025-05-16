package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.UserModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface UsersService {

    /**
     * Get the user.
     *
     * @param username the username
     * @return the user bean
     */
    UserModel getUser(
            @NotNull final String username);

    /**
     * Get the user.
     *
     * @param directoryId the directory id
     * @param username the username
     * @return the user bean
     */
    UserModel getUser(
            final long directoryId,
            @NotNull final String username);

    /**
     * Set (add or update) user.
     *
     * @param directoryId the directory id
     * @param userModel the user bean
     * @return the set user bean
     */
    UserModel setUser(
            final long directoryId,
            @NotNull final UserModel userModel);

    /**
     * Set (add or update) users.
     *
     * @param directoryId the directory id
     * @param userModels the user beans
     * @return the set user beans
     */
    List<UserModel> setUsers(
            final long directoryId,
            @NotNull final List<UserModel> userModels);

    /**
     * Update the user.
     *
     * @param username the username
     * @param userModel the user bean
     * @return the updated user bean
     */
    UserModel updateUser(
            @NotNull final String username,
            @NotNull final UserModel userModel);

    /**
     * Update the user password.
     *
     * @param username the username
     * @param password the password
     * @return the updated user bean
     */
    UserModel updatePassword(
            @NotNull final String username,
            @NotNull final String password);

}
