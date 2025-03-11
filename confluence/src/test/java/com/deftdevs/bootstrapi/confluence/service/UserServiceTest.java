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
import com.deftdevs.bootstrapi.commons.model.UserBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.deftdevs.bootstrapi.confluence.model.util.UserBeanUtil.toUser;
import static com.deftdevs.bootstrapi.confluence.model.util.UserBeanUtil.toUserBean;
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
        User user = toUser(UserBean.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        UserBean userBean = toUserBean(user);
        UserBean gotUserBean = userService.getUser(user.getName());

        assertUserBeanEquals(userBean, gotUserBean);
        assertNull(userBean.getPassword());
    }

    @Test
    void testGetUserIsNotConfluenceUser() throws NotFoundException, EntityException {
        final User user = new DefaultUser(toUser(UserBean.EXAMPLE_1));
        doReturn(user).when(userManager).getUser(user.getName());

        final String userName = user.getName();

        assertThrows(NotFoundException.class, () -> {
            userService.getUser(userName);
        });
    }

    @Test
    void testUpdateUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = toUser(UserBean.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        final UserBean updateUserBean = UserBean.EXAMPLE_2;
        final UserBean updatedUserBean = userService.updateUser(user.getName(), updateUserBean);

        assertUserBeanEquals(updateUserBean, updatedUserBean);
    }

    @Test
    void testUpdateUsername() throws EntityException {
        final UserBean requestUserBean = new UserBean();
        requestUserBean.setUsername("ChangeUsername");

        final User existingUser = toUser(UserBean.EXAMPLE_1);
        final ConfluenceUserImpl userUpdatedUsername = new ConfluenceUserImpl(existingUser);
        userUpdatedUsername.setName(requestUserBean.getUsername());

        doReturn(existingUser).when(userManager).getUser(existingUser.getName());
        doReturn(userUpdatedUsername).when(userAccessor).renameUser((ConfluenceUser)existingUser, requestUserBean.getUsername());

        final UserBean updatedUserBean = userService.updateUser(existingUser.getName(), requestUserBean);

        assertEquals(requestUserBean.getUsername(), updatedUserBean.getUsername());
    }

    @Test
    void testUpdateUsernameException() throws EntityException {
        final UserBean requestUserBean = new UserBean();
        requestUserBean.setUsername("ChangeUsername");

        final User existingUser = toUser(UserBean.EXAMPLE_1);
        doReturn(existingUser).when(userManager).getUser(existingUser.getName());
        doThrow(new EntityException()).when(userAccessor).renameUser((ConfluenceUser)existingUser, requestUserBean.getUsername());

        final String existingUserName = existingUser.getName();

        assertThrows(BadRequestException.class, () -> {
            userService.updateUser(existingUserName, requestUserBean);
        });
    }

    @Test
    void testUpdateUserEmptyBean() throws EntityException, NotFoundException, BadRequestException {
        final User user = toUser(UserBean.EXAMPLE_1);
        doReturn(user).when(userManager).getUser(user.getName());

        final UserBean updateUserBean = new UserBean();
        final UserBean notUpdatedUserBean = userService.updateUser(user.getName(), updateUserBean);

        assertUserBeanEquals(UserBean.EXAMPLE_1, notUpdatedUserBean);
    }

    @Test
    void testUpdateUserNotConfluenceUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = new DefaultUser(toUser(UserBean.EXAMPLE_1));
        doReturn(user).when(userManager).getUser(user.getName());

        final String userName = user.getName();

        assertThrows(NotFoundException.class, () -> {
            userService.updateUser(userName, UserBean.EXAMPLE_2);
        });
    }

    @Test
    void testUpdateUserPassword() throws EntityException, NotFoundException, BadRequestException {
        final UserBean userBean = UserBean.EXAMPLE_1;
        doReturn(toUser(userBean)).when(userManager).getUser(userBean.getUsername());

        final UserBean updateUserBean = new UserBean();
        updateUserBean.setPassword("new password");

        final UserBean updatedUserBean = userService.updateUser(userBean.getUsername(), userBean);
        assertUserBeanEquals(userBean, updatedUserBean);
        // user password is not returned here, getting user bean shows update was successful
        assertNull(updatedUserBean.getPassword());
    }

    @Test
    void testUpdateUserPasswordNotConfluenceUser() throws EntityException, NotFoundException, BadRequestException {
        final User user = new DefaultUser(toUser(UserBean.EXAMPLE_1));
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
    public static void assertUserBeanEquals(
            final UserBean expected,
            final UserBean actual) {

        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFullName(), actual.getFullName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

}
