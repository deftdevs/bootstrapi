package it.com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import org.apache.wink.client.Resource;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractPingResourceFuncTest {

    @Test
    void testGetPing() {
        Resource pingResource = ResourceBuilder.builder(ConfAPI.PING).acceptMediaType(MediaType.TEXT_PLAIN).build();
        assertEquals(Response.Status.OK.getStatusCode(), pingResource.get().getStatusCode());
    }
}
