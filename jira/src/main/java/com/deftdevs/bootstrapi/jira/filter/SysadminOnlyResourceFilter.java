package com.deftdevs.bootstrapi.jira.filter;

import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import com.deftdevs.bootstrapi.commons.exception.web.UnauthorizedException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

import javax.ws.rs.ext.Provider;

/**
 * The Sysadmin only resource filter.
 */
@Provider
public class SysadminOnlyResourceFilter implements ContainerRequestFilter, ResourceFilter {

    private final UserManager userManager;

    /**
     * Instantiates a new Sysadmin only resource filter.
     *
     * @param userManager           the user manager
     */
    public SysadminOnlyResourceFilter(
            final UserManager userManager) {

        this.userManager = userManager;
    }

    public ContainerRequest filter(
            final ContainerRequest containerRequest) {

        final UserProfile loggedInUser = userManager.getRemoteUser();

        if (loggedInUser == null || !userManager.isSystemAdmin(loggedInUser.getUserKey())) {
            throw new UnauthorizedException("Client must be authenticated as a system administrator to access this resource.");
        }

        return containerRequest;
    }

    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

}
