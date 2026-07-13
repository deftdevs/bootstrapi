package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceAuthenticationService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path(BootstrAPI.AUTHENTICATION)
@SystemAdminOnly
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl {

    @Inject
    public AuthenticationResourceImpl(
            final ConfluenceAuthenticationService authenticationService) {

        super(authenticationService);
    }

}
