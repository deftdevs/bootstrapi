package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.confluence.user.UserAccessor;
import com.atlassian.user.EntityException;
import com.atlassian.user.User;
import com.atlassian.user.UserManager;
import com.atlassian.user.impl.DefaultUser;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.deftdevs.bootstrapi.confluence.model.util.UserModelUtil.toUser;
import static com.deftdevs.bootstrapi.confluence.model.util.UserModelUtil.toUserModel;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserManager userManager;

    @Mock
    private UserAccessor userAccessor;

    private UsersServiceImpl userService;

    @BeforeEach
    public void setup() {
        userService = new UsersServiceImpl(userManager, userAccessor);
    }

    @Test
    void testGetUser() throws EntityException, NotFoundException {
        User user = toUser(UserModel.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        UserModel userModel = toUserModel(user);
        UserModel gotUserModel = userService.getUser(user.getName());

        assertUserModelEquals(userModel, gotUserModel);
        assertNull(userModel.getPassword());
    }

    @Test
    void testGetUserIsNotConfluenceUser() throws NotFoundException, EntityException {
        final User user = new DefaultUser(toUser(UserModel.EXAMPLE_1));
        doReturn(user).when(userManager).getUser(user.getName());

        final String userName = user.getName();

        assertThrows(NotFoundException.class, () -> {
            userService.getUser(userName);
        });
    }

    @Test
    void testUpdateUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = toUser(UserModel.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        final UserModel updateUserModel = UserModel.EXAMPLE_2;
        final UserModel updatedUserModel = userService.updateUser(user.getName(), updateUserModel);

        assertUserModelEquals(updateUserModel, updatedUserModel);
    }

    @Test
    void testUpdateUsername() throws EntityException {
        final UserModel requestUserModel = new UserModel();
        requestUserModel.setUsername("ChangeUsername");

        final User existingUser = toUser(UserModel.EXAMPLE_1);
        final ConfluenceUserImpl userUpdatedUsername = new ConfluenceUserImpl(existingUser);
        userUpdatedUsername.setName(requestUserModel.getUsername());

        doReturn(existingUser).when(userManager).getUser(existingUser.getName());
        doReturn(userUpdatedUsername).when(userAccessor).renameUser((ConfluenceUser)existingUser, requestUserModel.getUsername());

        final UserModel updatedUserModel = userService.updateUser(existingUser.getName(), requestUserModel);

        assertEquals(requestUserModel.getUsername(), updatedUserModel.getUsername());
    }

    @Test
    void testUpdateUsernameException() throws EntityException {
        final UserModel requestUserModel = new UserModel();
        requestUserModel.setUsername("ChangeUsername");

        final User existingUser = toUser(UserModel.EXAMPLE_1);
        doReturn(existingUser).when(userManager).getUser(existingUser.getName());
        doThrow(new EntityException()).when(userAccessor).renameUser((ConfluenceUser)existingUser, requestUserModel.getUsername());

        final String existingUserName = existingUser.getName();

        assertThrows(BadRequestException.class, () -> {
            userService.updateUser(existingUserName, requestUserModel);
        });
    }

    @Test
    void testUpdateUserEmptyModel() throws EntityException, NotFoundException, BadRequestException {
        final User user = toUser(UserModel.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        final UserModel updateUserModel = new UserModel();
        final UserModel notUpdatedUserModel = userService.updateUser(user.getName(), updateUserModel);

        assertUserModelEquals(UserModel.EXAMPLE_1, notUpdatedUserModel);
    }

    @Test
    void testUpdateUserNotConfluenceUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = new DefaultUser(toUser(UserModel.EXAMPLE_1));
        doReturn(user).when(userManager).getUser(user.getName());

        final String userName = user.getName();

        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(userName, UserModel.EXAMPLE_2);
        });
    }

    @Test
    void testUpdateUserPassword() throws EntityException, NotFoundException, BadRequestException {
        final UserModel userModel = UserModel.EXAMPLE_1;
        doReturn(toUser(userModel)).when(userManager).getUser(userModel.getUsername());

        final UserModel updateUserModel = new UserModel();
        updateUserModel.setPassword("new password");

        final UserModel updatedUserModel = userService.updateUser(userModel.getUsername(), userModel);
        assertUserModelEquals(userModel, updatedUserModel);
        // user password is not returned here, getting user bean shows update was successful
        assertNull(updatedUserModel.getPassword());
    }

    @Test
    void testUpdateUserPasswordNotConfluenceUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = new DefaultUser(toUser(UserModel.EXAMPLE_1));
        doReturn(user).when(userManager).getUser(user.getName());

        final String userName = user.getName();

        assertThrows(NotFoundException.class, () -> {
            userService.updatePassword(userName, "newPW");
        });
    }

    /*
     * Not yet implemented methods
     */

    @Test
    void testGetUserByDirectoryIdNotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userService.getUser(0, null);
        });
    }

    @Test
    void testSetUserByDirectoryIdNotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userService.setUser(0, null);
        });
    }

    @Test
    void testSetUsersByDirectoryIdNotImplemented() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userService.setUsers(0, null);
        });
    }

    /*
     * A user in confluence only holds the attributes `username`, `fullName` and `email`,
     * so create this method to easily compare users without custom comparators.
     */
    public static void assertUserModelEquals(
            final UserModel expected,
            final UserModel actual) {

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

}
