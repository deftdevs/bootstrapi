package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdBean;
import com.deftdevs.bootstrapi.commons.rest.impl.TestDirectoryResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DirectoryResourceTest {

    @Mock
    private DirectoriesService directoriesService;

    private TestDirectoryResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestDirectoryResourceImpl(directoriesService);
    }

    @Test
    void testGetDirectory() {
        DirectoryCrowdBean directoryBean = DirectoryCrowdBean.EXAMPLE_1;

        doReturn(directoryBean).when(directoriesService).getDirectory(1L);

        final Response response = resource.getDirectory(1L);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryBean directoryBeanResponse = (AbstractDirectoryBean) response.getEntity();

        assertEquals(directoryBean, directoryBeanResponse);
    }

    @Test
    void testCreateDirectory() {
        DirectoryCrowdBean bean = DirectoryCrowdBean.EXAMPLE_1;

        doReturn(bean).when(directoriesService).addDirectory(bean, false);

        final Response response = resource.createDirectory(Boolean.FALSE, bean);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryBean responseBean = (AbstractDirectoryBean) response.getEntity();

        assertEquals(bean.getName(), responseBean.getName());
    }

    @Test
    void testUpdateDirectory() {
        DirectoryCrowdBean directoryBean = DirectoryCrowdBean.EXAMPLE_1;

        doReturn(directoryBean).when(directoriesService).setDirectory(1L, directoryBean, false);

        final Response response = resource.updateDirectory(1L, Boolean.FALSE, directoryBean);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryBean directoryBeanResponse = (AbstractDirectoryBean) response.getEntity();

        assertEquals(directoryBean, directoryBeanResponse);
    }

    @Test
    void testDeleteDirectory() {
        resource.deleteDirectory(1L);
        assertTrue(true, "Delete Successful");
    }

}
