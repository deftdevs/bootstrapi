package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.embedded.api.MockDirectoryInternal;
import com.atlassian.crowd.exception.CrowdException;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.exception.DirectoryInstantiationException;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.commons.exception.ServiceUnavailableException;
import de.aservo.confapi.commons.model.AbstractDirectoryBean;
import de.aservo.confapi.commons.model.DirectoriesBean;
import de.aservo.confapi.commons.model.DirectoryInternalBean;
import de.aservo.confapi.commons.model.GroupBean;
import de.aservo.confapi.commons.model.UserBean;
import de.aservo.confapi.commons.service.api.UsersService;
import de.aservo.confapi.crowd.model.GroupsBean;
import de.aservo.confapi.crowd.model.util.DirectoryBeanUtil;
import de.aservo.confapi.crowd.service.api.GroupsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DirectoriesServiceTest {

    @Mock
    private DirectoryManager directoryManager;

    @Mock
    private GroupsService groupsService;

    @Mock
    private UsersService usersService;

    private DirectoriesServiceImpl directoriesService;

    @Before
    public void setup() {
        directoriesService = new DirectoriesServiceImpl(directoryManager, groupsService, usersService);
    }

    @Test
    public void testGetDirectories() throws DirectoryNotFoundException {
        doReturn(Collections.singletonList(new MockDirectoryInternal())).when(directoryManager).searchDirectories(any());
        assertNotNull(directoriesService.getDirectories());
    }

    @Test
    public void testGetDirectory() throws DirectoryNotFoundException {
        doReturn(new MockDirectoryInternal()).when(directoryManager).findDirectoryById(anyLong());
        assertNotNull(directoriesService.getDirectory(1L));
    }

    @Test
    public void testSetDirectoriesAddNew() {
        final Directory directoryInternal = getTestDirectoryInternal();
        final Directory directoryInternalNew = getTestDirectoryInternalOther();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(Collections.singletonList(directoryInternal)).when(spy).findAllDirectories();

        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directoryInternalNew);
        final DirectoriesBean directoriesBean = new DirectoriesBean(Collections.singletonList(directoryBean));
        final boolean testConnection = false;
        doReturn(null).when(spy).addDirectory(directoryBean, testConnection);

        spy.setDirectories(directoriesBean, testConnection);
        verify(spy).addDirectory(directoryBean, testConnection);
    }

    @Test(expected = BadRequestException.class)
    public void testSetDirectoriesAddNewUnsupportedType() {
        final Directory directoryInternal = getTestDirectoryInternal();
        final Directory directoryAzureAd = getTestDirectoryAzureAd();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(Collections.singletonList(directoryInternal)).when(spy).findAllDirectories();

        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directoryAzureAd);
        final DirectoriesBean directoriesBean = new DirectoriesBean(Collections.singletonList(directoryBean));
        final boolean testConnection = false;
        spy.setDirectories(directoriesBean, testConnection);
    }

    @Test
    public void testSetDirectoriesSetExisting() {
        final Directory directory = getTestDirectoryInternal();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(Collections.singletonList(directory)).when(spy).findAllDirectories();

        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        final DirectoriesBean directoriesBean = new DirectoriesBean(Collections.singletonList(directoryBean));
        final boolean testConnection = false;
        doReturn(null).when(spy).setDirectory(directory.getId(), directoryBean, testConnection);

        spy.setDirectories(directoriesBean, testConnection);
        verify(spy).setDirectory(directory.getId(), directoryBean, testConnection);
    }

    @Test(expected = BadRequestException.class)
    public void testSetDirectoriesSetExistingUnsupportedType() {
        final Directory directoryInternal = getTestDirectoryInternal();
        final Directory directoryAzureAd = getTestDirectoryAzureAd();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(ListUtil.of(directoryInternal, directoryAzureAd)).when(spy).findAllDirectories();
        doReturn(directoryAzureAd).when(spy).findDirectory(directoryAzureAd.getId());

        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directoryAzureAd);
        final DirectoriesBean directoriesBean = new DirectoriesBean(Collections.singletonList(directoryBean));
        final boolean testConnection = false;
        spy.setDirectories(directoriesBean, testConnection);
    }

    @Test
    public void testAddDirectory() throws CrowdException {
        final Directory directory = getTestDirectoryInternalOther();
        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        // Return the same directory as passed as argument
        doAnswer(invocation -> invocation.getArgumentAt(0, Directory.class)).when(directoryManager).addDirectory(any());

        directoriesService.addDirectory(directoryBean, false);
        verify(directoryManager).addDirectory(any());
    }

    @Test
    public void testAddDirectoryWithGroupsAndUsers() throws CrowdException {
        final Directory directory = getTestDirectoryInternalOther();
        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        assertEquals(DirectoryInternalBean.class, directoryBean.getClass());

        final DirectoryInternalBean directoryInternalBean = (DirectoryInternalBean) directoryBean;
        directoryInternalBean.setGroups(Collections.singletonList(GroupBean.EXAMPLE_1));
        directoryInternalBean.setUsers(Collections.singletonList(UserBean.EXAMPLE_1));

        // Return the same directory as passed as argument
        doAnswer(invocation -> invocation.getArgumentAt(0, Directory.class)).when(directoryManager).addDirectory(any());
        // Return the same groups bean as passed as argument
        doAnswer(invocation -> invocation.getArgumentAt(1, GroupsBean.class)).when(groupsService).setGroups(anyLong(), any());
        // Return the same user beans as passed as argument
        doAnswer(invocation -> invocation.getArgumentAt(1, Collection.class)).when(usersService).setUsers(anyLong(), any());

        directoriesService.addDirectory(directoryInternalBean, false);
        verify(directoryManager).addDirectory(any());
        verify(groupsService).setGroups(anyLong(), any());
        verify(usersService).setUsers(anyLong(), any());
    }

    @Test(expected = InternalServerErrorException.class)
    public void testAddDirectoryWithException() throws CrowdException {
        final Directory directory = getTestDirectoryInternalOther();
        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        doThrow(new DirectoryInstantiationException()).when(directoryManager).addDirectory(any());

        directoriesService.addDirectory(directoryBean, false);
    }

    @Test
    public void testUpdateDirectory() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());

        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        // Return the same directory as passed as argument

        doAnswer(invocation -> invocation.getArgumentAt(0, Directory.class)).when(directoryManager).updateDirectory(any());

        directoriesService.setDirectory(directory.getId(), directoryBean, false);
        verify(directoryManager).updateDirectory(any());
    }

    @Test
    public void testUpdateDirectoryWithGroupsAndUsers() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());

        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        assertEquals(DirectoryInternalBean.class, directoryBean.getClass());

        final DirectoryInternalBean directoryInternalBean = (DirectoryInternalBean) directoryBean;
        directoryInternalBean.setGroups(Collections.singletonList(GroupBean.EXAMPLE_1));
        directoryInternalBean.setUsers(Collections.singletonList(UserBean.EXAMPLE_1));

        // Return the same directory as passed as argument
        doAnswer(invocation -> invocation.getArgumentAt(0, Directory.class)).when(directoryManager).updateDirectory(any());
        // Return the same groups bean as passed as argument
        doAnswer(invocation -> invocation.getArgumentAt(1, GroupsBean.class)).when(groupsService).setGroups(anyLong(), any());
        // Return the same user beans as passed as argument
        doAnswer(invocation -> invocation.getArgumentAt(1, Collection.class)).when(usersService).setUsers(anyLong(), any());

        directoriesService.setDirectory(directory.getId(), directoryInternalBean, false);
        verify(directoryManager).updateDirectory(any());
        verify(groupsService).setGroups(anyLong(), any());
        verify(usersService).setUsers(anyLong(), any());
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateDirectoryNotFound() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        doThrow(new DirectoryNotFoundException(directory.getName())).when(directoryManager).findDirectoryById(directory.getId());

        directoriesService.setDirectory(directory.getId(), directoryBean, false);
    }

    @Test(expected = InternalServerErrorException.class)
    public void testUpdateDirectoryWithException() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        final AbstractDirectoryBean directoryBean = DirectoryBeanUtil.toDirectoryBean(directory);
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());
        doThrow(new DirectoryNotFoundException(directory.getName())).when(directoryManager).updateDirectory(any());

        directoriesService.setDirectory(directory.getId(), directoryBean, false);
    }

    @Test(expected = NotFoundException.class)
    public void testGetDirectoryNotFoundException() throws DirectoryNotFoundException {
        final Directory directory = getTestDirectoryInternal();
        doThrow(new DirectoryNotFoundException(directory.getName())).when(directoryManager).findDirectoryById(directory.getId());
        directoriesService.getDirectory(directory.getId());
    }

    @Test
    public void testDeleteDirectories() throws CrowdException {
        final Directory directoryInternal = getTestDirectoryInternal();
        final Directory directoryAzureAd = getTestDirectoryAzureAd();
        final Directory directoryInternalOther = getTestDirectoryInternalOther();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(ListUtil.of(directoryInternal, directoryAzureAd, directoryInternalOther)).when(spy).findAllDirectories();

        spy.deleteDirectories(true);
        verify(spy, never()).deleteDirectory(directoryInternal.getId());
        verify(spy).deleteDirectory(directoryAzureAd.getId());
        verify(spy).deleteDirectory(directoryInternalOther.getId());
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteDirectoriesMissingForce() {
        directoriesService.deleteDirectories(false);
    }

    @Test
    public void testDeleteDirectory() throws CrowdException {
        final Directory directory = getTestDirectoryAzureAd();
        doReturn(ListUtil.of(getTestDirectoryInternal(), directory)).when(directoryManager).searchDirectories(any());
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());
        directoriesService.deleteDirectory(directory.getId());
        verify(directoryManager).removeDirectory(directory);
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteDirectoryLast() {
        final Directory directory = getTestDirectoryInternal();
        doReturn(Collections.singletonList(directory)).when(directoryManager).searchDirectories(any());
        directoriesService.deleteDirectory(directory.getId());
    }

    // This case should actually not happen, but we need the code coverage
    @Test(expected = InternalServerErrorException.class)
    public void testDeleteDirectoryNotFoundAfterAlreadyFound() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        doReturn(ListUtil.of(directory, getTestDirectoryAzureAd())).when(directoryManager).searchDirectories(any());
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());
        doThrow(new DirectoryNotFoundException("Directory")).when(directoryManager).removeDirectory(directory);
        directoriesService.deleteDirectory(directory.getId());
    }

    @Test(expected = ServiceUnavailableException.class)
    public void testDeleteDirectorySynchronizing() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        doReturn(ListUtil.of(directory, getTestDirectoryAzureAd())).when(directoryManager).searchDirectories(any());
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());
        doThrow(new DirectoryCurrentlySynchronisingException(1L)).when(directoryManager).removeDirectory(directory);
        directoriesService.deleteDirectory(directory.getId());
    }

    @Test
    public void testDirectoryComparator() {
        final List<Directory> directories = new ArrayList<>(getTestDirectories());
        directories.sort(new DirectoriesServiceImpl.DirectoryComparator());
        assertEquals(2L, (long) directories.get(0).getId());
        assertEquals(3L, (long) directories.get(1).getId());
        assertEquals(1L, (long) directories.get(2).getId());
    }

    private Directory getTestDirectoryInternal() {
        return ImmutableDirectory
                .builder("Old Internal Directory", DirectoryType.INTERNAL, "internal")
                .setId(1L)
                .setCreatedDate(toDate(LocalDate.now().minusDays(3))).
                build();
    }

    private Directory getTestDirectoryAzureAd() {
        return ImmutableDirectory
                .builder("Azure AD Directory", DirectoryType.AZURE_AD, "azuread")
                .setId(2L)
                .setCreatedDate(toDate(LocalDate.now().minusDays(1)))
                .build();
    }

    private Directory getTestDirectoryInternalOther() {
        return ImmutableDirectory
                .builder("New Internal Directory", DirectoryType.INTERNAL, "internal")
                .setId(3L)
                .setCreatedDate(toDate(LocalDate.now()))
                .build();
    }

    private List<Directory> getTestDirectories() {
        return ListUtil.of(getTestDirectoryInternal(), getTestDirectoryAzureAd(), getTestDirectoryInternalOther());
    }

    private Date toDate(
            final LocalDate localDate) {

        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private static class ListUtil {

        static <T> List<T> of(T... elements) {
            List<T> list = new ArrayList<>();
            Collections.addAll(list, elements);
            return list;
        }

    }

}
