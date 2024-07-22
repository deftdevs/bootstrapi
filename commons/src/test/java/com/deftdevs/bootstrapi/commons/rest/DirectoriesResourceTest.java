package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdBean;
import com.deftdevs.bootstrapi.commons.rest.impl.TestDirectoriesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DirectoriesResourceTest {

    @Mock
    private DirectoriesService directoriesService;

    private TestDirectoriesResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestDirectoriesResourceImpl(directoriesService);
    }

    @Test
    void testGetDirectories() {
        final DirectoryCrowdBean initialDirectoryBean = DirectoryCrowdBean.EXAMPLE_1;
        final List<AbstractDirectoryBean> directoryBeans = Collections.singletonList(initialDirectoryBean);
        doReturn(directoryBeans).when(directoriesService).getDirectories();

        final Response response = resource.getDirectories();
        assertEquals(200, response.getStatus());

        final List<AbstractDirectoryBean> responseDirectoryBeans = (List<AbstractDirectoryBean>) response.getEntity();
        assertEquals(initialDirectoryBean, responseDirectoryBeans.iterator().next());
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
    void testSetDirectories() {
        final DirectoryCrowdBean directoryBean1 = DirectoryCrowdBean.EXAMPLE_1;
        final DirectoryCrowdBean directoryBean2 = DirectoryCrowdBean.EXAMPLE_3;
        final List<AbstractDirectoryBean> directoryBeans = Arrays.asList(directoryBean1, directoryBean2);
        doReturn(directoryBeans).when(directoriesService).setDirectories(directoryBeans, false);

        final Response response = resource.setDirectories(Boolean.FALSE, directoryBeans);
        assertEquals(200, response.getStatus());

        final List<AbstractDirectoryBean> responseDirectoryBeans = (List<AbstractDirectoryBean>) response.getEntity();
        assertEquals(directoryBeans.size(), responseDirectoryBeans.size());
    }

    @Test
    void testSetDirectory() {
        DirectoryCrowdBean directoryBean = DirectoryCrowdBean.EXAMPLE_1;

        doReturn(directoryBean).when(directoriesService).setDirectory(1L, directoryBean, false);

        final Response response = resource.updateDirectory(1L, Boolean.FALSE, directoryBean);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryBean directoryBeanResponse = (AbstractDirectoryBean) response.getEntity();

        assertEquals(directoryBean, directoryBeanResponse);
    }

    @Test
    void testAddDirectory() {
        DirectoryCrowdBean bean = DirectoryCrowdBean.EXAMPLE_1;

        doReturn(bean).when(directoriesService).addDirectory(bean, false);

        final Response response = resource.createDirectory(Boolean.FALSE, bean);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryBean responseBean = (AbstractDirectoryBean) response.getEntity();

        assertEquals(bean.getName(), responseBean.getName());
    }

    @Test
    void testDeleteDirectories() {
        resource.deleteDirectories(true);
        assertTrue(true, "Delete Successful");
    }

    @Test
    void testDeleteDirectory() {
        resource.deleteDirectory(1L);
        assertTrue(true, "Delete Successful");
    }

}
