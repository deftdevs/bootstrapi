package com.deftdevs.bootstrapi.crowd.filter;

import com.atlassian.crowd.manager.permission.UserPermissionService;
import com.atlassian.plugins.rest.common.security.AuthenticationRequiredException;
import com.atlassian.plugins.rest.common.security.AuthorisationException;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SysadminOnlyResourceFilterTest {

    @Mock
    private UserManager userManager;

    @Mock
    private UserPermissionService userPermissionService;

    private SysadminOnlyResourceFilter sysadminOnlyResourceFilter;

    @BeforeEach
    public void setup() {
        sysadminOnlyResourceFilter = new SysadminOnlyResourceFilter(userManager, userPermissionService);
    }

    @Test
    public void testFilterDefaults() {
        assertNull(sysadminOnlyResourceFilter.getResponseFilter());
        assertEquals(sysadminOnlyResourceFilter, sysadminOnlyResourceFilter.getRequestFilter());
    }

    @Test
    public void testAdminAccessNoUser() {
        assertThrows(AuthenticationRequiredException.class, () -> {
            sysadminOnlyResourceFilter.filter(null);
        });
    }

    @Test
    public void testNonSysadminAccess() {
        final UserProfile userProfile = mock(UserProfile.class);
        doReturn(userProfile).when(userManager).getRemoteUser();
        doReturn(false).when(userPermissionService).hasPermission(any(), any());

        assertThrows(AuthorisationException.class, () -> {
            sysadminOnlyResourceFilter.filter(null);
        });
    }

    @Test
    public void testSysadminAccess() {
        final UserProfile userProfile = mock(UserProfile.class);
        doReturn(userProfile).when(userManager).getRemoteUser();
        doReturn(true).when(userPermissionService).hasPermission(any(), any());

        assertNull(sysadminOnlyResourceFilter.filter(null));
    }

}
