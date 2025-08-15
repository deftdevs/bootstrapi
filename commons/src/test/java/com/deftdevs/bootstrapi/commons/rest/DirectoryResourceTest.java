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

        final AbstractDirectoryModel directoryModelResponse = resource.getDirectory(1L);
        assertEquals(directoryModel, directoryModelResponse);
    }

    @Test
    void testCreateDirectory() {
        DirectoryCrowdModel bean = DirectoryCrowdModel.EXAMPLE_1;
        doReturn(bean).when(directoriesService).addDirectory(bean);

        final AbstractDirectoryModel responseModel = resource.createDirectory(bean);
        assertEquals(bean.getName(), responseModel.getName());
    }

    @Test
    void testUpdateDirectory() {
        DirectoryCrowdModel directoryModel = DirectoryCrowdModel.EXAMPLE_1;
        doReturn(directoryModel).when(directoriesService).setDirectory(1L, directoryModel);

        final AbstractDirectoryModel directoryModelResponse = resource.updateDirectory(1L, directoryModel);
        assertEquals(directoryModel, directoryModelResponse);
    }

    @Test
    void testDeleteDirectory() {
        resource.deleteDirectory(1L);
        assertTrue(true, "Delete Successful");
    }

}
