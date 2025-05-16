package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractDirectoryModel;
import com.deftdevs.bootstrapi.commons.model.DirectoryCrowdModel;
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
        final DirectoryCrowdModel initialDirectoryModel = DirectoryCrowdModel.EXAMPLE_1;
        final List<AbstractDirectoryModel> directoryModels = Collections.singletonList(initialDirectoryModel);
        doReturn(directoryModels).when(directoriesService).getDirectories();

        final Response response = resource.getDirectories();
        assertEquals(200, response.getStatus());

        final List<AbstractDirectoryModel> responseDirectoryModels = (List<AbstractDirectoryModel>) response.getEntity();
        assertEquals(initialDirectoryModel, responseDirectoryModels.iterator().next());
    }

    @Test
    void testSetDirectories() {
        final DirectoryCrowdModel directoryModel1 = DirectoryCrowdModel.EXAMPLE_1;
        final DirectoryCrowdModel directoryModel2 = DirectoryCrowdModel.EXAMPLE_3;
        final List<AbstractDirectoryModel> directoryModels = Arrays.asList(directoryModel1, directoryModel2);
        doReturn(directoryModels).when(directoriesService).setDirectories(directoryModels, false);

        final Response response = resource.setDirectories(Boolean.FALSE, directoryModels);
        assertEquals(200, response.getStatus());

        final List<AbstractDirectoryModel> responseDirectoryModels = (List<AbstractDirectoryModel>) response.getEntity();
        assertEquals(directoryModels.size(), responseDirectoryModels.size());
    }

    @Test
    void testDeleteDirectories() {
        resource.deleteDirectories(true);
        assertTrue(true, "Delete Successful");
    }

}
