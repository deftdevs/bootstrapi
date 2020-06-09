package it.de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.SettingsBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class AbstractSettingsResourceFuncTest {

    @Test
    public void testGetSettings() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS).build();
        ClientResponse clientResponse = settingsResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());
        assertNotNull(clientResponse.getEntity(SettingsBean.class).getTitle());
    }

    @Test
    public void testSetSettings() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS).build();
        assertEquals(Response.Status.OK.getStatusCode(), settingsResource.put(getExampleBean()).getStatusCode());

        ClientResponse clientResponse = settingsResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());
        assertEquals(getExampleBean(), clientResponse.getEntity(SettingsBean.class));
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testGetSettingsUnauthenticated() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS)
                .username("wrong")
                .password("password")
                .build();
        settingsResource.get();
    }

    @Test(expected = ClientAuthenticationException.class)
    public void testSetSettingsUnauthenticated() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS)
                .username("wrong")
                .password("password")
                .build();
        settingsResource.put(getExampleBean());
    }

    @Test
    public void testGetSettingsUnauthorized() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS)
                .username("user")
                .password("user")
                .build();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), settingsResource.get().getStatusCode());
    }

    @Test
    public void testSetSettingsUnauthorized() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS)
                .username("user")
                .password("user")
                .build();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), settingsResource.put(getExampleBean()).getStatusCode());
    }

    protected SettingsBean getExampleBean() {
        return SettingsBean.EXAMPLE_1;
    }
}
