package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.CloudConnectorModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestCloudConnectorResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.CloudConnectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CloudConnectorResourceTest {

    @Mock
    private CloudConnectorService cloudConnectorService;

    private TestCloudConnectorResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestCloudConnectorResourceImpl(cloudConnectorService);
    }

    @Test
    void testGetCloudConnector() {
        doReturn(CloudConnectorModel.EXAMPLE_1).when(cloudConnectorService).getCloudConnector();

        assertEquals(CloudConnectorModel.EXAMPLE_1, resource.getCloudConnector());
    }

    @Test
    void testSetCloudConnector() {
        final CloudConnectorModel cloudConnectorModel = CloudConnectorModel.builder().credentials("credentials").build();
        doReturn(CloudConnectorModel.EXAMPLE_1).when(cloudConnectorService).setCloudConnector(cloudConnectorModel);

        assertEquals(CloudConnectorModel.EXAMPLE_1, resource.setCloudConnector(cloudConnectorModel));
    }

    @Test
    void testDeleteCloudConnector() {
        resource.deleteCloudConnector();

        verify(cloudConnectorService).deleteCloudConnector();
    }

}
