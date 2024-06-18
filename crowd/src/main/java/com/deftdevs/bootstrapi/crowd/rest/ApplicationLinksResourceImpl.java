package com.deftdevs.bootstrapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractApplicationLinksResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.crowd.filter.SysadminOnlyResourceFilter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

@Named
@Path(ConfAPI.APPLICATION_LINKS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class ApplicationLinksResourceImpl extends AbstractApplicationLinksResourceImpl {

    @Inject
    public ApplicationLinksResourceImpl(ApplicationLinksService applicationLinkService) {
        super(applicationLinkService);
    }
}
