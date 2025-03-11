package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractApplicationLinksResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.APPLICATION_LINKS)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class ApplicationLinksResourceImpl extends AbstractApplicationLinksResourceImpl {

    @Inject
    public ApplicationLinksResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        super(applicationLinksService);
    }
}
