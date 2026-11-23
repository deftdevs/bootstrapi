package com.deftdevs.bootstrapi.commons.model;

import com.deftdevs.bootstrapi.commons.junit.AbstractModelTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class CloudConnectorModelTest extends AbstractModelTest {

    @Test
    void testToStringDoesNotContainCredentials() {
        final CloudConnectorModel cloudConnectorModel = CloudConnectorModel.builder()
                .credentials("secret-credentials")
                .build();

        assertFalse(cloudConnectorModel.toString().contains("secret-credentials"));
    }

}
