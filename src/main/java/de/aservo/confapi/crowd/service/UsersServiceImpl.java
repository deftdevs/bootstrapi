package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.exception.InvalidCredentialException;
import com.atlassian.crowd.exception.InvalidUserException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.UserAlreadyExistsException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.directory.DirectoryPermissionException;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.model.user.UserTemplate;
import com.atlassian.crowd.model.user.UserTemplateWithAttributes;
import com.atlassian.crowd.search.EntityDescriptor;
import com.atlassian.crowd.search.builder.QueryBuilder;
import com.atlassian.crowd.search.query.entity.EntityQuery;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import de.aservo.confapi.commons.model.UserBean;
import de.aservo.confapi.commons.service.api.UsersService;
import de.aservo.confapi.crowd.exception.NotFoundExceptionForDirectory;
import de.aservo.confapi.crowd.exception.NotFoundExceptionForUser;
import de.aservo.confapi.crowd.model.util.UserBeanUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ExportAsService(UsersService.class)
public class UsersServiceImpl implements UsersService {

    @ComponentImport
    private final DirectoryManager directoryManager;

    @Inject
    public UsersServiceImpl(
            final DirectoryManager directoryManager) {

        this.directoryManager = directoryManager;
    }

    @Override
    public UserBean getUser(
            final long directoryId,
            final String username) {

        final User user = findUser(directoryId, username);

        if (user == null) {
            throw new NotFoundExceptionForUser(username);
        }

        return UserBeanUtil.toUserBean(user);
    }

    public UserBean setUser(
            final long directoryId,
            final UserBean userBean) {

        final User user = findUser(directoryId, userBean.getUsername());

        if (user == null) {
            return addUser(directoryId, userBean);
        }

        return updateUser(directoryId, user.getName(), userBean);
    }

    @Override
    public List<UserBean> setUsers(
            final long directoryId,
            final Collection<UserBean> userBeans) {

        return userBeans.stream()
                .map(userBean -> setUser(directoryId, userBean))
                .collect(Collectors.toList());
    }

    public UserBean addUser(
            final long directoryId,
            final UserBean userBean) {

        return addUser(directoryId, userBean.getUsername(), userBean);
    }

    UserBean addUser(
            final long directoryId,
            final String username,
            final UserBean userBean) {

        User user = findUser(directoryId, userBean.getUsername());

        if (user != null) {
            throw new BadRequestException(String.format("User '%s' already exists", userBean.getUsername()));
        }

        if (userBean.getUsername() == null) {
            if (username == null) {
                throw new BadRequestException("Cannot create user, username is required");
            }

            userBean.setUsername(username);
        }

        if (username != null && !userBean.getUsername().equals(username)) {
            throw new BadRequestException("Cannot create user, two different usernames provided");
        }

        if (userBean.getFirstName() == null || userBean.getLastName() == null || userBean.getFullName() == null || userBean.getEmail() == null) {
            throw new BadRequestException("Cannot create user, first name, last name, display (full) name and email are required");
        }

        if (userBean.getPassword() == null) {
            throw new BadRequestException("Cannot create user, password is required");
        }

        final UserTemplateWithAttributes userTemplate = new UserTemplateWithAttributes(userBean.getUsername(), directoryId);
        userTemplate.setFirstName(userBean.getFirstName());
        userTemplate.setLastName(userBean.getLastName());
        userTemplate.setDisplayName(userBean.getFullName());
        userTemplate.setEmailAddress(userBean.getEmail());
        userTemplate.setActive(userBean.getActive() != null || userBean.getActive());

        final PasswordCredential passwordCredential = PasswordCredential.unencrypted(userBean.getPassword());

        try {
            return UserBeanUtil.toUserBean(directoryManager.addUser(directoryId, userTemplate, passwordCredential));
        } catch (InvalidCredentialException | InvalidUserException | DirectoryPermissionException | UserAlreadyExistsException e) {
            throw new BadRequestException(e);
        } catch (DirectoryNotFoundException e) {
            throw new NotFoundExceptionForDirectory(directoryId);
        } catch (OperationFailedException e) {
            throw new InternalServerErrorException(e);
        }
    }

    UserBean updateUser(
            final long directoryId,
            final String username,
            final UserBean userBean) {

        User user = findUser(directoryId, username);

        if (user == null) {
            throw new NotFoundExceptionForUser(username);
        }

        if (userBean.getUsername() != null && !username.equals(userBean.getUsername())) {
            user = renameUser(user, userBean.getUsername());
        }

        if (userBean.getFirstName() != null || userBean.getLastName() != null || userBean.getFullName() != null || userBean.getEmail() != null) {
            user = updateUser(user.getDirectoryId(), getUserTemplate(user, userBean));
        }

        if (userBean.getPassword() != null) {
            updatePassword(user, userBean.getPassword());
        }

        return UserBeanUtil.toUserBean(user);
    }

