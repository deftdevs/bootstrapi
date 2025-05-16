package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.confluence.service.api.ConfluenceAuthenticationService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.AUTHENTICATION)
@SystemAdminOnly
@Component
public class AuthenticationResourceImpl extends AbstractAuthenticationResourceImpl<AbstractAuthenticationIdpBean, AuthenticationSsoBean, ConfluenceAuthenticationService> {

    @Inject
    public AuthenticationResourceImpl(
            final ConfluenceAuthenticationService authenticationService) {

        super(authenticationService);
    }

}
