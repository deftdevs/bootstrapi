package com.deftdevs.bootstrapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUsersResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.crowd.filter.SysadminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.USERS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class UsersResourceImpl extends AbstractUsersResourceImpl {

    @Inject
    public UsersResourceImpl(
            final UsersService usersService) {

        super(usersService);
    }

}
