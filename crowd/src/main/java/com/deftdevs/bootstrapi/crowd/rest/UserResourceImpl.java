package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUserResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@SystemAdminOnly
@Path(BootstrAPI.USER)
public class UserResourceImpl extends AbstractUserResourceImpl {

    @Inject
    public UserResourceImpl(
            final UsersService usersService) {

        super(usersService);
    }

}
