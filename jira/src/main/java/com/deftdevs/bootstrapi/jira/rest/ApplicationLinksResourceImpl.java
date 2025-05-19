package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractApplicationLinksResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;

import javax.ws.rs.Path;

@Path(BootstrAPI.APPLICATION_LINKS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class ApplicationLinksResourceImpl extends AbstractApplicationLinksResourceImpl {

    public ApplicationLinksResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        super(applicationLinksService);
    }

}
