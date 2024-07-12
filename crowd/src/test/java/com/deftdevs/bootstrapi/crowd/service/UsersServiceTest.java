package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.MockDirectoryInternal;
import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.directory.DirectoryPermissionException;
import com.atlassian.crowd.model.user.ImmutableUser;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.model.user.UserTemplate;
import com.atlassian.crowd.model.user.UserTemplateWithAttributes;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.GroupBean;
import com.deftdevs.bootstrapi.commons.model.UserBean;
import com.deftdevs.bootstrapi.crowd.exception.NotFoundExceptionForUser;
import com.deftdevs.bootstrapi.crowd.model.util.UserBeanUtil;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.WebApplicationException;
import java.util.*;

import static com.atlassian.crowd.model.user.UserConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    @Mock
    private CrowdService crowdService;

    @Mock
    private DirectoryManager directoryManager;

    @Mock
    private GroupsService groupsService;

    private UsersServiceImpl usersService;

    @BeforeEach
    public void setup() {
        usersService = new UsersServiceImpl(crowdService, directoryManager, groupsService);

        setupDirectoryManager();
    }

    private void setupDirectoryManager() {
        lenient().doReturn(Collections.singletonList(getTestDirectory())).when(directoryManager).searchDirectories(any());
    }

    @Test
    public void testGetUser() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = usersService.getUser(user.getDirectoryId(), user.getName());
        assertEquals(user.getName(), userBean.getUsername());
    }

    @Test
    public void testGetUserAnyDirectory() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = usersService.getUser(user.getName());
        assertEquals(user.getName(), userBean.getUsername());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        final Directory directory = getTestDirectory();
        final String userName = "not_found";
        doThrow(new UserNotFoundException(userName)).when(directoryManager).findUserByName(directory.getId(), userName);

        assertThrows(NotFoundExceptionForUser.class, () -> {
            usersService.getUser(directory.getId(), userName);
        });
    }

    @Test
    public void testGetUserNotFoundAnyDirectory() throws Exception {
        final Directory directory = getTestDirectory();
        final String userName = "not_found";
        doThrow(new UserNotFoundException(userName)).when(directoryManager).findUserByName(directory.getId(), userName);

        assertThrows(NotFoundExceptionForUser.class, () -> {
            usersService.getUser(userName);
        });
    }

    @Test
    public void testSetUserAddNew() {
        final User user = getTestUser();
        final UserBean userBean = UserBeanUtil.toUserBean(user);
        final UsersServiceImpl spy = spy(usersService);
        doReturn(userBean).when(spy).addUser(anyLong(), any(UserBean.class));

        spy.setUser(user.getDirectoryId(), userBean);
        verify(spy).addUser(anyLong(), any(UserBean.class));
    }

    @Test
    public void testSetUserUpdateExisting() throws CrowdException {
        final User user = getTestUser();
        final UserBean userBean = UserBeanUtil.toUserBean(user);
        assertNotNull(userBean);

        final UsersServiceImpl spy = spy(usersService);
        doReturn(user).when(spy).findUser(user.getDirectoryId(), user.getName());
        doReturn(userBean).when(spy).updateUser(user.getDirectoryId(), user.getName(), userBean);

        spy.setUser(user.getDirectoryId(), userBean);
        verify(spy).updateUser(anyLong(), anyString(), any());
    }

    @Test
    public void testSetUsers() {
        final User user = getTestUser();
        final UserBean userBean = UserBeanUtil.toUserBean(user);
        userBean.setPassword("s3cr3t");

        final List<UserBean> userBeans = new ArrayList<>();
        userBeans.add(userBean);
        userBeans.add(UserBean.EXAMPLE_1);
        final UsersServiceImpl spy = spy(usersService);
        doAnswer(invocation -> invocation.getArguments()[1]).when(spy).setUser(anyLong(), any());

        spy.setUsers(user.getDirectoryId(), userBeans);
        verify(spy, times(userBeans.size())).setUser(anyLong(), any());
    }

    @Test
    public void testSetUsersNull() {
        assertEquals(Collections.emptyList(), usersService.setUsers(getTestDirectory().getId(), null));
    }

    @Test
    public void testAddUser() throws CrowdException, DirectoryPermissionException {
        // return the same user as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addUser(anyLong(), any(), any());

        final UserBean userBean = UserBean.EXAMPLE_1;
        userBean.setPassword("s3cr3t");

        final ArgumentCaptor<UserTemplateWithAttributes> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplateWithAttributes.class);
        usersService.addUser(1L, userBean);
        verify(directoryManager).addUser(anyLong(), userTemplateArgumentCaptor.capture(), any());
        assertEquals(userBean.getFirstName(), userTemplateArgumentCaptor.getValue().getFirstName());
        assertEquals(userBean.getLastName(), userTemplateArgumentCaptor.getValue().getLastName());
        assertEquals(userBean.getFullName(), userTemplateArgumentCaptor.getValue().getDisplayName());
        assertEquals(userBean.getEmail(), userTemplateArgumentCaptor.getValue().getEmailAddress());
        assertEquals(userBean.getActive(), userTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testAddUserActiveByDefault() throws CrowdException, DirectoryPermissionException {
        // return the same user as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addUser(anyLong(), any(), any());

        final UserBean userBean = UserBean.EXAMPLE_1;
        userBean.setActive(null);

        final ArgumentCaptor<UserTemplateWithAttributes> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplateWithAttributes.class);
        usersService.addUser(1L, userBean);
        verify(directoryManager).addUser(anyLong(), userTemplateArgumentCaptor.capture(), any());
        assertTrue(userTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testAddUserWithGroups() throws CrowdException, DirectoryPermissionException {
        // return the same user as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addUser(anyLong(), any(), any());

        final UserBean userBean = UserBeanUtil.toUserBean(getTestUser());
        final Collection<GroupBean> groupBeans = Collections.singletonList(GroupBean.EXAMPLE_1);
        userBean.setPassword("12345");
        userBean.setGroups(groupBeans);

        usersService.addUser(1L, userBean);
        verify(groupsService, times(groupBeans.size())).setGroup(anyLong(), anyString(), any());
    }

    @Test
    public void testAddUserAlreadyExists() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = UserBeanUtil.toUserBean(user);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), userBean);
        });
    }

    @Test
    public void testAddUserNoName() throws CrowdException {
        final User user = getTestUser();
        final UserBean userBean = UserBeanUtil.toUserBean(user);
        userBean.setUsername(null);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), userBean);
        });
    }

    @Test
    public void testAddUserTwoDifferentNames() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = UserBeanUtil.toUserBean(user);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), "Other", userBean);
        });
    }

    @Test
    public void testAddUserDetailMissing() throws CrowdException {
        final User user = getTestUser();
        final UserBean userBean = UserBeanUtil.toUserBean(user);
        userBean.setFirstName(null);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), userBean);
        });
    }

    @Test
    public void testAddUserPasswordMissing() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = UserBeanUtil.toUserBean(user);
        userBean.setPassword(null);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), userBean);
        });
    }

    @Test
    public void testUpdateUser() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());
        // return the same user as the one we are updating
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).updateUser(anyLong(), any());

        final UserBean userBean = new UserBean();
        userBean.setFirstName("Other");
        userBean.setLastName("Full Name");
        userBean.setFullName("Other Full Name");
        userBean.setEmail("other@example.com");
        userBean.setActive(false);

        final ArgumentCaptor<UserTemplate> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplate.class);
        usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        verify(directoryManager).updateUser(anyLong(), userTemplateArgumentCaptor.capture());
        assertEquals(userBean.getFirstName(), userTemplateArgumentCaptor.getValue().getFirstName());
        assertEquals(userBean.getLastName(), userTemplateArgumentCaptor.getValue().getLastName());
        assertEquals(userBean.getFullName(), userTemplateArgumentCaptor.getValue().getDisplayName());
        assertEquals(userBean.getEmail(), userTemplateArgumentCaptor.getValue().getEmailAddress());
        assertEquals(userBean.getActive(), userTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void updateUserWithGroups() throws CrowdException, DirectoryPermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());
        // return the same user as the one we are updating
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).updateUser(anyLong(), any());

        final UserBean userBean = UserBeanUtil.toUserBean(getTestUser());
        final Collection<GroupBean> groupBeans = Collections.singletonList(GroupBean.EXAMPLE_1);
        userBean.setGroups(groupBeans);

        usersService.updateUser(1L, user.getName(), userBean);
        verify(groupsService, times(groupBeans.size())).setGroup(anyLong(), anyString(), any());
    }

    @Test
    public void testUpdateUserNoOp() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername(user.getName());

        usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        verify(directoryManager, never()).renameUser(anyLong(), anyString(), anyString());
        verify(directoryManager, never()).updateUser(anyLong(), any());
        verify(directoryManager, never()).updateUserCredential(anyLong(), anyString(), any());
    }

    @Test
    public void testUpdateUserNotFound() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final UserBean userBean = UserBeanUtil.toUserBean(user);

        assertThrows(NotFoundExceptionForUser.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        });
    }

    @Test
    public void testUpdateUserAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(Collections.singletonList(getTestDirectory())).when(directoryManager).searchDirectories(any());
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).updateUser(anyLong(), any());

        final UserBean userBean = new UserBean();
        userBean.setFirstName("Other");
        userBean.setLastName("Full Name");
        userBean.setFullName("Other Full Name");
        userBean.setEmail("other@example.com");
        userBean.setActive(false);

        final ArgumentCaptor<UserTemplate> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplate.class);
        usersService.updateUser(user.getName(), userBean);
        verify(directoryManager).updateUser(anyLong(), userTemplateArgumentCaptor.capture());
        assertEquals(userBean.getFirstName(), userTemplateArgumentCaptor.getValue().getFirstName());
        assertEquals(userBean.getLastName(), userTemplateArgumentCaptor.getValue().getLastName());
        assertEquals(userBean.getFullName(), userTemplateArgumentCaptor.getValue().getDisplayName());
        assertEquals(userBean.getEmail(), userTemplateArgumentCaptor.getValue().getEmailAddress());
        assertEquals(userBean.getActive(), userTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testUpdateUserAnyDirectoryWithRename() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(Collections.singletonList(getTestDirectory())).when(directoryManager).searchDirectories(any());
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");
        doReturn(user).when(directoryManager).renameUser(user.getDirectoryId(), user.getName(), userBean.getUsername());

        usersService.updateUser(user.getName(), userBean);
        // we are just checking that the rename method was called
        verify(directoryManager).renameUser(user.getDirectoryId(), user.getName(), userBean.getUsername());
    }

    @Test
    public void testUpdateUserAnyDirectoryNoOp() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        // Just setting the same username and nothing else should not trigger any update
        final UserBean userBean = new UserBean();
        userBean.setUsername(user.getName());

        usersService.updateUser(user.getName(), userBean);
        verify(directoryManager, never()).renameUser(anyLong(), anyString(), anyString());
        verify(directoryManager, never()).updateUser(anyLong(), any());
        verify(directoryManager, never()).updateUserCredential(anyLong(), anyString(), any());
    }

    @Test
    public void testUpdateUserAnyDirectoryWithPassword() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setPassword("s3cr3t");

        final ArgumentCaptor<PasswordCredential> passwordCredentialArgumentCaptor = ArgumentCaptor.forClass(PasswordCredential.class);
        usersService.updateUser(user.getName(), userBean);
        verify(directoryManager).updateUserCredential(anyLong(), anyString(), passwordCredentialArgumentCaptor.capture());
        assertEquals(userBean.getPassword(), passwordCredentialArgumentCaptor.getValue().getCredential());
    }

    @Test
    public void testUpdatePassword() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final String password = "pa55w0rd";

        final UsersServiceImpl spyUsersService = spy(usersService);
        final ArgumentCaptor<PasswordCredential> passwordCredentialArgumentCaptor = ArgumentCaptor.forClass(PasswordCredential.class);
        spyUsersService.updatePassword(user.getName(), password);
        verify(spyUsersService).resetUserPasswordAttributes(any());
        verify(directoryManager).updateUserCredential(anyLong(), anyString(), passwordCredentialArgumentCaptor.capture());
        assertEquals(password, passwordCredentialArgumentCaptor.getValue().getCredential());
    }

    @Test
    public void testUpdatePasswordWithSamePassword() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final String password = "pa55w0rd";
        final com.atlassian.crowd.embedded.api.User authenticatedUserMock = mock(com.atlassian.crowd.embedded.api.User.class);
        doReturn(authenticatedUserMock).when(crowdService).authenticate(user.getName(), password);

        usersService.updatePassword(user.getName(), password);
        verify(directoryManager, never()).updateUserCredential(anyLong(), anyString(), any());
    }

    @Captor
    private ArgumentCaptor<Map<String, Set<String>>> userAttributeArgumentCaptor;

    @Test
    public void testResetUserPasswordAttributes() throws CrowdException, PermissionException {
        final User user = getTestUser();

        usersService.resetUserPasswordAttributes(user);
        verify(directoryManager).storeUserAttributes(anyLong(), anyString(), userAttributeArgumentCaptor.capture());

        assertNotNull(userAttributeArgumentCaptor.getValue().get(INVALID_PASSWORD_ATTEMPTS));
        assertNotNull(userAttributeArgumentCaptor.getValue().get(REQUIRES_PASSWORD_CHANGE));
        assertNotNull(userAttributeArgumentCaptor.getValue().get(PASSWORD_LASTCHANGED));
    }

    // We kind of need to test all the exceptions here, but it's also pointless to test
    // all the exact mappings, because that's like repeating the implementation.
    // For this reason, we are just using WebApplicationException as a catch-all for now.
    // We are also not testing DirectoryNotFoundException and UserNotFoundException here,
    // because these cases won't happen anymore after a user has been found by its name.

    @Test
    public void testGetUserDirectoryNotFoundException() throws CrowdException {
        final User user = getTestUser();
        doThrow(new DirectoryNotFoundException(user.getDirectoryId())).when(directoryManager).findUserByName(anyLong(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.getUser(user.getDirectoryId(), user.getName());
        });
    }

    @Test
    public void testGetUserDirectoryNotFoundExceptionAnyDirectory() throws CrowdException {
        final User user = getTestUser();
        doThrow(new DirectoryNotFoundException(user.getDirectoryId())).when(directoryManager).findUserByName(anyLong(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.getUser(user.getName());
        });
    }

    @Test
    public void testGetUserOperationFailedException() throws CrowdException {
        final User user = getTestUser();
        doThrow(new OperationFailedException()).when(directoryManager).findUserByName(anyLong(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.getUser(user.getDirectoryId(), user.getName());
        });
    }

    @Test
    public void testGetUserOperationFailedExceptionAnyDirectory() throws CrowdException {
        final User user = getTestUser();
        doThrow(new OperationFailedException()).when(directoryManager).findUserByName(anyLong(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.getUser(user.getName());
        });
    }

    @Test
    public void testRenameUserDirectoryPermissionException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        });
    }

    @Test
    public void testRenameUserDirectoryPermissionExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userBean);
        });
    }

    @Test
    public void testRenameUserUserAlreadyExistsException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new UserAlreadyExistsException(user.getDirectoryId(), userBean.getUsername())).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        });
    }

    @Test
    public void testRenameUserUserAlreadyExistsExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new UserAlreadyExistsException(user.getDirectoryId(), userBean.getUsername())).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userBean);
        });
    }

    @Test
    public void testRenameUserInvalidUserException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        });
    }

    @Test
    public void testRenameUserInvalidUserExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userBean);
        });
    }

    @Test
    public void testRenameUserOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new OperationFailedException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        });
    }

    @Test
    public void testRenameUserOperationFailedExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new OperationFailedException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userBean);
        });
    }

    @Test
    public void testUpdateUserDirectoryPermissionException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setFullName("Other Full Name");

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        });
    }

    @Test
    public void testUpdateUserNotFoundExceptionAnyDirectory() throws CrowdException {
        final User user = getTestUser();
        final UserBean userBean = new UserBean();
        userBean.setFullName("Other Full Name");

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userBean);
        });
    }

    @Test
    public void testUpdateUserDirectoryPermissionExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setFullName("Other Full Name");

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userBean);
        });
    }

    @Test
    public void testUpdateUserInvalidUserException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setFullName("Other Full Name");

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        });
    }

    @Test
    public void testUpdateUserInvalidUserExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setFullName("Other Full Name");

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userBean);
        });
    }

    @Test
    public void testUpdateUserOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setEmail("other@example.com");

        doThrow(new OperationFailedException()).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userBean);
        });
    }

    @Test
    public void testUpdateUserOperationFailedExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setEmail("other@example.com");

        doThrow(new OperationFailedException()).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userBean);
        });
    }

    @Test
    public void testUpdatePasswordNotFoundException() throws CrowdException, DirectoryPermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doThrow(new UserNotFoundException(user.getName())).when(directoryManager).updateUserCredential(user.getDirectoryId(), user.getName(), PasswordCredential.unencrypted(password));

        assertThrows(WebApplicationException.class, () -> {
            usersService.updatePassword(user, password);
        });
    }

    @Test
    public void testUpdatePasswordDirectoryPermissionException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doThrow(new DirectoryPermissionException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updatePassword(user, password);
        });
    }

    @Test
    public void testUpdatePasswordDirectoryPermissionExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updatePassword(user.getName(), password);
        });
    }

    @Test
    public void testUpdatePasswordInvalidCredentialException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doThrow(new InvalidCredentialException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updatePassword(user, password);
        });
    }

    @Test
    public void testUpdatePasswordInvalidCredentialExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        doThrow(new InvalidCredentialException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updatePassword(user.getName(), password);
        });
    }

    @Test
    public void testUpdatePasswordOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doThrow(new OperationFailedException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updatePassword(user, password);
        });
    }

    @Test
    public void testUpdatePasswordOperationFailedExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        doThrow(new OperationFailedException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updatePassword(user.getName(), password);
        });
    }

    @Test
    public void testResetUserPasswordAttributesDirectoryPermissionException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doThrow(new DirectoryPermissionException()).when(directoryManager).storeUserAttributes(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.resetUserPasswordAttributes(user);
        });
    }

    @Test
    public void testResetUserPasswordAttributesDirectoryNotFoundException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doThrow(new DirectoryNotFoundException(user.getDirectoryId())).when(directoryManager).storeUserAttributes(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.resetUserPasswordAttributes(user);
        });
    }

    @Test
    public void testResetUserPasswordAttributesUserNotFoundException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doThrow(new UserNotFoundException(user.getName())).when(directoryManager).storeUserAttributes(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.resetUserPasswordAttributes(user);
        });
    }

    @Test
    public void testResetUserPasswordAttributesOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doThrow(new OperationFailedException()).when(directoryManager).storeUserAttributes(anyLong(), anyString(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.resetUserPasswordAttributes(user);
        });
    }

    private Directory getTestDirectory() {
        return new MockDirectoryInternal();
    }

    private User getTestUser() {
        return ImmutableUser.builder(getTestDirectory().getId(), "test")
                .firstName("Test")
                .lastName("User")
                .displayName("Test User")
                .emailAddress("test@example.com")
                .active(true)
                .build();
    }
}
