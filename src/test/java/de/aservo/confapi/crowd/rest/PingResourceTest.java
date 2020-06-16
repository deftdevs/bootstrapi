package de.aservo.confapi.crowd.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

@RunWith(MockitoJUnitRunner.class)
public class PingResourceTest {

    private PingResource pingResource;

    @Before
    public void setup() {
        pingResource = new PingResource();
    }

    @Test
    public void testPing() {
        final Response pingResponse = pingResource.get();
        Assert.assertEquals(PingResource.PONG, pingResponse.getEntity().toString());
    }

}
