package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.jira.service.api.JiraAuthenticationService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.AUTHENTICATION)
@SystemAdminOnly
@Component
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl<AbstractAuthenticationIdpBean, AuthenticationSsoBean, JiraAuthenticationService> {

    @Inject
    public AuthenticationResourceImpl(JiraAuthenticationService authenticationService) {
        super(authenticationService);
    }

}
