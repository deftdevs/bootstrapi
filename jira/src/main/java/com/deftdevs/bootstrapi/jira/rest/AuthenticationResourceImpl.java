package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.jira.service.api.JiraAuthenticationService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path(BootstrAPI.AUTHENTICATION)
@SystemAdminOnly
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl {

    @Inject
    public AuthenticationResourceImpl(
            final JiraAuthenticationService authenticationService) {

        super(authenticationService);
    }

}
