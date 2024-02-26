package de.aservo.confapi.confluence.filter;

import com.atlassian.confluence.security.PermissionManager;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUserImpl;
import com.atlassian.plugins.rest.common.security.AuthenticationRequiredException;
import com.atlassian.plugins.rest.common.security.AuthorisationException;
import com.sun.jersey.spi.container.ContainerRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SysAdminOnlyResourceFilterTest {

    private ConfluenceUserImpl user;
    private PermissionManager permissionManager;
    private SysAdminOnlyResourceFilter filter;

    @Before
    public void setup() {
        user  = new ConfluenceUserImpl("test", "test test", "test@test.de");
        permissionManager = mock(PermissionManager.class);
        filter = new SysAdminOnlyResourceFilter(permissionManager);
    }

    @Test
    public void testFilterDefaults() {
        assertNull(filter.getResponseFilter());
        assertEquals(filter, filter.getRequestFilter());
    }

    @Test(expected = AuthenticationRequiredException.class)
    public void testAdminAccessNoUser() {
        filter.filter(null);
    }

    @Test
    public void testSysAdminAccess() {
        when(permissionManager.isSystemAdministrator(user)).thenReturn(Boolean.TRUE);

        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);

            ContainerRequest filterResponse = filter.filter(null);
            assertNull(filterResponse);
        }
    }

    @Test(expected = AuthorisationException.class)
    public void testNonSysAdminAccess() {
        when(permissionManager.isSystemAdministrator(user)).thenReturn(Boolean.FALSE);

        try (MockedStatic<AuthenticatedUserThreadLocal> authenticatedUserThreadLocalMockedStatic = mockStatic(AuthenticatedUserThreadLocal.class)) {
            authenticatedUserThreadLocalMockedStatic.when(AuthenticatedUserThreadLocal::get).thenReturn(user);

            filter.filter(any(ContainerRequest.class));
        }
    }
}
