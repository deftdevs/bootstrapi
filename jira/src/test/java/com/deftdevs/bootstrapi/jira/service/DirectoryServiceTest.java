package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryLdapModel;
import com.deftdevs.bootstrapi.jira.model.util.DirectoryModelUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.atlassian.crowd.directory.RemoteCrowdDirectory.*;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.*;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.SyncGroupMembershipsAfterAuth.WHEN_AUTHENTICATION_CREATED_THE_USER;
import static com.atlassian.crowd.model.directory.DirectoryImpl.ATTRIBUTE_KEY_USE_NESTED_GROUPS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectoryServiceTest {

    @Mock
    private CrowdDirectoryService crowdDirectoryService;

    private DirectoriesServiceImpl directoryService;

    @BeforeEach
    public void setup() {
        directoryService = new DirectoriesServiceImpl(crowdDirectoryService);
    }

    @Test
    void testGetDirectories() {
        final Directory directory = createDirectory();
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        final Map<String, AbstractDirectoryModel> directories = directoryService.getDirectories();
        assertEquals(directories.values().iterator().next(), DirectoryModelUtil.toDirectoryModel(directory));
    }

    @Test
    void testGetDirectoriesUriException() {
        Directory directory = createDirectory("öäöää://uhveuehvde");
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        assertThrows(IllegalArgumentException.class, () -> {
            directoryService.getDirectories();
        });
    }

    @Test
    void testGetDirectory() {
        Directory directory = createDirectory();
        doReturn(directory).when(crowdDirectoryService).findDirectoryById(1L);

        AbstractDirectoryModel directoryModel = directoryService.getDirectory(1L);

        assertEquals(DirectoryModelUtil.toDirectoryModel(directory), directoryModel);
    }

    @Test
    void testGetDirectoryNotExisting() {
        assertThrows(NotFoundException.class, () -> {
            directoryService.getDirectory(1L);
        });
    }

    @Test
    void testSetDirectoriesWithoutExistingDirectory() {
        final Directory directory = createDirectory();
        doReturn(directory).when(crowdDirectoryService).addDirectory(any());
        doReturn(Collections.emptyList()).when(crowdDirectoryService).findAllDirectories();

        final DirectoryCrowdModel directoryModel = (DirectoryCrowdModel) DirectoryModelUtil.toDirectoryModel(directory);
        directoryModel.getServer().setAppPassword("test");
        directoryService.setDirectories(Collections.singletonMap(directoryModel.getName(), directoryModel));
        assertTrue(true, "Update Successful");
    }

    @Test
    void testSetDirectoriesWithExistingDirectory() {
        final Directory directory = createDirectory();
        doReturn(directory).when(crowdDirectoryService).findDirectoryById(1L);
        doReturn(directory).when(crowdDirectoryService).updateDirectory(any());
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        final DirectoryCrowdModel directoryModel = (DirectoryCrowdModel) DirectoryModelUtil.toDirectoryModel(directory);
        directoryModel.getServer().setAppPassword("test");
        final Map<String, ? extends AbstractDirectoryModel> directoryAdded = directoryService.setDirectories(Collections.singletonMap(directoryModel.getName(), directoryModel));
        assertEquals(directoryAdded.values().iterator().next().getName(), directoryModel.getName());
    }

    @Test
    void testSetDirectoriesWithConnectionTest() {
        final Directory directory = createDirectory();
        doReturn(directory).when(crowdDirectoryService).findDirectoryById(1L);
        doReturn(directory).when(crowdDirectoryService).updateDirectory(any());
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        final DirectoryCrowdModel directoryModel = (DirectoryCrowdModel) DirectoryModelUtil.toDirectoryModel(directory);
        directoryModel.getServer().setAppPassword("test");
        final Map<String, ? extends AbstractDirectoryModel> directoryAdded = directoryService.setDirectories(Collections.singletonMap(directoryModel.getName(), directoryModel));
        assertEquals(directoryAdded.values().iterator().next().getName(), directoryModel.getName());
    }

    @Test
    void testSetDirectoryDefault() {
        Directory directory = createDirectory();

        doReturn(directory).when(crowdDirectoryService).findDirectoryById(1L);
        doReturn(directory).when(crowdDirectoryService).updateDirectory(any());

        DirectoryCrowdModel directoryModel = (DirectoryCrowdModel) DirectoryModelUtil.toDirectoryModel(directory);
        directoryModel.setDescription(null);
        directoryModel.setName(null);

        directoryModel.getServer().setAppPassword("test");
        AbstractDirectoryModel directoryAdded = directoryService.setDirectory(1L, directoryModel);

        assertEquals(directoryModel.getDescription(), directoryAdded.getDescription());
        assertEquals(directory.getName(), directoryAdded.getName());
    }

    @Test
    void testSetDirectoryWithConnectionTest() {
        Directory directory = createDirectory();

        doReturn(directory).when(crowdDirectoryService).findDirectoryById(1L);
        doReturn(directory).when(crowdDirectoryService).updateDirectory(any());

        DirectoryCrowdModel directoryModel = (DirectoryCrowdModel) DirectoryModelUtil.toDirectoryModel(directory);
        directoryModel.getServer().setAppPassword("test");
        AbstractDirectoryModel directoryAdded = directoryService.setDirectory(1L, directoryModel);

        assertEquals(directoryModel.getName(), directoryAdded.getName());
        assertEquals(directoryModel.getId(), directoryAdded.getId());
    }

    @Test
    void testSetDirectoryUnsupportedType() {
        final DirectoryLdapModel directoryLdapModel = DirectoryLdapModel.builder().build();

        assertThrows(BadRequestException.class, () -> {
            directoryService.setDirectory(1L, directoryLdapModel);
        });
    }

    @Test
    void testSetDirectoryNotExisting() {
        final Directory directory = createDirectory();
        final AbstractDirectoryModel directoryModel = DirectoryModelUtil.toDirectoryModel(directory);

        assertThrows(NotFoundException.class, () -> {
            directoryService.setDirectory(1L, directoryModel);
        });
    }

    @Test
    void testAddDirectoryUriException() {
        Directory responseDirectory = createDirectory("öäöää://uhveuehvde");
        doReturn(responseDirectory).when(crowdDirectoryService).addDirectory(any());

        Directory directory = createDirectory();
        DirectoryCrowdModel directoryModel = (DirectoryCrowdModel) DirectoryModelUtil.toDirectoryModel(directory);

        assertThrows(IllegalArgumentException.class, () -> {
            directoryService.addDirectory(directoryModel);
        });
    }

    @Test
    void testAddDirectory() {
        Directory directory = createDirectory();
        doReturn(directory).when(crowdDirectoryService).addDirectory(any(Directory.class));

        DirectoryCrowdModel directoryModel = (DirectoryCrowdModel) DirectoryModelUtil.toDirectoryModel(directory);
        directoryModel.getServer().setAppPassword("test");

        AbstractDirectoryModel directoryAdded = directoryService.addDirectory(directoryModel);
        assertEquals(directoryAdded.getName(), directoryModel.getName());
        assertEquals(directoryAdded.getId(), directoryModel.getId());
    }

    @Test
    void testAddDirectoryUnsupportedType() {
        final DirectoryLdapModel directoryLdapModel = DirectoryLdapModel.builder().build();

        assertThrows(BadRequestException.class, () -> {
            directoryService.addDirectory(directoryLdapModel);
        });
    }

    @Test
    void testDeleteDirectoriesWithoutForceParameter() {
        assertThrows(BadRequestException.class, () -> {
            directoryService.deleteDirectories(false);
        });
    }

    @Test
    void testDeleteDirectories() throws DirectoryCurrentlySynchronisingException {
        Directory directory = createDirectory();
        doReturn(directory).when(crowdDirectoryService).findDirectoryById(1L);
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        directoryService.deleteDirectories(true);

        verify(crowdDirectoryService).removeDirectory(1L);
    }

    @Test
    void testDeleteDirectoriesWithoutInternalDirectory() {
        Directory directory = createDirectory("http://localhost", DirectoryType.INTERNAL);
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        directoryService.deleteDirectories(true);

        verify(crowdDirectoryService).findAllDirectories();
    }

    @Test
    void testDeleteDirectory() throws DirectoryCurrentlySynchronisingException {
        doReturn(createDirectory()).when(crowdDirectoryService).findDirectoryById(1L);

        directoryService.deleteDirectory(1L);

        verify(crowdDirectoryService).removeDirectory(1L);
    }

    @Test
    void testDeleteDirectoryNotExisting() {
        assertThrows(NotFoundException.class, () -> {
            directoryService.deleteDirectory(1L);
        });
    }

    @Test
    void testDeleteDirectoryCurrentlySynchronisingException() throws DirectoryCurrentlySynchronisingException {
        doReturn(createDirectory()).when(crowdDirectoryService).findDirectoryById(1L);
        doThrow(new DirectoryCurrentlySynchronisingException(1L)).when(crowdDirectoryService).removeDirectory(1L);

        assertThrows(ServiceUnavailableException.class, () -> {
            directoryService.deleteDirectory(1L);
        });
    }

    private Directory createDirectory() {
        return createDirectory("http://localhost");
    }

    private Directory createDirectory(String url) {
        return createDirectory(url, DirectoryType.CROWD);
    }

    private Directory createDirectory(String url, DirectoryType directoryType) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(CROWD_SERVER_URL, url);
        attributes.put(APPLICATION_PASSWORD, "test");
        attributes.put(APPLICATION_NAME, "confluence-client");
        attributes.put(CACHE_SYNCHRONISE_INTERVAL, "3600");
        attributes.put(ATTRIBUTE_KEY_USE_NESTED_GROUPS, "false");
        attributes.put(INCREMENTAL_SYNC_ENABLED, "true");
        attributes.put(SYNC_GROUP_MEMBERSHIP_AFTER_SUCCESSFUL_USER_AUTH_ENABLED, WHEN_AUTHENTICATION_CREATED_THE_USER.getValue());

        ImmutableDirectory immutableDirectory = ImmutableDirectory.builder("test", directoryType, "test.class")
                .setId(1L)
                .setCreatedDate(new Date())
                .setUpdatedDate(new Date())
                .setAttributes(attributes).build();
        return new DirectoryImpl(immutableDirectory);  //required because directory needs to be mutable throughout the test
    }

}
