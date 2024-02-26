package de.aservo.confapi.confluence.filter;

import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.plugins.rest.common.security.AuthenticationRequiredException;
import com.atlassian.plugins.rest.common.security.AuthorisationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysAdminOnlyResourceFilterTest {

    private ConfluenceUserImpl user;

    @Mock
    private PermissionManager permissionManager;

    private SysAdminOnlyResourceFilter sysAdminOnlyResourceFilter;

    @BeforeEach
    public void setup() {
        user = new ConfluenceUserImpl("test", "test test", "test@test.de");
        sysAdminOnlyResourceFilter = new SysAdminOnlyResourceFilter(permissionManager);
    }

    @Test
    void testFilterDefaults() {
        assertNull(sysAdminOnlyResourceFilter.getResponseFilter());
        assertEquals(sysAdminOnlyResourceFilter, sysAdminOnlyResourceFilter.getRequestFilter());
    }

    @Test
    void testAdminAccessNoUser() {
        assertThrows(AuthenticationRequiredException.class, () -> {
            sysAdminOnlyResourceFilter.filter(null);
        });
    }

    @Test
    void testSysAdminAccess() {
        when(permissionManager.isSystemAdministrator(user)).thenReturn(Boolean.TRUE);

        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);

            assertNull(sysAdminOnlyResourceFilter.filter(null));
        }
    }

    @Test
    void testNonSysAdminAccess() {
        when(permissionManager.isSystemAdministrator(user)).thenReturn(Boolean.FALSE);

        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);

            assertThrows(AuthorisationException.class, () -> {
                sysAdminOnlyResourceFilter.filter(null);
            });
        }
    }
}
