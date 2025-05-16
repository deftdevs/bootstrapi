package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUserResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;

import javax.ws.rs.Path;

@Path(BootstrAPI.USER)
@SystemAdminOnly
public class UserResourceImpl extends AbstractUserResourceImpl {

    public UserResourceImpl(
            final UsersService userService) {

        super(userService);
    }

    // Completely inhering the implementation of AbstractUserResourceImpl

}
