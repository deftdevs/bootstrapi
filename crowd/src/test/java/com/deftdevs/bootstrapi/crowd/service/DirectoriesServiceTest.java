package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.embedded.api.MockDirectoryInternal;
import com.atlassian.crowd.exception.CrowdException;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.exception.DirectoryInstantiationException;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalModel;
import com.deftdevs.bootstrapi.commons.model.GroupModel;
import com.deftdevs.bootstrapi.commons.model.UserModel;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.crowd.model.util.DirectoryModelUtil;
import com.deftdevs.bootstrapi.crowd.service.api.GroupsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DirectoriesServiceTest {

    @Mock
    private DirectoryManager directoryManager;

    @Mock
    private GroupsService groupsService;

    @Mock
    private UsersService usersService;

    private DirectoriesServiceImpl directoriesService;

    @BeforeEach
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

        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directoryInternalNew);
        final Map<String, AbstractDirectoryModel> directoryModels = Collections.singletonMap(directoryModel.getName(), directoryModel);
        doReturn(directoryModel).when(spy).addDirectory(directoryModel);

        spy.setDirectories(directoryModels);
        verify(spy).addDirectory(directoryModel);
    }

    @Test
    public void testSetDirectoriesAddNewUnsupportedType() {
        final Directory directoryInternal = getTestDirectoryInternal();
        final Directory directoryAzureAd = getTestDirectoryAzureAd();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(Collections.singletonList(directoryInternal)).when(spy).findAllDirectories();

        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directoryAzureAd);
        final Map<String, AbstractDirectoryModel> directoryModels = Collections.singletonMap(directoryModel.getName(), directoryModel);
        final boolean testConnection = false;

        assertThrows(BadRequestException.class, () -> {
            spy.setDirectories(directoryModels);
        });
    }

    @Test
    public void testSetDirectoriesSetExisting() {
        final Directory directory = getTestDirectoryInternal();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(Collections.singletonList(directory)).when(spy).findAllDirectories();

        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        final Map<String, AbstractDirectoryModel> directoryModels = Collections.singletonMap(directoryModel.getName(), directoryModel);
        doReturn(directoryModel).when(spy).setDirectory(directory.getId(), directoryModel);

        spy.setDirectories(directoryModels);
        verify(spy).setDirectory(directory.getId(), directoryModel);
    }

    @Test
    public void testSetDirectoriesSetExistingUnsupportedType() {
        final Directory directoryInternal = getTestDirectoryInternal();
        final Directory directoryAzureAd = getTestDirectoryAzureAd();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(List.of(directoryInternal, directoryAzureAd)).when(spy).findAllDirectories();

        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directoryAzureAd);
        final Map<String, AbstractDirectoryModel> directoryModels = Collections.singletonMap(directoryModel.getName(), directoryModel);

        assertThrows(BadRequestException.class, () -> {
            spy.setDirectories(directoryModels);
        });
    }

    @Test
    public void testAddDirectory() throws CrowdException {
        final Directory directory = getTestDirectoryInternalOther();
        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        // Return the same directory as passed as argument
        doAnswer(invocation -> invocation.getArgument(0, Directory.class)).when(directoryManager).addDirectory(any());

        directoriesService.addDirectory(directoryModel);
        verify(directoryManager).addDirectory(any());
    }

    @Test
    public void testAddDirectoryWithGroupsAndUsers() throws CrowdException {
        final Directory directory = getTestDirectoryInternalOther();
        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        assertEquals(DirectoryInternalModel.class, directoryModel.getClass());

        final DirectoryInternalModel directoryInternalModel = (DirectoryInternalModel) directoryModel;
        directoryInternalModel.setGroups(Collections.singletonList(GroupModel.EXAMPLE_1));
        directoryInternalModel.setUsers(Collections.singletonList(UserModel.EXAMPLE_1));

        // Return the same directory as passed as argument
        doAnswer(invocation -> invocation.getArgument(0, Directory.class)).when(directoryManager).addDirectory(any());
        // Return the same groups bean as passed as argument
        doAnswer(invocation -> invocation.getArgument(1, List.class)).when(groupsService).setGroups(anyLong(), any());
        // Return the same user beans as passed as argument
        doAnswer(invocation -> invocation.getArgument(1, Collection.class)).when(usersService).setUsers(anyLong(), anyList());

        directoriesService.addDirectory(directoryInternalModel);
        verify(directoryManager).addDirectory(any());
        verify(groupsService).setGroups(anyLong(), any());
        verify(usersService).setUsers(anyLong(), anyList());
    }

    @Test
    public void testAddDirectoryWithException() throws CrowdException {
        final Directory directory = getTestDirectoryInternalOther();
        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        doThrow(new DirectoryInstantiationException()).when(directoryManager).addDirectory(any());

        assertThrows(InternalServerErrorException.class, () -> {
            directoriesService.addDirectory(directoryModel);
        });
    }

    @Test
    public void testUpdateDirectory() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());

        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        // Return the same directory as passed as argument
        doAnswer(invocation -> invocation.getArgument(0, Directory.class)).when(directoryManager).updateDirectory(any());

        directoriesService.setDirectory(directory.getId(), directoryModel);
        verify(directoryManager).updateDirectory(any());
    }

    @Test
    public void testUpdateDirectoryWithGroupsAndUsers() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());

        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        assertEquals(DirectoryInternalModel.class, directoryModel.getClass());

        final DirectoryInternalModel directoryInternalModel = (DirectoryInternalModel) directoryModel;
        directoryInternalModel.setGroups(Collections.singletonList(GroupModel.EXAMPLE_1));
        directoryInternalModel.setUsers(Collections.singletonList(UserModel.EXAMPLE_1));

        // Return the same directory as passed as argument
        doAnswer(invocation -> invocation.getArgument(0, Directory.class)).when(directoryManager).updateDirectory(any());
        // Return the same groups bean as passed as argument
        doAnswer(invocation -> invocation.getArgument(1, List.class)).when(groupsService).setGroups(anyLong(), any());
        // Return the same user beans as passed as argument
        doAnswer(invocation -> invocation.getArgument(1, Collection.class)).when(usersService).setUsers(anyLong(), anyList());

        directoriesService.setDirectory(directory.getId(), directoryInternalModel);
        verify(directoryManager).updateDirectory(any());
        verify(groupsService).setGroups(anyLong(), any());
        verify(usersService).setUsers(anyLong(), anyList());
    }

    @Test
    public void testUpdateDirectoryNotFound() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        doThrow(new DirectoryNotFoundException(directory.getName())).when(directoryManager).findDirectoryById(directory.getId());

        assertThrows(NotFoundException.class, () -> {
            directoriesService.setDirectory(directory.getId(), directoryModel);
        });
    }

    @Test
    public void testUpdateDirectoryWithException() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());
        doThrow(new DirectoryNotFoundException(directory.getName())).when(directoryManager).updateDirectory(any());

        assertThrows(InternalServerErrorException.class, () -> {
            directoriesService.setDirectory(directory.getId(), directoryModel);
        });
    }

    @Test
    public void testGetDirectoryNotFoundException() throws DirectoryNotFoundException {
        final Directory directory = getTestDirectoryInternal();
        doThrow(new DirectoryNotFoundException(directory.getName())).when(directoryManager).findDirectoryById(directory.getId());

        assertThrows(NotFoundException.class, () -> {
            directoriesService.getDirectory(directory.getId());
        });
    }

    @Test
    public void testDeleteDirectories() throws CrowdException {
        final Directory directoryInternal = getTestDirectoryInternal();
        final Directory directoryAzureAd = getTestDirectoryAzureAd();
        final Directory directoryInternalOther = getTestDirectoryInternalOther();
        final DirectoriesServiceImpl spy = spy(directoriesService);
        doReturn(List.of(directoryInternal, directoryAzureAd, directoryInternalOther)).when(spy).findAllDirectories();

        spy.deleteDirectories(true);
        verify(spy, never()).deleteDirectory(directoryInternal.getId());
        verify(spy).deleteDirectory(directoryAzureAd.getId());
        verify(spy).deleteDirectory(directoryInternalOther.getId());
    }

    @Test
    public void testDeleteDirectoriesMissingForce() {
        assertThrows(BadRequestException.class, () -> {
            directoriesService.deleteDirectories(false);
        });
    }

    @Test
    public void testDeleteDirectory() throws CrowdException {
        final Directory directory = getTestDirectoryAzureAd();
        doReturn(List.of(getTestDirectoryInternal(), directory)).when(directoryManager).searchDirectories(any());
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());
        directoriesService.deleteDirectory(directory.getId());
        verify(directoryManager).removeDirectory(directory);
    }

    @Test
    public void testDeleteDirectoryLast() {
        final Directory directory = getTestDirectoryInternal();
        doReturn(Collections.singletonList(directory)).when(directoryManager).searchDirectories(any());

        assertThrows(BadRequestException.class, () -> {
            directoriesService.deleteDirectory(directory.getId());
        });
    }

    // This case should actually not happen, but we need the code coverage
    @Test
    public void testDeleteDirectoryNotFoundAfterAlreadyFound() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        doReturn(List.of(directory, getTestDirectoryAzureAd())).when(directoryManager).searchDirectories(any());
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());
        doThrow(new DirectoryNotFoundException("Directory")).when(directoryManager).removeDirectory(directory);

        assertThrows(InternalServerErrorException.class, () -> {
            directoriesService.deleteDirectory(directory.getId());
        });
    }

    @Test
    public void testDeleteDirectorySynchronizing() throws CrowdException {
        final Directory directory = getTestDirectoryInternal();
        doReturn(List.of(directory, getTestDirectoryAzureAd())).when(directoryManager).searchDirectories(any());
        doReturn(directory).when(directoryManager).findDirectoryById(directory.getId());
        doThrow(new DirectoryCurrentlySynchronisingException(1L)).when(directoryManager).removeDirectory(directory);

        assertThrows(ServiceUnavailableException.class, () -> {
            directoriesService.deleteDirectory(directory.getId());
        });
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
        return List.of(getTestDirectoryInternal(), getTestDirectoryAzureAd(), getTestDirectoryInternalOther());
    }

    private Date toDate(
            final LocalDate localDate) {

        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

}
