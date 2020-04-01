package de.aservo.atlassian.crowd.confapi.helper;

import com.atlassian.crowd.manager.permission.UserPermissionService;
import com.atlassian.crowd.model.permission.UserPermission;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import de.aservo.atlassian.crowd.confapi.helper.CrowdWebAuthenticationHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.WebApplicationException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrowdWebAuthenticationHelperTest {

    @Mock
    private UserManager userManager;

    @Mock
    private UserPermissionService userPermissionService;

    @InjectMocks
    private CrowdWebAuthenticationHelper webAuthenticationHelper;

    @Mock
    private UserProfile user;

    @Test
    public void testMustBeAdminAsSysAdmin() {
        when(userManager.getRemoteUser()).thenReturn(user);
        when(userPermissionService.currentUserHasPermission(UserPermission.SYS_ADMIN)).thenReturn(true);
        // no exception may be thrown
        webAuthenticationHelper.mustBeSysAdmin();
    }

    @Test(expected = WebApplicationException.class)
    public void testMustBeAdminAsNonAdmin() {
        when(userManager.getRemoteUser()).thenReturn(user);
        webAuthenticationHelper.mustBeSysAdmin();
    }

    @Test(expected = WebApplicationException.class)
    public void testMustBeAdminWithNullUser() {
        when(userManager.getRemoteUser()).thenReturn(null);
        webAuthenticationHelper.mustBeSysAdmin();
    }

}
