package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.exception.*;
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
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.crowd.exception.NotFoundExceptionForDirectory;
import com.deftdevs.bootstrapi.crowd.exception.NotFoundExceptionForUser;
import com.deftdevs.bootstrapi.crowd.model.util.UserBeanUtil;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.atlassian.crowd.model.user.UserConstants.*;

@Component
@ExportAsService(UsersService.class)
public class UsersServiceImpl implements UsersService {

    @ComponentImport
    private final CrowdService crowdService;

    @ComponentImport
    private final DirectoryManager directoryManager;

    private final GroupsService groupsService;

    @Inject
    public UsersServiceImpl(
            final CrowdService crowdService,
            final DirectoryManager directoryManager,
            final GroupsService groupsService) {

        this.crowdService = crowdService;
        this.directoryManager = directoryManager;
        this.groupsService = groupsService;
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
            final List<UserBean> userBeans) {

        if (userBeans == null) {
            return Collections.emptyList();
        }

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

        final User existingUser = findUser(directoryId, userBean.getUsername());

        if (existingUser != null) {
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
        userTemplate.setActive(userBean.getActive() == null || userBean.getActive());

        final User user = createUser(directoryId, userTemplate, userBean.getPassword());
        final UserBean resultUserBean = UserBeanUtil.toUserBean(user);

        if (userBean.getGroups() != null) {
            final List<GroupBean> resultGroupBeans = addUserToGroups(directoryId, userBean.getUsername(), userBean.getGroups());
            resultUserBean.setGroups(resultGroupBeans);
        }

        return resultUserBean;
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

        final UserBean resultUserBean = UserBeanUtil.toUserBean(user);

        if (userBean.getGroups() != null) {
            final List<GroupBean> resultGroupBeans = addUserToGroups(directoryId, userBean.getUsername(), userBean.getGroups());
            resultUserBean.setGroups(resultGroupBeans);
        }

        return resultUserBean;
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
            final User user,
            final String newUsername) {

        try {
            return directoryManager.renameUser(user.getDirectoryId(), user.getName(), newUsername);
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
    User createUser(
            final long directoryId,
            final UserTemplateWithAttributes userTemplate,
            final String password) {

        final PasswordCredential passwordCredential = PasswordCredential.unencrypted(password);

        try {
            return directoryManager.addUser(directoryId, userTemplate, passwordCredential);
        } catch (InvalidCredentialException | InvalidUserException | DirectoryPermissionException |
                 UserAlreadyExistsException e) {
            // A permission exception should only happen if we try change the name
            // of a user in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (OperationFailedException | DirectoryNotFoundException e) {
            // At this point, we know the directory, thus directory not found should
            // never happen, so if it does, treat it as an internal server error
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

        // In order to be able to set a password in a declarative way, we need to reset all password-related
        // user attributes to make sure the password is expired...
        resetUserPasswordAttributes(user);

        // If the password is the same as the current one, do nothing,
        // to avoid clashing with Crowd's password history count mechanisms
        try {
            final com.atlassian.crowd.embedded.api.User authenticatedUser = crowdService.authenticate(user.getName(), password);

            // The null check is unnecessary (return would be enough), but it simplifies mocking in tests
            if (authenticatedUser != null) {
                return;
            }
        } catch (FailedAuthenticationException e) {
            // Ignore - if the password is wrong, we can actually try to update it
        }

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

    void resetUserPasswordAttributes(
            final User user) {

        final Map<String, String> userAttributes = new HashMap<>();
        userAttributes.put(INVALID_PASSWORD_ATTEMPTS, String.valueOf(0));
        userAttributes.put(REQUIRES_PASSWORD_CHANGE, String.valueOf(false));
        userAttributes.put(PASSWORD_LASTCHANGED, String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

        try {
            directoryManager.storeUserAttributes(user.getDirectoryId(), user.getName(), userAttributes.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> Collections.singleton(e.getValue()))));
        } catch (DirectoryPermissionException e) {
            // A permission exception should only happen if we try to update a user
            // in a read-only directory, so treat this as a bad request
            throw new BadRequestException(e);
        } catch (DirectoryNotFoundException | UserNotFoundException | OperationFailedException e) {
            // At this point, we know the user exists, thus directory or user not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    List<GroupBean> addUserToGroups(
            final long directoryId,
            final String username,
            final List<GroupBean> groupBeans) {

        final List<GroupBean> resultGroupBeans = new ArrayList<>();

        if (groupBeans != null) {
            for (GroupBean groupBean : groupBeans) {
                final GroupBean resultGroupBean = groupsService.setGroup(directoryId, groupBean.getName(), groupBean);

                try {
                    directoryManager.addUserToGroup(directoryId, username, groupBean.getName());
                    resultGroupBeans.add(resultGroupBean);
                } catch (DirectoryPermissionException | ReadOnlyGroupException e) {
                    throw new BadRequestException(e);
                } catch (DirectoryNotFoundException | UserNotFoundException | GroupNotFoundException |
                         OperationFailedException e) {
                    throw new InternalServerErrorException(e);
                } catch (MembershipAlreadyExistsException e) {
                    // ignore
                }
            }
        }

        return resultGroupBeans;
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
