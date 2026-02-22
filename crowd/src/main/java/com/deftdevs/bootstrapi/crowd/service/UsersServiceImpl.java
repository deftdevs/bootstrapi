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
import com.deftdevs.bootstrapi.commons.exception.DirectoryNotFoundException;
import com.deftdevs.bootstrapi.commons.exception.UserNotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.crowd.model.util.UserModelUtil;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.atlassian.crowd.model.user.UserConstants.*;

public class UsersServiceImpl implements UsersService {

    private final CrowdService crowdService;
    private final DirectoryManager directoryManager;
    private final GroupsService groupsService;

    public UsersServiceImpl(
            final CrowdService crowdService,
            final DirectoryManager directoryManager,
            final GroupsService groupsService) {

        this.crowdService = crowdService;
        this.directoryManager = directoryManager;
        this.groupsService = groupsService;
    }

    @Override
    public UserModel getUser(
            final long directoryId,
            final String username) {

        final User user = findUser(directoryId, username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        return UserModelUtil.toUserModel(user);
    }

    public UserModel setUser(
            final long directoryId,
            final UserModel userModel) {

        final User user = findUser(directoryId, userModel.getUsername());

        if (user == null) {
            return addUser(directoryId, userModel);
        }

        return updateUser(directoryId, user.getName(), userModel);
    }

    @Override
    public Map<String, UserModel> setUsers(
            final long directoryId,
            final Map<String, UserModel> userModels) {

        if (userModels == null) {
            return Collections.emptyMap();
        }

        final Map<String, UserModel> resultUserModels = new LinkedHashMap<>();
        for (Map.Entry<String, UserModel> entry : userModels.entrySet()) {
            final UserModel resultUserModel = setUser(directoryId, entry.getValue());
            resultUserModels.put(resultUserModel.getUsername(), resultUserModel);
        }
        return resultUserModels;
    }

    public UserModel addUser(
            final long directoryId,
            final UserModel userModel) {

        return addUser(directoryId, userModel.getUsername(), userModel);
    }

    UserModel addUser(
            final long directoryId,
            final String username,
            final UserModel userModel) {

        final User existingUser = findUser(directoryId, userModel.getUsername());

        if (existingUser != null) {
            throw new BadRequestException(String.format("User '%s' already exists", userModel.getUsername()));
        }

        if (userModel.getUsername() == null) {
            if (username == null) {
                throw new BadRequestException("Cannot create user, username is required");
            }

            userModel.setUsername(username);
        }

        if (username != null && !userModel.getUsername().equals(username)) {
            throw new BadRequestException("Cannot create user, two different usernames provided");
        }

        if (userModel.getFirstName() == null || userModel.getLastName() == null || userModel.getFullName() == null || userModel.getEmail() == null) {
            throw new BadRequestException("Cannot create user, first name, last name, display (full) name and email are required");
        }

        if (userModel.getPassword() == null) {
            throw new BadRequestException("Cannot create user, password is required");
        }

        final UserTemplateWithAttributes userTemplate = new UserTemplateWithAttributes(userModel.getUsername(), directoryId);
        userTemplate.setFirstName(userModel.getFirstName());
        userTemplate.setLastName(userModel.getLastName());
        userTemplate.setDisplayName(userModel.getFullName());
        userTemplate.setEmailAddress(userModel.getEmail());
        userTemplate.setActive(userModel.getActive() == null || userModel.getActive());

        final User user = createUser(directoryId, userTemplate, userModel.getPassword());
        final UserModel resultUserModel = UserModelUtil.toUserModel(user);

        if (userModel.getGroups() != null) {
            final List<GroupModel> resultGroupModels = addUserToGroups(directoryId, userModel.getUsername(), userModel.getGroups());
            resultUserModel.setGroups(resultGroupModels);
        }

        return resultUserModel;
    }

    UserModel updateUser(
            final long directoryId,
            final String username,
            final UserModel userModel) {

        User user = findUser(directoryId, username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        if (userModel.getUsername() != null && !username.equals(userModel.getUsername())) {
            user = renameUser(user, userModel.getUsername());
        }

        if (userModel.getFirstName() != null || userModel.getLastName() != null || userModel.getFullName() != null || userModel.getEmail() != null) {
            user = updateUser(user.getDirectoryId(), getUserTemplate(user, userModel));
        }

        if (userModel.getPassword() != null) {
            updatePassword(user, userModel.getPassword());
        }

        final UserModel resultUserModel = UserModelUtil.toUserModel(user);

        if (userModel.getGroups() != null) {
            final List<GroupModel> resultGroupModels = addUserToGroups(directoryId, userModel.getUsername(), userModel.getGroups());
            resultUserModel.setGroups(resultGroupModels);
        }

        return resultUserModel;
    }

    @Nullable
    User findUser(
            final long directoryId,
            final String username) {

        try {
            return directoryManager.findUserByName(directoryId, username);
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException e) {
            throw new DirectoryNotFoundException(directoryId);
        } catch (com.atlassian.crowd.exception.UserNotFoundException e) {
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
    public UserModel getUser(
            final String username) {

        final User user = findUserAllDirectories(username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        return UserModelUtil.toUserModel(user);
    }

    /*
     * All the deprecated methods...
     */

    /**
     * @deprecated (due to random directory selection)
     */
    @Override
    @Deprecated
    public UserModel updateUser(
            final String username,
            final UserModel userModel) {

        final User user = findUserAllDirectories(username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        return updateUser(user.getDirectoryId(), username, userModel);
    }

    /**
     * @deprecated (due to random directory selection)
     */
    @Override
    @Deprecated
    public UserModel updatePassword(
            final String username,
            final String password) {

        final User user = findUserAllDirectories(username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        updatePassword(user, password);
        return UserModelUtil.toUserModel(user);
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
            } catch (com.atlassian.crowd.exception.UserNotFoundException e) {
                // Ignore, will be handled below
            } catch (com.atlassian.crowd.exception.DirectoryNotFoundException | OperationFailedException e) {
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
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException |
                 com.atlassian.crowd.exception.UserNotFoundException | OperationFailedException e) {
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
        } catch (OperationFailedException | com.atlassian.crowd.exception.DirectoryNotFoundException e) {
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
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException |
                 com.atlassian.crowd.exception.UserNotFoundException | OperationFailedException e) {
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
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException |
                 com.atlassian.crowd.exception.UserNotFoundException | OperationFailedException e) {
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
        } catch (com.atlassian.crowd.exception.DirectoryNotFoundException |
                 com.atlassian.crowd.exception.UserNotFoundException | OperationFailedException e) {
            // At this point, we know the user exists, thus directory or user not found
            // should never happen, so if it does, treat it as an internal server error
            throw new InternalServerErrorException(e);
        }
    }

    List<GroupModel> addUserToGroups(
            final long directoryId,
            final String username,
            final List<GroupModel> groupModels) {

        final List<GroupModel> resultGroupModels = new ArrayList<>();

        if (groupModels != null) {
            for (GroupModel groupModel : groupModels) {
                final GroupModel resultGroupModel = groupsService.setGroup(directoryId, groupModel.getName(), groupModel);

                try {
                    directoryManager.addUserToGroup(directoryId, username, groupModel.getName());
                    resultGroupModels.add(resultGroupModel);
                } catch (DirectoryPermissionException | ReadOnlyGroupException e) {
                    throw new BadRequestException(e);
                } catch (com.atlassian.crowd.exception.DirectoryNotFoundException |
                         com.atlassian.crowd.exception.UserNotFoundException | GroupNotFoundException |
                         OperationFailedException e) {
                    throw new InternalServerErrorException(e);
                } catch (MembershipAlreadyExistsException e) {
                    // ignore
                }
            }
        }

        return resultGroupModels;
    }

    private static UserTemplate getUserTemplate(
            final User user,
            final UserModel userModel) {

        final UserTemplate userTemplate = new UserTemplate(user);

        if (userModel.getFirstName() != null) {
            userTemplate.setFirstName(userModel.getFirstName());
        }

        if (userModel.getLastName() != null) {
            userTemplate.setLastName(userModel.getLastName());
        }

        if (userModel.getFullName() != null) {
            userTemplate.setDisplayName(userModel.getFullName());
        }

        if (userModel.getEmail() != null) {
            userTemplate.setEmailAddress(userModel.getEmail());
        }

        if (userModel.getActive() != null) {
            userTemplate.setActive(userModel.getActive());
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
