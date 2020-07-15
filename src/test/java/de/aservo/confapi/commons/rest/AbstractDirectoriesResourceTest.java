package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.AbstractDirectoryBean;
import de.aservo.confapi.commons.model.DirectoriesBean;
import de.aservo.confapi.commons.model.DirectoryCrowdBean;
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
        DirectoryCrowdBean initialDirectoryBean = DirectoryCrowdBean.EXAMPLE_1;
        DirectoriesBean directoriesBean = new DirectoriesBean(Collections.singleton(initialDirectoryBean));

        doReturn(directoriesBean).when(directoryService).getDirectories();

        final Response response = resource.getDirectories();
        assertEquals(200, response.getStatus());
        final DirectoriesBean directoriesBeanResponse = (DirectoriesBean) response.getEntity();

        assertEquals(initialDirectoryBean, directoriesBeanResponse.getDirectories().iterator().next());
    }

    @Test
    public void testSetDirectories() {
        DirectoryCrowdBean directoryBean1 = DirectoryCrowdBean.EXAMPLE_1;
        DirectoryCrowdBean directoryBean2 = DirectoryCrowdBean.EXAMPLE_3;
        DirectoriesBean directoriesBean = new DirectoriesBean(Arrays.asList(directoryBean1, directoryBean2));

        doReturn(directoriesBean).when(directoryService).setDirectories(directoriesBean, false);

        final Response response = resource.setDirectories(Boolean.FALSE, directoriesBean);
        assertEquals(200, response.getStatus());
        final DirectoriesBean directoriesBeanResponse = (DirectoriesBean) response.getEntity();

        assertEquals(directoriesBean.getDirectories().size(), directoriesBeanResponse.getDirectories().size());
    }

    @Test
    public void testAddDirectory() {
        DirectoryCrowdBean bean = DirectoryCrowdBean.EXAMPLE_1;

        doReturn(bean).when(directoryService).addDirectory(bean, false);

        final Response response = resource.addDirectory(Boolean.FALSE, bean);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryBean responseBean = (AbstractDirectoryBean) response.getEntity();

        assertEquals(bean.getName(), responseBean.getName());
    }

}
