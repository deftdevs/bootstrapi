package de.aservo.atlassian.crowd.confapi;

import com.atlassian.crowd.manager.permission.UserPermissionService;
import com.atlassian.crowd.model.permission.UserPermission;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Component
public class CrowdWebAuthenticationHelper {

    @ComponentImport
    private final UserManager userManager;

    @ComponentImport
    private final UserPermissionService userPermissionService;

    /**
     * Constructor.
     *
     * @param userManager           injected {@link UserManager}
     * @param userPermissionService injected {@link UserPermissionService}
    */
    @Autowired
    public CrowdWebAuthenticationHelper(
            final UserManager userManager,
            final UserPermissionService userPermissionService) {

        this.userManager = userManager;
        this.userPermissionService = userPermissionService;
    }

    private boolean isAuthenticated() {
        return userManager.getRemoteUser() != null;
    }

    private boolean isSysAdmin() {
        return userPermissionService.currentUserHasPermission(UserPermission.SYS_ADMIN);
    }

    public void mustBeSysAdmin() throws WebApplicationException {
        if (!isAuthenticated()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        if (!isSysAdmin()) {
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
    }

}
