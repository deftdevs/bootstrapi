package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryBean;
import com.deftdevs.bootstrapi.commons.model.DirectoryInternalBean;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class DirectoriesResourceTest {

    @Mock
    private DirectoriesService directoriesService;

    private DirectoriesResourceImpl directoriesResource;

    @BeforeEach
    public void setup() {
        directoriesResource = new DirectoriesResourceImpl(directoriesService);
    }

    @Test
    public void testGetDirectories() {
        final List<AbstractDirectoryBean> directoriesBean = Collections.singletonList(DirectoryInternalBean.EXAMPLE_1);
        doReturn(directoriesBean).when(directoriesService).getDirectories();

        final Response response = directoriesResource.getDirectories();
        assertEquals(200, response.getStatus());

        final List<AbstractDirectoryBean> responseDirectoryBean = (List<AbstractDirectoryBean>) response.getEntity();
        assertEquals(directoriesBean, responseDirectoryBean);
    }

    @Test
    public void testGetDirectory() {
        final DirectoryInternalBean directoryBean = DirectoryInternalBean.EXAMPLE_1;
        doReturn(directoryBean).when(directoriesService).getDirectory(anyLong());

        final Response response = directoriesResource.getDirectory(directoryBean.getId());
        assertEquals(200, response.getStatus());

        final DirectoryInternalBean responseDirectoryBean = (DirectoryInternalBean) response.getEntity();
        assertEquals(directoryBean, responseDirectoryBean);
    }

}
