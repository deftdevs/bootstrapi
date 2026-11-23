package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.junit.AbstractModelTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class CloudTunnelModelTest extends AbstractModelTest {

    private static final String KEY = "secret-key";

    @Override
    protected String getExpectedXmlRootElementName() {
        return BootstrAPI.CLOUD_TUNNEL;
    }

    @Test
    void testToStringDoesNotContainKey() {
        final CloudTunnelModel cloudTunnelModel = CloudTunnelModel.builder()
                .key(KEY)
                .build();

        assertFalse(cloudTunnelModel.toString().contains(KEY));
    }

}
