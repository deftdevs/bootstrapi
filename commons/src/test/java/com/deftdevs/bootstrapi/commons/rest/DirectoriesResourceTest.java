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

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        final Map<String, AbstractDirectoryModel> directoryModels = Collections.singletonMap(initialDirectoryModel.getName(), initialDirectoryModel);
        doReturn(directoryModels).when(directoriesService).getDirectories();

        final Map<String, ? extends AbstractDirectoryModel> responseDirectoryModels = resource.getDirectories();
        assertEquals(initialDirectoryModel, responseDirectoryModels.values().iterator().next());
    }

    @Test
    void testSetDirectories() {
        final DirectoryCrowdModel directoryModel1 = DirectoryCrowdModel.EXAMPLE_1;
        final DirectoryCrowdModel directoryModel2 = DirectoryCrowdModel.EXAMPLE_3;
        final Map<String, ? extends AbstractDirectoryModel> directoryModels = Stream.of(directoryModel1, directoryModel2)
                        .collect(Collectors.toMap(AbstractDirectoryModel::getName, Function.identity()));
        doReturn(directoryModels).when(directoriesService).setDirectories(directoryModels);

        final Map<String, ? extends AbstractDirectoryModel> responseDirectoryModels = resource.setDirectories(directoryModels);
        assertEquals(directoryModels.size(), responseDirectoryModels.size());
    }

    @Test
    void testDeleteDirectories() {
        resource.deleteDirectories(true);
        assertTrue(true, "Delete Successful");
    }

}