    @Nullable
    User findUser(
            final long directoryId,
            final String username) {

        try {
            return directoryManager.findUserByName(directoryId, username);
        } catch (DirectoryNotFoundException e) {
            throw new NotFoundExceptionForDirectory(directoryId);
        } catch (UserNotFoundException e) {
            // Ignore, will be handled differently
        } catch (OperationFailedException e) {
            throw new InternalServerErrorException(e);
        }

        return null;
    }

    /**
     * @deprecated (due to random directory selection)
     */
    @Override
    @Deprecated
    public UserBean getUser(
            final String username) {

        final User user = findUserAllDirectories(username);

        if (user == null) {
            throw new NotFoundExceptionForUser(username);
        }

        return UserBeanUtil.toUserBean(user);
    }

    /*
     * All the deprecated methods...
     */

    /**
     * @deprecated (due to random directory selection)
     */
    @Override
    @Deprecated
    public UserBean updateUser(
            final String username,
            final UserBean userBean) {

        final User user = findUserAllDirectories(username);

        if (user == null) {
            throw new NotFoundExceptionForUser(username);
        }

        return updateUser(user.getDirectoryId(), username, userBean);
    }

    /**
     * @deprecated (due to random directory selection)
     */
    @Override
    @Deprecated
    public UserBean updatePassword(
            final String username,
            final String password) {

        final User user = findUserAllDirectories(username);

        if (user == null) {
            throw new NotFoundExceptionForUser(username);
        }

        updatePassword(user, password);
        return UserBeanUtil.toUserBean(user);
    }

    /**
     * @deprecated (due to random directory selection)
     */
    @Nullable
    @Deprecated
    private User findUserAllDirectories(
            final String username) {

        for (Directory directory : findDirectories()) {
            try {
                return directoryManager.findUserByName(directory.getId(), username);
            } catch (UserNotFoundException e) {
                // Ignore, will be handled below
            } catch (DirectoryNotFoundException | OperationFailedException e) {
                throw new InternalServerErrorException(e);
            }
        }

        return null;
    }

    @NotNull
    private User renameUser(
            final User username,
            final String newUsername) {

        try {
            return directoryManager.renameUser(username.getDirectoryId(), username.getName(), newUsername);
        } catch (DirectoryPermissionException | UserAlreadyExistsException | InvalidUserException e) {
            // A permission exception should only happen if we try change the name
            // of a user in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (DirectoryNotFoundException | UserNotFoundException | OperationFailedException e) {
            // At this point, we know the user exists, thus directory or user not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    @NotNull
    private User updateUser(
            final long directoryId,
            final UserTemplate userTemplate) {

        try {
            return directoryManager.updateUser(directoryId, userTemplate);
        } catch (DirectoryPermissionException | InvalidUserException e) {
            // A permission exception should only happen if we try change the name
            // of a user in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (DirectoryNotFoundException | UserNotFoundException | OperationFailedException e) {
            // At this point, we know the user exists, thus directory or user not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    void updatePassword(
            final User user,
            final String password) {

        try {
            directoryManager.updateUserCredential(user.getDirectoryId(), user.getName(), PasswordCredential.unencrypted(password));
        } catch (DirectoryPermissionException | InvalidCredentialException e) {
            // A permission exception should only happen if we try to update the password
            // of a user in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (DirectoryNotFoundException | UserNotFoundException | OperationFailedException e) {
            // At this point, we know the user exists, thus directory or user not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    private static UserTemplate getUserTemplate(
            final User user,
            final UserBean userBean) {

        final UserTemplate userTemplate = new UserTemplate(user);

        if (userBean.getFirstName() != null) {
            userTemplate.setFirstName(userBean.getFirstName());
        }

        if (userBean.getLastName() != null) {
            userTemplate.setLastName(userBean.getLastName());
        }

        if (userBean.getFullName() != null) {
            userTemplate.setDisplayName(userBean.getFullName());
        }

        if (userBean.getEmail() != null) {
            userTemplate.setEmailAddress(userBean.getEmail());
        }

        if (userBean.getActive() != null) {
            userTemplate.setActive(userBean.getActive());
        }

        return userTemplate;
    }

    /**
     * @deprecated (due to random directory selection)
     */
    @Deprecated
    private List<Directory> findDirectories() {
        final EntityQuery<Directory> directoryEntityQuery = QueryBuilder.queryFor(Directory.class, EntityDescriptor.directory())
                .returningAtMost(EntityQuery.ALL_RESULTS);

        return directoryManager.searchDirectories(directoryEntityQuery);
    }

}
