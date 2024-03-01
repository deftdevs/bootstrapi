package de.aservo.confapi.confluence.service;

import com.atlassian.confluence.security.SpacePermission;
import com.atlassian.confluence.security.SpacePermissionManager;
import com.atlassian.confluence.security.service.AnonymousUserPermissionsService;
import de.aservo.confapi.confluence.model.PermissionAnonymousAccessBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.atlassian.confluence.security.SpacePermission.USE_CONFLUENCE_PERMISSION;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PermissionsServiceTest {

    @Mock
    private AnonymousUserPermissionsService anonymousUserPermissionsService;

    @Mock
    private SpacePermissionManager spacePermissionManager;

    private PermissionsServiceImpl permissionsService;

    @BeforeEach
    public void setup() {
        permissionsService = new PermissionsServiceImpl(anonymousUserPermissionsService, spacePermissionManager);
    }

    @Test
    void testGetAnonymousPermissions() {
        List<SpacePermission> globalPermissions = new ArrayList<>();
        globalPermissions.add(SpacePermission.createGroupSpacePermission(USE_CONFLUENCE_PERMISSION, null, null));

        doReturn(globalPermissions).when(spacePermissionManager).getGlobalPermissions();

        PermissionAnonymousAccessBean response = permissionsService.getPermissionAnonymousAccess();
        assertNotNull(response);
    }

    @Test
    void testSetAnonymousPermissions() {
        PermissionAnonymousAccessBean accessBean = new PermissionAnonymousAccessBean(true, true);
        PermissionAnonymousAccessBean response = permissionsService.setPermissionAnonymousAccess(accessBean);
        assertNotNull(response);
    }
}
