package de.aservo.confapi.jira.service;

import com.atlassian.crowd.embedded.api.CrowdDirectoryService;
import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.model.directory.ImmutableDirectory;
import de.aservo.confapi.commons.exception.InternalServerErrorException;
import de.aservo.confapi.commons.model.DirectoriesBean;
import de.aservo.confapi.commons.model.DirectoryCrowdBean;
import de.aservo.confapi.jira.model.util.DirectoryBeanUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.atlassian.crowd.directory.RemoteCrowdDirectory.*;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.*;
import static com.atlassian.crowd.directory.SynchronisableDirectoryProperties.SyncGroupMembershipsAfterAuth.WHEN_AUTHENTICATION_CREATED_THE_USER;
import static com.atlassian.crowd.model.directory.DirectoryImpl.ATTRIBUTE_KEY_USE_NESTED_GROUPS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DirectoryServiceTest {

    @Mock
    private CrowdDirectoryService crowdDirectoryService;

    @InjectMocks
    private DirectoriesServiceImpl directoriesService;

    @Test
    public void testGetDirectories() {
        Directory directory = createDirectory();
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();

        DirectoriesBean directories = directoriesService.getDirectories();

        assertEquals(directories.getDirectories().iterator().next(), DirectoryBeanUtil.toDirectoryBean(directory));
    }

    @Test
    public void testSetDirectoryWithoutExistingDirectory() {
        Directory directory = createDirectory();
        doReturn(directory).when(crowdDirectoryService).addDirectory(any(Directory.class));

        DirectoryCrowdBean directoryBean = (DirectoryCrowdBean) DirectoryBeanUtil.toDirectoryBean(directory);
        directoryBean.setServer(new DirectoryCrowdBean.DirectoryCrowdServer());
        directoryBean.getServer().setAppPassword("test");
        DirectoriesBean directoriesBean = directoriesService.setDirectories(new DirectoriesBean(Collections.singletonList(directoryBean)), false);

        assertEquals(directoriesBean.getDirectories().iterator().next().getName(), directoryBean.getName());
    }

    @Test
    public void testSetDirectoryWithExistingDirectory() {
        Directory directory = createDirectory();
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();
        doReturn(directory).when(crowdDirectoryService).addDirectory(any(Directory.class));

        DirectoryCrowdBean directoryBean = (DirectoryCrowdBean) DirectoryBeanUtil.toDirectoryBean(directory);
        directoryBean.setServer(new DirectoryCrowdBean.DirectoryCrowdServer());
        directoryBean.getServer().setAppPassword("test");
        DirectoriesBean directoriesBean = directoriesService.setDirectories(new DirectoriesBean(Collections.singletonList(directoryBean)), false);

        assertEquals(directoriesBean.getDirectories().iterator().next().getName(), directoryBean.getName());
    }

    @Test
    public void testSetDirectoryWithConnectionTest() {
        Directory directory = createDirectory();
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();
        doReturn(directory).when(crowdDirectoryService).addDirectory(any(Directory.class));

        DirectoryCrowdBean directoryBean = (DirectoryCrowdBean) DirectoryBeanUtil.toDirectoryBean(directory);
        directoryBean.setServer(new DirectoryCrowdBean.DirectoryCrowdServer());
        directoryBean.getServer().setAppPassword("test");
        DirectoriesBean directoriesBean = directoriesService.setDirectories(new DirectoriesBean(Collections.singletonList(directoryBean)), true);

        verify(crowdDirectoryService).testConnection(any());
        assertEquals(directoriesBean.getDirectories().iterator().next().getName(), directoryBean.getName());
    }

    @Test(expected = InternalServerErrorException.class)
    public void testSetDirectoryDirectoryCurrentlySynchronisingException() throws DirectoryCurrentlySynchronisingException {
        Directory directory = createDirectory();
        doReturn(Collections.singletonList(directory)).when(crowdDirectoryService).findAllDirectories();
        doThrow(new DirectoryCurrentlySynchronisingException(1L)).when(crowdDirectoryService).removeDirectory(1L);

        DirectoryCrowdBean directoryBean = (DirectoryCrowdBean) DirectoryBeanUtil.toDirectoryBean(directory);
        directoryBean.setServer(new DirectoryCrowdBean.DirectoryCrowdServer());
        directoryBean.getServer().setAppPassword("test");
        DirectoriesBean directoriesBean = directoriesService.setDirectories(new DirectoriesBean(Collections.singletonList(directoryBean)), false);
    }

    private Directory createDirectory() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(CROWD_SERVER_URL, "http://localhost");
        attributes.put(APPLICATION_PASSWORD, "test");
        attributes.put(APPLICATION_NAME, "confluence-client");
        attributes.put(CACHE_SYNCHRONISE_INTERVAL, "3600");
        attributes.put(ATTRIBUTE_KEY_USE_NESTED_GROUPS, "false");
        attributes.put(INCREMENTAL_SYNC_ENABLED, "true");
        attributes.put(SYNC_GROUP_MEMBERSHIP_AFTER_SUCCESSFUL_USER_AUTH_ENABLED, WHEN_AUTHENTICATION_CREATED_THE_USER.getValue());
        return ImmutableDirectory.builder("test", DirectoryType.CROWD, "test.class").setId(1L).setAttributes(attributes).build();
    }

}
