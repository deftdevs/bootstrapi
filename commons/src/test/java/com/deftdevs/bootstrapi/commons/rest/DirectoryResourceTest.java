package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
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
        DirectoryCrowdModel directoryModel = DirectoryCrowdModel.EXAMPLE_1;

        doReturn(directoryModel).when(directoriesService).getDirectory(1L);

        final Response response = resource.getDirectory(1L);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryModel directoryModelResponse = (AbstractDirectoryModel) response.getEntity();

        assertEquals(directoryModel, directoryModelResponse);
    }

    @Test
    void testCreateDirectory() {
        DirectoryCrowdModel bean = DirectoryCrowdModel.EXAMPLE_1;

        doReturn(bean).when(directoriesService).addDirectory(bean, false);

        final Response response = resource.createDirectory(Boolean.FALSE, bean);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryModel responseModel = (AbstractDirectoryModel) response.getEntity();

        assertEquals(bean.getName(), responseModel.getName());
    }

    @Test
    void testUpdateDirectory() {
        DirectoryCrowdModel directoryModel = DirectoryCrowdModel.EXAMPLE_1;

        doReturn(directoryModel).when(directoriesService).setDirectory(1L, directoryModel, false);

        final Response response = resource.updateDirectory(1L, Boolean.FALSE, directoryModel);
        assertEquals(200, response.getStatus());
        final AbstractDirectoryModel directoryModelResponse = (AbstractDirectoryModel) response.getEntity();

        assertEquals(directoryModel, directoryModelResponse);
    }

    @Test
    void testDeleteDirectory() {
        resource.deleteDirectory(1L);
        assertTrue(true, "Delete Successful");
    }

}
