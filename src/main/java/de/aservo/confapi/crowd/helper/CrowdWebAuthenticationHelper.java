package de.aservo.confapi.crowd.helper;

import com.atlassian.crowd.manager.permission.UserPermissionService;
import com.atlassian.crowd.model.permission.UserPermission;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import de.aservo.confapi.commons.helper.WebAuthenticationHelper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CrowdWebAuthenticationHelper implements WebAuthenticationHelper<UserProfile> {

    @ComponentImport
    private final UserManager userManager;

    @ComponentImport
    private final UserPermissionService userPermissionService;

    @Inject
    public CrowdWebAuthenticationHelper(
            final UserManager userManager,
            final UserPermissionService userPermissionService) {

        this.userManager = userManager;
        this.userPermissionService = userPermissionService;
    }

    @Override
    public UserProfile getAuthenticatedUser() {
        return userManager.getRemoteUser();
    }

    @Override
    public boolean isSystemAdministrator(UserProfile userProfile) {
        return userPermissionService.currentUserHasPermission(UserPermission.SYS_ADMIN);
    }

}
