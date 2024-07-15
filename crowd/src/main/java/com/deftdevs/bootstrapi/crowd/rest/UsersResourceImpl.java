package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUsersResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Component
@SystemAdminOnly
@Path(BootstrAPI.USERS)
public class UsersResourceImpl extends AbstractUsersResourceImpl {

    @Inject
    public UsersResourceImpl(
            final UsersService usersService) {

        super(usersService);
    }

}
