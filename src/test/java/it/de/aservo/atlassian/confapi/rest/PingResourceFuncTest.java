package it.de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.constants.ConfAPI;
import org.apache.wink.client.Resource;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public abstract class PingResourceFuncTest {

    @Test
    public void testGetPing() {
        Resource pingResource = ResourceBuilder.builder(ConfAPI.PING).build();
        assertEquals(Response.Status.OK.getStatusCode(), pingResource.get().getStatusCode());
    }
}
