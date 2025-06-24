package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUserResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.USER)
@SystemAdminOnly
public class UserResourceImpl extends AbstractUserResourceImpl {

    @Inject
    public UserResourceImpl(
            final UsersService userService) {

        super(userService);
    }

    // Completely inhering the implementation of AbstractUserResourceImpl

}
