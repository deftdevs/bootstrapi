package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.AUTHENTICATION)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl {

    @Inject
    public AuthenticationResourceImpl(AuthenticationService authenticationService) {
        super(authenticationService);
    }

}
