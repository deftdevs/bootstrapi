package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.user.EntityException;
import com.atlassian.user.User;
import com.atlassian.user.UserManager;
import com.atlassian.user.impl.DefaultUser;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.confluence.model.util.UserModelUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

import static com.deftdevs.bootstrapi.commons.util.ModelValidationUtil.validate;

@Component
public class UsersServiceImpl implements UsersService {

    private final UserManager userManager;

    private final UserAccessor userAccessor;

    @Inject
    public UsersServiceImpl(
            @ComponentImport final UserManager userManager,
            @ComponentImport final UserAccessor userAccessor) {

        this.userManager = userManager;
        this.userAccessor = userAccessor;
    }

    @Override
    public UserModel getUser(
            final String userName) throws NotFoundException {

        final User user = findConfluenceUser(userName);
        return UserModelUtil.toUserModel(user);
    }

    @Override
    public UserModel getUser(
            final long directoryId,
            final String username) {

        // Not yet implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public UserModel updateUser(
            final String userName,
            final UserModel userModel) {

        validate(userModel);
        User user = findConfluenceUser(userName);

        if (userModel.getUsername() != null && !userName.equals(userModel.getUsername())) {
            try {
                user = userAccessor.renameUser((ConfluenceUser)user, userModel.getUsername());
            } catch (EntityException e) {
                throw new BadRequestException(String.format("Error trying to update username for user: %s. " +
                        "New username might already exist, operation is not permitted or the crowd service could not " +
                        "handle the request.", user.getName()));
            }
        }
        // userManager.saveUser will convert this user into a ConfluenceUser
        final DefaultUser updateUser = new DefaultUser(user);

        if (userModel.getFullName() != null) {
            updateUser.setFullName(userModel.getFullName());
        }
        if (userModel.getEmail() != null) {
            updateUser.setEmail(userModel.getEmail());
        }
        if (userModel.getPassword() != null) {
            updatePassword(userName, userModel.getPassword());
        }

        try {
            userManager.saveUser(updateUser);
        } catch (EntityException e) {
            throw new BadRequestException(String.format("User %s cannot be updated", userName));
        }

        return UserModelUtil.toUserModel(updateUser);
    }

    @Override
    public UserModel setUser(
            final long directoryId,
            final UserModel userModel) {

        // Not yet implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UserModel> setUsers(
            final long directoryId,
            final List<UserModel> userModels) {

        // Not yet implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public UserModel updatePassword(
            final String username,
            final String password) throws NotFoundException, BadRequestException {

        final User user = findConfluenceUser(username);

        try {
            userManager.alterPassword(user, password);
        } catch (EntityException e) {
            throw new BadRequestException(String.format("Password for user %s cannot be set", username));
        }

        return UserModelUtil.toUserModel(user);
    }

    private User findConfluenceUser(
            final String username) {

        final ConfluenceUser confluenceUser;

        try {
            // user *should* always be ConfluenceUser if not null
            confluenceUser = (ConfluenceUser) userManager.getUser(username);
        } catch (EntityException | ClassCastException e) {
            throw new NotFoundException(String.format("User %s cannot be found", username));
        }

        return confluenceUser;
    }

}
