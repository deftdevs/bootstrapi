package de.aservo.confapi.crowd.rest;

import de.aservo.confapi.crowd.model.DirectoryBean;
import de.aservo.confapi.crowd.service.api.DirectoriesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class DirectoriesResourceTest {

    @Mock
    private DirectoriesService directoriesService;

    private DirectoriesResourceImpl directoriesResource;

    @Before
    public void setup() {
        directoriesResource = new DirectoriesResourceImpl(directoriesService);
    }

    @Test
    public void testGetDirectory() {
        final DirectoryBean directoryBean = DirectoryBean.EXAMPLE_1;
        doReturn(directoryBean).when(directoriesService).getDirectory(anyLong());

        final Response response = directoriesResource.getDirectory(directoryBean.getId());
        assertEquals(200, response.getStatus());

        final DirectoryBean responseDirectoryBean = (DirectoryBean) response.getEntity();
        assertEquals(directoryBean, responseDirectoryBean);
    }

}
