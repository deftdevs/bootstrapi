package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.embedded.api.MockDirectory;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.search.query.entity.EntityQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class DirectoriesServiceTest {

    @Mock
    private DirectoryManager directoryManager;

    private DirectoriesServiceImpl directoriesService;

    @Before
    public void setup() {
        directoriesService = new DirectoriesServiceImpl(directoryManager);
    }

    @Test
    public void testGetDirectories() throws DirectoryNotFoundException {
        doReturn(Collections.singletonList(new MockDirectory())).when(directoryManager).searchDirectories(any(EntityQuery.class));
        assertNotNull(directoriesService.getDirectories());
    }

    @Test
    public void testGetDirectory() throws DirectoryNotFoundException {
        doReturn(new MockDirectory()).when(directoryManager).findDirectoryById(anyLong());
        assertNotNull(directoriesService.getDirectory(1L));
    }

    @Test
    public void testGetDirectoryNullWhenNotFoundWithNull() throws DirectoryNotFoundException {
        doReturn(null).when(directoryManager).findDirectoryById(anyLong());
        assertNull(directoriesService.getDirectory(1L));
    }

    @Test
    public void testGetDirectoryNullWhenNotFoundWithException() throws DirectoryNotFoundException {
        doThrow(new DirectoryNotFoundException("Directory")).when(directoryManager).findDirectoryById(anyLong());
        assertNull(directoriesService.getDirectory(1L));
    }

}
