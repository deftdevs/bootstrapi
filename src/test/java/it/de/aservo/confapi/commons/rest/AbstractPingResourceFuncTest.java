package it.de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.constants.ConfAPI;
import org.apache.wink.client.Resource;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public abstract class AbstractPingResourceFuncTest {

    @Test
    public void testGetPing() {
        Resource pingResource = ResourceBuilder.builder(ConfAPI.PING).acceptMediaType(MediaType.TEXT_PLAIN).build();
        assertEquals(Response.Status.OK.getStatusCode(), pingResource.get().getStatusCode());
    }
}
