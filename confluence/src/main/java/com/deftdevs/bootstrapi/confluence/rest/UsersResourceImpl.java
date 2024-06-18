package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUsersResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.USERS)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class UsersResourceImpl extends AbstractUsersResourceImpl {

    @Inject
    public UsersResourceImpl(UsersService userService) {
        super(userService);
    }

    // Completely inhering the implementation of AbstractUserResourceImpl

}
