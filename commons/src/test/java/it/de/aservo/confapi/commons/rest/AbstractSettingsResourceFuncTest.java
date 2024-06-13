package it.de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.commons.model.SettingsBean;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractSettingsResourceFuncTest {

    @Test
    void testGetSettings() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS).build();
        ClientResponse clientResponse = settingsResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());
        assertNotNull(clientResponse.getEntity(SettingsBean.class).getTitle());
    }

    @Test
    void testSetSettings() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS).build();
        assertEquals(Response.Status.OK.getStatusCode(), settingsResource.put(getExampleBean()).getStatusCode());

        ClientResponse clientResponse = settingsResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());
        assertEquals(getExampleBean(), clientResponse.getEntity(SettingsBean.class));
    }

    @Test
    public void testGetSettingsUnauthenticated() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS)
                .username("wrong")
                .password("password")
                .build();

        assertThrows(ClientAuthenticationException.class, settingsResource::get);
    }

    @Test
    public void testSetSettingsUnauthenticated() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS)
                .username("wrong")
                .password("password")
                .build();


        assertThrows(ClientAuthenticationException.class, () -> {
            settingsResource.put(getExampleBean());
        });
    }

    @Test
    void testGetSettingsUnauthorized() {
        Resource settingsResource = ResourceBuilder.builder(ConfAPI.SETTINGS)
                .username("user")
                .password("user")
                .build();

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), settingsResource.get().getStatusCode());
    }

    @Test
    void testSetSettingsUnauthorized() {
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
