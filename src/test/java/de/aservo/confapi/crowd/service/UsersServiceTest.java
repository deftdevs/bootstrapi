package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.MockDirectoryInternal;
import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.exception.CrowdException;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.exception.InvalidCredentialException;
import com.atlassian.crowd.exception.InvalidUserException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.PermissionException;
import com.atlassian.crowd.exception.UserAlreadyExistsException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.directory.DirectoryPermissionException;
import com.atlassian.crowd.model.user.ImmutableUser;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.model.user.UserTemplate;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.commons.model.UserBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTest {

    @Mock
    private DirectoryManager directoryManager;

    private UsersServiceImpl usersService;

    @Before
    public void setup() {
        usersService = new UsersServiceImpl(directoryManager);

        setupDirectoryManager();
    }

    private void setupDirectoryManager() {
        doReturn(Collections.singletonList(getTestDirectory())).when(directoryManager).searchDirectories(any());
    }

    @Test
    public void testGetUser() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = usersService.getUser(user.getName());
        assertEquals(user.getName(), userBean.getUsername());
    }

    @Test(expected = NotFoundException.class)
    public void testGetUserNotFound() throws Exception {
        final Directory directory = getTestDirectory();
        final String userName = "not_found";
        doThrow(new UserNotFoundException(userName)).when(directoryManager).findUserByName(directory.getId(), userName);

        usersService.getUser(userName);
    }

    @Test
    public void testUpdateUser() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setFullName("Other Full Name");
        userBean.setEmail("other@example.com");

        final ArgumentCaptor<UserTemplate> userTemplateArgumentCaptor = ArgumentCaptor.forClass(UserTemplate.class);
        usersService.updateUser(user.getName(), userBean);
        verify(directoryManager).updateUser(anyLong(), userTemplateArgumentCaptor.capture());
        assertEquals(userBean.getFullName(), userTemplateArgumentCaptor.getValue().getDisplayName());
        assertEquals(userBean.getEmail(), userTemplateArgumentCaptor.getValue().getEmailAddress());
    }

    @Test
    public void testUpdateUserWithRename() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        usersService.updateUser(user.getName(), userBean);
        verify(directoryManager).renameUser(user.getDirectoryId(), user.getName(), userBean.getUsername());
    }

    @Test
    public void testUpdateUserNoOp() throws CrowdException, PermissionException {
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
    public void testUpdateUserWithPassword() throws CrowdException, PermissionException {
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
    public void testChangePassword() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final String password = "pa55w0rd";

        final ArgumentCaptor<PasswordCredential> passwordCredentialArgumentCaptor = ArgumentCaptor.forClass(PasswordCredential.class);
        usersService.updatePassword(user.getName(), password);
        verify(directoryManager).updateUserCredential(anyLong(), anyString(), passwordCredentialArgumentCaptor.capture());
        assertEquals(password, passwordCredentialArgumentCaptor.getValue().getCredential());
    }

    // We kind of need to test all the exceptions here, but it's also pointless to test
    // all the exact mappings, because that's like repeating the implementation.
    // For this reason, we are just using WebApplicationException as a catch-all for now.
    // We are also not testing DirectoryNotFoundException and UserNotFoundException here,
    // because these cases won't happen anymore after a user has been found by its name.

    @Test(expected = WebApplicationException.class)
    public void testGetUserDirectoryNotFoundException() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());
        doThrow(new DirectoryNotFoundException(user.getDirectoryId())).when(directoryManager).findUserByName(anyLong(), anyString());
        usersService.getUser(user.getName());
    }

    @Test(expected = WebApplicationException.class)
    public void testGetUserOperationFailedException() throws CrowdException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());
        doThrow(new OperationFailedException()).when(directoryManager).findUserByName(anyLong(), anyString());
        usersService.getUser(user.getName());
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameUserDirectoryPermissionException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new DirectoryPermissionException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());
        usersService.updateUser(user.getName(), userBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameUserUserAlreadyExistsException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new UserAlreadyExistsException(user.getDirectoryId(), userBean.getUsername())).when(directoryManager).renameUser(anyLong(), anyString(), anyString());
        usersService.updateUser(user.getName(), userBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameUserInvalidUserException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).renameUser(anyLong(), anyString(), anyString());
        usersService.updateUser(user.getName(), userBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testRenameUserOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setUsername("new_username");

        doThrow(new OperationFailedException()).when(directoryManager).renameUser(anyLong(), anyString(), anyString());
        usersService.updateUser(user.getName(), userBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateUserDirectoryPermissionException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setFullName("Other Full Name");

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateUser(anyLong(), any());
        usersService.updateUser(user.getName(), userBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateUserInvalidUserException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setFullName("Other Full Name");

        doThrow(new InvalidUserException(user, "message")).when(directoryManager).updateUser(anyLong(), any());
        usersService.updateUser(user.getName(), userBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdateUserOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        final UserBean userBean = new UserBean();
        userBean.setEmail("other@example.com");

        doThrow(new OperationFailedException()).when(directoryManager).updateUser(anyLong(), any());
        usersService.updateUser(user.getName(), userBean);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdatePasswordDirectoryPermissionException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        doThrow(new DirectoryPermissionException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());
        usersService.updatePassword(user.getName(), password);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdatePasswordInvalidCredentialException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        doThrow(new InvalidCredentialException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());
        usersService.updatePassword(user.getName(), password);
    }

    @Test(expected = WebApplicationException.class)
    public void testUpdatePasswordOperationFailedException() throws CrowdException, PermissionException {
        final User user = getTestUser();
        final String password = "pa55w0rd";
        doReturn(user).when(directoryManager).findUserByName(user.getDirectoryId(), user.getName());

        doThrow(new OperationFailedException()).when(directoryManager).updateUserCredential(anyLong(), anyString(), any());
        usersService.updatePassword(user.getName(), password);
    }

    private Directory getTestDirectory() {
        return new MockDirectoryInternal();
    }

    private User getTestUser() {
        return ImmutableUser.builder(getTestDirectory().getId(), "test")
                .displayName("Test User")
                .emailAddress("test@example.com")
                .build();
    }
}
