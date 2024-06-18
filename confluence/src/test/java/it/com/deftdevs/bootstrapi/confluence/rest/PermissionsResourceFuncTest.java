package it.com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.confluence.model.PermissionAnonymousAccessBean;
import it.com.deftdevs.bootstrapi.commons.rest.ResourceBuilder;
import org.apache.wink.client.ClientAuthenticationException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

class PermissionsResourceFuncTest {

    @Test
    void testGetAnonymousPermissions() {
        Resource permissionsResource = ResourceBuilder.builder(ConfAPI.PERMISSIONS + "/" + ConfAPI.PERMISSION_ANONYMOUS_ACCESS).build();

        ClientResponse clientResponse = permissionsResource.get();
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        PermissionAnonymousAccessBean accessBean = clientResponse.getEntity(PermissionAnonymousAccessBean.class);
        assertNotNull(accessBean);
    }

    @Test
    void testSetAnonymousPermissions() {
        Resource permissionsResource = ResourceBuilder.builder(ConfAPI.PERMISSIONS + "/" + ConfAPI.PERMISSION_ANONYMOUS_ACCESS).build();

        ClientResponse clientResponse = permissionsResource.put(getExampleBean());
        assertEquals(Response.Status.OK.getStatusCode(), clientResponse.getStatusCode());

        PermissionAnonymousAccessBean accessBean = clientResponse.getEntity(PermissionAnonymousAccessBean.class);
        assertEquals(getExampleBean(), accessBean);
    }

    @Test
    void testGetAnonymousPermissionsUnauthenticated() {
        Resource permissionsResource = ResourceBuilder.builder(ConfAPI.PERMISSIONS + "/" + ConfAPI.PERMISSION_ANONYMOUS_ACCESS)
                .username("wrong")
                .password("password")
                .build();

        assertThrows(ClientAuthenticationException.class, permissionsResource::get);
    }

    @Test
    void testSetAnonymousPermissionsUnauthenticated() {
        Resource permissionsResource = ResourceBuilder.builder(ConfAPI.PERMISSIONS + "/" + ConfAPI.PERMISSION_ANONYMOUS_ACCESS)
                .username("wrong")
                .password("password")
                .build();

        final PermissionAnonymousAccessBean permissionAnonymousAccessBean = getExampleBean();

        assertThrows(ClientAuthenticationException.class, () -> {
            permissionsResource.put(permissionAnonymousAccessBean);
        });
    }

    @Test
    @Disabled("cannot be executed because there is no default user with restricted access rights")
    void testGetAnonymousPermissionsUnauthorized() {
        Resource permissionsResource = ResourceBuilder.builder(ConfAPI.PERMISSIONS + "/" + ConfAPI.PERMISSION_ANONYMOUS_ACCESS)
                .username("user")
                .password("user")
                .build();
        permissionsResource.get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), permissionsResource.put(getExampleBean()).getStatusCode());
    }

    @Test
    @Disabled("cannot be executed because there is no default user with restricted access rights")
    void testSetAnonymousPermissionsUnauthorized() {
        Resource permissionsResource = ResourceBuilder.builder(ConfAPI.PERMISSIONS + "/" + ConfAPI.PERMISSION_ANONYMOUS_ACCESS)
                .username("user")
                .password("user")
                .build();
        permissionsResource.put(getExampleBean());
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), permissionsResource.put(getExampleBean()).getStatusCode());
    }

    protected PermissionAnonymousAccessBean getExampleBean() {
        return PermissionAnonymousAccessBean.EXAMPLE_1;
    }
}
