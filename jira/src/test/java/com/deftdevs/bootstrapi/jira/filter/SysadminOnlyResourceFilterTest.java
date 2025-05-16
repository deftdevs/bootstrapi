package com.deftdevs.bootstrapi.jira.filter;

import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.deftdevs.bootstrapi.commons.exception.web.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SysadminOnlyResourceFilterTest {

    @Mock
    private UserManager userManager;

    private SysadminOnlyResourceFilter sysadminOnlyResourceFilter;

    @BeforeEach
    public void setup() {
        sysadminOnlyResourceFilter = new SysadminOnlyResourceFilter(userManager);
    }

    @Test
    void testFilterDefaults() {
        assertNull(sysadminOnlyResourceFilter.getResponseFilter());
        assertEquals(sysadminOnlyResourceFilter, sysadminOnlyResourceFilter.getRequestFilter());
    }

    @Test
    void testAdminAccessNoUser() {
        assertThrows(UnauthorizedException.class, () -> {
            sysadminOnlyResourceFilter.filter(null);
        });
    }

    @Test
    void testNonSysadminAccess() {
        final UserProfile userProfile = mock(UserProfile.class);
        doReturn(userProfile).when(userManager).getRemoteUser();

        assertThrows(UnauthorizedException.class, () -> {
            sysadminOnlyResourceFilter.filter(null);
        });
    }

    @Test
    void testSysadminAccess() {
        final UserProfile userProfile = mock(UserProfile.class);
        doReturn(new UserKey("user")).when(userProfile).getUserKey();
        doReturn(userProfile).when(userManager).getRemoteUser();
        doReturn(true).when(userManager).isSystemAdmin(any(UserKey.class));

        assertNull(sysadminOnlyResourceFilter.filter(null));
    }

}
