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
import com.atlassian.crowd.search.EntityDescriptor;
import com.atlassian.crowd.search.builder.QueryBuilder;
import com.atlassian.crowd.search.query.entity.EntityQuery;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.commons.model.UserBean;
import de.aservo.confapi.commons.service.api.UsersService;
import de.aservo.confapi.crowd.model.util.UserBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

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
            final String name) {

        return UserBeanUtil.toUserBean(findUser(name));
    }

    @Override
    public UserBean updateUser(
            final String name,
            final UserBean userBean) {

        User user = findUser(name);

        if (userBean.getUsername() != null && !name.equals(userBean.getUsername())) {
            user = renameUser(user, userBean.getUsername());
        }

        if (userBean.getFullName() != null || userBean.getEmail() != null) {
            final UserTemplate userTemplate = new UserTemplate(user);

            if (userBean.getFullName() != null) {
                userTemplate.setDisplayName(userBean.getFullName());
            }

            if (userBean.getEmail() != null) {
                userTemplate.setEmailAddress(userBean.getEmail());
            }

            user = updateUser(user.getDirectoryId(), userTemplate);
        }

        if (userBean.getPassword() != null) {
            updatePassword(user, userBean.getPassword());
        }

        return UserBeanUtil.toUserBean(user);
    }

    @Override
    public UserBean updatePassword(
            final String name,
            final String password) {

        final User user = findUser(name);
        updatePassword(user, password);
        return UserBeanUtil.toUserBean(user);
    }

    @NotNull
    private User findUser(
            final String name) {

        for (Directory directory : findDirectories()) {
            try {
                return directoryManager.findUserByName(directory.getId(), name);
            } catch (UserNotFoundException e) {
                // Ignore, will be handled below
            } catch (DirectoryNotFoundException | OperationFailedException e) {
                throw new InternalServerErrorException(e);
            }
        }

        throw new NotFoundException(String.format("User with name '%s' could not be found", name));
    }

    @NotNull
    private User renameUser(
            final User user,
            final String newName) {

        try {
            return directoryManager.renameUser(user.getDirectoryId(), user.getName(), newName);
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

    private void updatePassword(
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

    private List<Directory> findDirectories() {
        final EntityQuery<Directory> directoryEntityQuery = QueryBuilder.queryFor(Directory.class, EntityDescriptor.directory())
                .returningAtMost(EntityQuery.ALL_RESULTS);

        return directoryManager.searchDirectories(directoryEntityQuery);
    }
}
