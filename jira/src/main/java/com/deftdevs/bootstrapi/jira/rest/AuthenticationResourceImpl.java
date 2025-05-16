package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.deftdevs.bootstrapi.jira.service.api.JiraAuthenticationService;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.AUTHENTICATION)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl<AbstractAuthenticationIdpModel, AuthenticationSsoModel, JiraAuthenticationService> {

    @Inject
    public AuthenticationResourceImpl(JiraAuthenticationService authenticationService) {
        super(authenticationService);
    }

}
