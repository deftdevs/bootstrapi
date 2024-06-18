package com.deftdevs.bootstrapi.jira.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.AUTHENTICATION)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl {

    @Inject
    public AuthenticationResourceImpl(AuthenticationService authenticationService) {
        super(authenticationService);
    }

}
