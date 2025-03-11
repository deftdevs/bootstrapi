package com.deftdevs.bootstrapi.confluence.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractApplicationLinkResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.APPLICATION_LINK)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class ApplicationLinkResourceImpl extends AbstractApplicationLinkResourceImpl {

    @Inject
    public ApplicationLinkResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        super(applicationLinksService);
    }
}
