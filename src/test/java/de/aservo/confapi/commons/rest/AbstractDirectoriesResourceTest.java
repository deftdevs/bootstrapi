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

        doReturn(Collections.singletonList(initialDirectoryBean)).when(directoryService).getDirectories();

        final Response response = resource.getDirectories();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked") final DirectoriesBean directoriesBean = (DirectoriesBean) response.getEntity();

        assertEquals(initialDirectoryBean, directoriesBean.getDirectories().iterator().next());
    }

    @Test
    public void testAddDirectory() {
        DirectoryBean directoryBean = DirectoryBean.EXAMPLE_1;
        directoryBean.setCrowdUrl("http://localhost");
        directoryBean.setClientName("confluence-client");
        directoryBean.setAppPassword("test");

        doReturn(directoryBean).when(directoryService).setDirectory(directoryBean, false);

        final Response response = resource.setDirectory(Boolean.FALSE, directoryBean);
        assertEquals(200, response.getStatus());
        final DirectoryBean DirectoryBean = (DirectoryBean) response.getEntity();

        assertEquals(DirectoryBean.getName(), directoryBean.getName());
    }

}
