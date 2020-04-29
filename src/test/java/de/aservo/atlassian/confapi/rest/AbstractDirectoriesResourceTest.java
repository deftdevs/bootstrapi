package de.aservo.atlassian.confapi.rest;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.DirectoryType;
import com.atlassian.crowd.exception.DirectoryCurrentlySynchronisingException;
import com.atlassian.crowd.model.directory.DirectoryImpl;
import de.aservo.atlassian.confapi.exception.BadRequestException;
import de.aservo.atlassian.confapi.exception.InternalServerErrorException;
import de.aservo.atlassian.confapi.model.DirectoriesBean;
import de.aservo.atlassian.confapi.model.DirectoryBean;
import de.aservo.atlassian.confapi.model.ErrorCollection;
import de.aservo.atlassian.confapi.service.api.DirectoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

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
        Directory directory = createDirectory();
        DirectoryBean initialDirectoryBean = DirectoryBean.from(directory);

        doReturn(Collections.singletonList(initialDirectoryBean)).when(directoryService).getDirectories();

        final Response response = resource.getDirectories();
        assertEquals(200, response.getStatus());
        @SuppressWarnings("unchecked") final DirectoriesBean directoriesBean = (DirectoriesBean) response.getEntity();

        assertEquals(initialDirectoryBean, directoriesBean.getDirectories().iterator().next());
    }

    @Test
    public void testAddDirectory() throws BadRequestException, InternalServerErrorException {
        Directory directory = createDirectory();
        DirectoryBean directoryBean = DirectoryBean.from(directory);
        directoryBean.setCrowdUrl("http://localhost");
        directoryBean.setClientName("confluence-client");
        directoryBean.setAppPassword("test");

        doReturn(directoryBean).when(directoryService).setDirectory(directoryBean, false);

        final Response response = resource.setDirectory(Boolean.FALSE, directoryBean);
        assertEquals(200, response.getStatus());
        final DirectoryBean DirectoryBean = (DirectoryBean) response.getEntity();

        assertEquals(DirectoryBean.getName(), directoryBean.getName());
    }

    @Test
    public void testAddDirectoryWithError() throws BadRequestException, InternalServerErrorException {
        Directory directory = createDirectory();
        DirectoryBean directoryBean = DirectoryBean.from(directory);
        directoryBean.setCrowdUrl("http://localhost");
        directoryBean.setClientName("confluence-client");
        directoryBean.setAppPassword("test");

        DirectoryCurrentlySynchronisingException directoryCurrentlySynchronisingException = new DirectoryCurrentlySynchronisingException(1L);
        InternalServerErrorException internalServerErrorException = new InternalServerErrorException(directoryCurrentlySynchronisingException.getMessage());
        doThrow(internalServerErrorException).when(directoryService).setDirectory(directoryBean, false);

        final Response response = resource.setDirectory(Boolean.FALSE, directoryBean);
        assertEquals(internalServerErrorException.getStatus().getStatusCode(), response.getStatus());

        assertNotNull(response.getEntity());
        assertEquals(ErrorCollection.class, response.getEntity().getClass());
    }

    private Directory createDirectory() {
        return new DirectoryImpl("test", DirectoryType.CROWD, "test.class");
    }

}
