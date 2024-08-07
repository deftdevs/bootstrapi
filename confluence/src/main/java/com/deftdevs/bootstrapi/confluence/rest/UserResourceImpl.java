package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUserResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UsersService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.USER)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class UserResourceImpl extends AbstractUserResourceImpl {

    @Inject
    public UserResourceImpl(UsersService userService) {
        super(userService);
    }

    // Completely inhering the implementation of AbstractUserResourceImpl

}
