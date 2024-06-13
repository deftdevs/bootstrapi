package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.rest.api.PingResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PingResourceTest {

    private AbstractPingResourceImpl pingResource;

    @BeforeEach
    public void setup() {
        // we can use mock with CALLS_REAL_METHODS here because PingResource uses no services
        pingResource = mock(AbstractPingResourceImpl.class, CALLS_REAL_METHODS);
    }

    @Test
    void testGetPing() {
        final Response pingResponse = pingResource.getPing();
        assertEquals(PingResource.PONG, pingResponse.getEntity().toString());
    }

}
