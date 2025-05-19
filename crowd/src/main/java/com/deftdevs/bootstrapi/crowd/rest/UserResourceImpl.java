package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUserResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;

import javax.ws.rs.Path;

@SystemAdminOnly
@Path(BootstrAPI.USER)
public class UserResourceImpl extends AbstractUserResourceImpl {

    public UserResourceImpl(
            final UsersService usersService) {

        super(usersService);
    }

}
