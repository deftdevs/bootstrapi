package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.jira.service.api.JiraAuthenticationService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.AUTHENTICATION)
@SystemAdminOnly
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl<AbstractAuthenticationIdpModel, AuthenticationSsoModel, JiraAuthenticationService> {

    @Inject
    public AuthenticationResourceImpl(
            final JiraAuthenticationService authenticationService) {

        super(authenticationService);
    }

}
