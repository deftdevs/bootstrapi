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
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import com.deftdevs.bootstrapi.commons.exception.UserNotFoundException;
import com.deftdevs.bootstrapi.crowd.model.util.UserModelUtil;
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

        final UserModel userModel = usersService.getUser(user.getDirectoryId(), user.getName());
        assertEquals(user.getName(), userModel.getUsername());
    }

    @Test
    public void testGetUserAnyDirectory() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = usersService.getUser(user.getName());
        assertEquals(user.getName(), userModel.getUsername());
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        final Directory directory = getTestDirectory();
        final String userName = "not_found";
        doThrow(new com.atlassian.crowd.exception.UserNotFoundException(userName)).when(directoryManager).findUserByName(directory.getId(), userName);

        assertThrows(UserNotFoundException.class, () -> {
            usersService.getUser(directory.getId(), userName);
        });
    }

    @Test
    public void testGetUserNotFoundAnyDirectory() throws Exception {
        final Directory directory = getTestDirectory();
        final String userName = "not_found";
        doThrow(new com.atlassian.crowd.exception.UserNotFoundException(userName)).when(directoryManager).findUserByName(directory.getId(), userName);

        assertThrows(UserNotFoundException.class, () -> {
            usersService.getUser(userName);
        });
    }

    @Test
    public void testSetUserAddNew() {
        final User user = getTestUser();
        final UserModel userModel = UserModelUtil.toUserModel(user);
        final UsersServiceImpl spy = spy(usersService);
        doReturn(userModel).when(spy).addUser(anyLong(), any(UserModel.class));

        spy.setUser(user.getDirectoryId(), userModel);
        verify(spy).addUser(anyLong(), any(UserModel.class));
    }

    @Test
    public void testSetUserUpdateExisting() throws CrowdException {
        final User user = getTestUser();
        final UserModel userModel = UserModelUtil.toUserModel(user);
        assertNotNull(userModel);

        final UsersServiceImpl spy = spy(usersService);
        doReturn(user).when(spy).findUser(user.getDirectoryId(), user.getName());
        doReturn(userModel).when(spy).updateUser(user.getDirectoryId(), user.getName(), userModel);

        spy.setUser(user.getDirectoryId(), userModel);
        verify(spy).updateUser(anyLong(), anyString(), any());
    }

    @Test
    public void testSetUsers() {
        final User user = getTestUser();
        final UserModel userModel = UserModelUtil.toUserModel(user);
        userModel.setPassword("s3cr3t");

        final List<UserModel> userModels = new ArrayList<>();
        userModels.add(userModel);
        userModels.add(UserModel.EXAMPLE_1);
        final UsersServiceImpl spy = spy(usersService);
        doAnswer(invocation -> invocation.getArguments()[1]).when(spy).setUser(anyLong(), any());

        spy.setUsers(user.getDirectoryId(), userModels);
        verify(spy, times(userModels.size())).setUser(anyLong(), any());
    }

    @Test
    public void testSetUsersNull() {
        assertEquals(Collections.emptyList(), usersService.setUsers(getTestDirectory().getId(), null));
    }

    @Test
    public void testAddUser() throws CrowdException, DirectoryPermissionException {
        // return the same user as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addUser(anyLong(), any(), any());

        final UserModel userModel = UserModel.EXAMPLE_1;
        userModel.setPassword("s3cr3t");

        final ArgumentCaptor<UserTemplateWithAttributes> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplateWithAttributes.class);
        usersService.addUser(1L, userModel);
        verify(directoryManager).addUser(anyLong(), userTemplateArgumentCaptor.capture(), any());
        assertEquals(userModel.getFirstName(), userTemplateArgumentCaptor.getValue().getFirstName());
        assertEquals(userModel.getLastName(), userTemplateArgumentCaptor.getValue().getLastName());
        assertEquals(userModel.getFullName(), userTemplateArgumentCaptor.getValue().getDisplayName());
        assertEquals(userModel.getEmail(), userTemplateArgumentCaptor.getValue().getEmailAddress());
        assertEquals(userModel.getActive(), userTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testAddUserActiveByDefault() throws CrowdException, DirectoryPermissionException {
        // return the same user as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addUser(anyLong(), any(), any());

        final UserModel userModel = UserModel.EXAMPLE_1;
        userModel.setActive(null);

        final ArgumentCaptor<UserTemplateWithAttributes> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplateWithAttributes.class);
        usersService.addUser(1L, userModel);
        verify(directoryManager).addUser(anyLong(), userTemplateArgumentCaptor.capture(), any());
        assertTrue(userTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testAddUserWithGroups() throws CrowdException, DirectoryPermissionException {
        // return the same user as the one we are adding
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).addUser(anyLong(), any(), any());

        final UserModel userModel = UserModelUtil.toUserModel(getTestUser());
        final List<GroupModel> groupModels = Collections.singletonList(GroupModel.EXAMPLE_1);
        userModel.setPassword("12345");
        userModel.setGroups(groupModels);

        usersService.addUser(1L, userModel);
        verify(groupsService, times(groupModels.size())).setGroup(anyLong(), anyString(), any());
    }

    @Test
    public void testAddUserAlreadyExists() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModelUtil.toUserModel(user);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), userModel);
        });
    }

    @Test
    public void testAddUserNoName() throws CrowdException {
        final User user = getTestUser();
        final UserModel userModel = UserModelUtil.toUserModel(user);
        userModel.setUsername(null);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), userModel);
        });
    }

    @Test
    public void testAddUserTwoDifferentNames() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModelUtil.toUserModel(user);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), "Other", userModel);
        });
    }

    @Test
    public void testAddUserDetailMissing() throws CrowdException {
        final User user = getTestUser();
        final UserModel userModel = UserModelUtil.toUserModel(user);
        userModel.setFirstName(null);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), userModel);
        });
    }

    @Test
    public void testAddUserPasswordMissing() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModelUtil.toUserModel(user);
        userModel.setPassword(null);

        assertThrows(BadRequestException.class, () -> {
            usersService.addUser(user.getDirectoryId(), userModel);
        });
    }

    @Test
    public void testUpdateUser() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());
        // return the same user as the one we are updating
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).updateUser(anyLong(), any());

        final UserModel userModel = UserModel.builder()
            .firstName("Other")
            .lastName("Full Name")
            .fullName("Other Full Name")
            .email("other@example.com")
            .active(false)
            .build();

        final ArgumentCaptor<UserTemplate> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplate.class);
        usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        verify(directoryManager).updateUser(anyLong(), userTemplateArgumentCaptor.capture());
        assertEquals(userModel.getFirstName(), userTemplateArgumentCaptor.getValue().getFirstName());
        assertEquals(userModel.getLastName(), userTemplateArgumentCaptor.getValue().getLastName());
        assertEquals(userModel.getFullName(), userTemplateArgumentCaptor.getValue().getDisplayName());
        assertEquals(userModel.getEmail(), userTemplateArgumentCaptor.getValue().getEmailAddress());
        assertEquals(userModel.getActive(), userTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void updateUserWithGroups() throws CrowdException, DirectoryPermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());
        // return the same user as the one we are updating
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).updateUser(anyLong(), any());

        final UserModel userModel = UserModelUtil.toUserModel(getTestUser());
        final List<GroupModel> groupModels = Collections.singletonList(GroupModel.EXAMPLE_1);
        userModel.setGroups(groupModels);

        usersService.updateUser(1L, user.getName(), userModel);
        verify(groupsService, times(groupModels.size())).setGroup(anyLong(), anyString(), any());
    }

    @Test
    public void testUpdateUserNoOp() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username(user.getName()).build();

        usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        verify(directoryManager, never()).renameUser(anyLong(), anyString(), anyString());
        verify(directoryManager, never()).updateUser(anyLong(), any());
        verify(directoryManager, never()).updateUserCredential(anyLong(), anyString(), any());
    }

    @Test
    public void testUpdateUserNotFound() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final UserModel userModel = UserModelUtil.toUserModel(user);

        assertThrows(UserNotFoundException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        });
    }

    @Test
    public void testUpdateUserAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(Collections.singletonList(getTestDirectory())).when(directoryManager).searchDirectories(any());
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());
        doAnswer(invocation -> invocation.getArguments()[1]).when(directoryManager).updateUser(anyLong(), any());

        final UserModel userModel = UserModel.builder()
            .firstName("Other")
            .lastName("Full Name")
            .fullName("Other Full Name")
            .email("other@example.com")
            .active(false)
            .build();

        final ArgumentCaptor<UserTemplate> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplate.class);
        usersService.updateUser(user.getName(), userModel);
        verify(directoryManager).updateUser(anyLong(), userTemplateArgumentCaptor.capture());
        assertEquals(userModel.getFirstName(), userTemplateArgumentCaptor.getValue().getFirstName());
        assertEquals(userModel.getLastName(), userTemplateArgumentCaptor.getValue().getLastName());
        assertEquals(userModel.getFullName(), userTemplateArgumentCaptor.getValue().getDisplayName());
        assertEquals(userModel.getEmail(), userTemplateArgumentCaptor.getValue().getEmailAddress());
        assertEquals(userModel.getActive(), userTemplateArgumentCaptor.getValue().isActive());
    }

    @Test
    public void testUpdateUserAnyDirectoryWithRename() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(Collections.singletonList(getTestDirectory())).when(directoryManager).searchDirectories(any());
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username("new_username").build();
        doReturn(user).when(directoryManager).renameUser(user.getDirectoryId(), user.getName(), userModel.getUsername());

        usersService.updateUser(user.getName(), userModel);
        // we are just checking that the rename method was called
        verify(directoryManager).renameUser(user.getDirectoryId(), user.getName(), userModel.getUsername());
    }

    @Test
    public void testUpdateUserAnyDirectoryNoOp() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        // Just setting the same username and nothing else should not trigger any update
        final UserModel userModel = UserModel.builder().username(user.getName()).build();

        usersService.updateUser(user.getName(), userModel);
        verify(directoryManager, never()).renameUser(anyLong(), anyString(), anyString());
        verify(directoryManager, never()).updateUser(anyLong(), any());
        verify(directoryManager, never()).updateUserCredential(anyLong(), anyString(), any());
    }

    @Test
    public void testUpdateUserAnyDirectoryWithPassword() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().password("s3cr3t").build();

        final ArgumentCaptor<PasswordCredential> passwordCredentialArgumentCaptor = ArgumentCaptor.forClass(PasswordCredential.class);
        usersService.updateUser(user.getName(), userModel);
        verify(directoryManager).updateUserCredential(anyLong(), anyString(), passwordCredentialArgumentCaptor.capture());
        assertEquals(userModel.getPassword(), passwordCredentialArgumentCaptor.getValue().getCredential());
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

        final UserModel userModel = UserModel.builder().username("new_username").build();

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        });
    }

    @Test
    public void testRenameUserDirectoryPermissionExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username("new_username").build();

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userModel);
        });
    }

    @Test
    public void testRenameUserUserAlreadyExistsException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username("new_username").build();

        doThrow(new UserAlreadyExistsException(user.getDirectoryId(), userModel.getUsername())).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        });
    }

    @Test
    public void testRenameUserUserAlreadyExistsExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username("new_username").build();

        doThrow(new UserAlreadyExistsException(user.getDirectoryId(), userModel.getUsername())).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userModel);
        });
    }

    @Test
    public void testRenameUserInvalidUserException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username("new_username").build();

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        });
    }

    @Test
    public void testRenameUserInvalidUserExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username("new_username").build();

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userModel);
        });
    }

    @Test
    public void testRenameUserOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username("new_username").build();

        doThrow(new OperationFailedException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        });
    }

    @Test
    public void testRenameUserOperationFailedExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().username("new_username").build();

        doThrow(new OperationFailedException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userModel);
        });
    }

    @Test
    public void testUpdateUserDirectoryPermissionException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().fullName("Other Full Name").build();

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        });
    }

    @Test
    public void testUpdateUserNotFoundExceptionAnyDirectory() throws CrowdException {
        final User user = getTestUser();
        final UserModel userModel = UserModel.builder().fullName("Other Full Name").build();

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userModel);
        });
    }

    @Test
    public void testUpdateUserDirectoryPermissionExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().fullName("Other Full Name").build();

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userModel);
        });
    }

    @Test
    public void testUpdateUserInvalidUserException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().fullName("Other Full Name").build();

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        });
    }

    @Test
    public void testUpdateUserInvalidUserExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().fullName("Other Full Name").build();

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userModel);
        });
    }

    @Test
    public void testUpdateUserOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().email("other@example.com").build();

        doThrow(new OperationFailedException()).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getDirectoryId(), user.getName(), userModel);
        });
    }

    @Test
    public void testUpdateUserOperationFailedExceptionAnyDirectory() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserModel userModel = UserModel.builder().email("other@example.com").build();

        doThrow(new OperationFailedException()).when(directoryManager).updateUser(anyLong(), any());

        assertThrows(WebApplicationException.class, () -> {
            usersService.updateUser(user.getName(), userModel);
        });
    }

    @Test
    public void testUpdatePasswordNotFoundException() throws CrowdException, DirectoryPermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doThrow(new com.atlassian.crowd.exception.UserNotFoundException(user.getName())).when(directoryManager).updateUserCredential(user.getDirectoryId(), user.getName(), PasswordCredential.unencrypted(password));

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
        doThrow(new com.atlassian.crowd.exception.UserNotFoundException(user.getName())).when(directoryManager).storeUserAttributes(anyLong(), anyString(), any());

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
