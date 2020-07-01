package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.DirectoriesBean;
import de.aservo.confapi.commons.model.DirectoryBean;
import de.aservo.confapi.commons.service.api.DirectoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class AbstractDirectoriesResourceTest {

    @Mock
    private DirectoryService directoryService;

    private TestDirectoriesResourceImpl resource;

    @Before
    public void setup() {
        resource = new TestDirectoriesResourceImpl(directoryService);
    }

    @Test
    public void testGetDirectories() {
        DirectoryBean initialDirectoryBean = DirectoryBean.EXAMPLE_1;
        DirectoriesBean directoriesBean = new DirectoriesBean(Collections.singleton(initialDirectoryBean));

        doReturn(directoriesBean).when(directoryService).getDirectories();

        final Response response = resource.getDirectories();
        assertEquals(200, response.getStatus());
        final DirectoriesBean directoriesBeanResponse = (DirectoriesBean) response.getEntity();

        assertEquals(initialDirectoryBean, directoriesBeanResponse.getDirectories().iterator().next());
    }

    @Test
    public void testSetDirectories() {
        DirectoryBean directoryBean1 = DirectoryBean.EXAMPLE_1;
        DirectoryBean directoryBean2 = DirectoryBean.EXAMPLE_3;
        DirectoriesBean directoriesBean = new DirectoriesBean(Arrays.asList(directoryBean1, directoryBean2));

        doReturn(directoriesBean).when(directoryService).setDirectories(directoriesBean, false);

        final Response response = resource.setDirectories(Boolean.FALSE, directoriesBean);
        assertEquals(200, response.getStatus());
        final DirectoriesBean directoriesBeanResponse = (DirectoriesBean) response.getEntity();

        assertEquals(directoriesBean.getDirectories().size(), directoriesBeanResponse.getDirectories().size());
    }

    @Test
    public void testAddDirectory() {
        DirectoryBean directoryBean = DirectoryBean.EXAMPLE_1;
        DirectoriesBean directoriesBean = new DirectoriesBean(Collections.singleton(directoryBean));

        doReturn(directoriesBean).when(directoryService).addDirectory(directoryBean, false);

        final Response response = resource.addDirectory(Boolean.FALSE, directoryBean);
        assertEquals(200, response.getStatus());
        final DirectoriesBean directoriesBeanResponse = (DirectoriesBean) response.getEntity();

        assertEquals(directoryBean.getName(), directoriesBeanResponse.getDirectories().iterator().next().getName());
    }

}
