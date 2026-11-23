package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestCloudTunnelResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.CloudTunnelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CloudTunnelResourceTest {

    @Mock
    private CloudTunnelService cloudTunnelService;

    private TestCloudTunnelResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestCloudTunnelResourceImpl(cloudTunnelService);
    }

    @Test
    void testGetCloudTunnel() {
        doReturn(CloudTunnelModel.EXAMPLE_1).when(cloudTunnelService).getCloudTunnel();

        assertEquals(CloudTunnelModel.EXAMPLE_1, resource.getCloudTunnel());
    }

    @Test
    void testSetCloudTunnel() {
        final CloudTunnelModel cloudTunnelModel = CloudTunnelModel.builder().key("key").build();
        doReturn(CloudTunnelModel.EXAMPLE_1).when(cloudTunnelService).setCloudTunnel(cloudTunnelModel);

        assertEquals(CloudTunnelModel.EXAMPLE_1, resource.setCloudTunnel(cloudTunnelModel));
    }

    @Test
    void testDeleteCloudTunnel() {
        resource.deleteCloudTunnel();

        verify(cloudTunnelService).deleteCloudTunnel();
    }

}
