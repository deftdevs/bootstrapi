package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUserResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Component
@SystemAdminOnly
@Path(BootstrAPI.USER)
public class UserResourceImpl extends AbstractUserResourceImpl {

    @Inject
    public UserResourceImpl(
            final UsersService usersService) {

        super(usersService);
    }

}
