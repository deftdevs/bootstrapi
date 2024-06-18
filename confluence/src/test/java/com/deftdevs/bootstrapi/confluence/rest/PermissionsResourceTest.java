package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.confluence.model.PermissionAnonymousAccessBean;
import com.deftdevs.bootstrapi.confluence.service.api.PermissionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PermissionsResourceTest {

    private PermissionsResourceImpl resource;

    @Mock
    private PermissionsService permissionsService;

    @BeforeEach
    public void setup() {
        resource = new PermissionsResourceImpl(permissionsService);
    }

    @Test
    void testGetAnonymousPermissions() {
        doReturn(PermissionAnonymousAccessBean.EXAMPLE_1).when(permissionsService).getPermissionAnonymousAccess();

        Response response = resource.getPermissionAnonymousAccess();
        assertEquals(200, response.getStatus());
        assertEquals(PermissionAnonymousAccessBean.class, response.getEntity().getClass());

        PermissionAnonymousAccessBean accessBean = (PermissionAnonymousAccessBean)response.getEntity();
        assertEquals(PermissionAnonymousAccessBean.EXAMPLE_1, accessBean);
    }

    @Test
    void testSetAnonymousPermissions() {
        Response response = resource.setPermissionAnonymousAccess(PermissionAnonymousAccessBean.EXAMPLE_1);
        assertEquals(200, response.getStatus());
    }
}
