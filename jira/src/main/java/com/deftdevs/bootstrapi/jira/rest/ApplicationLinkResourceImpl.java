package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractApplicationLinkResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;

import javax.ws.rs.Path;

@Path(BootstrAPI.APPLICATION_LINK)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class ApplicationLinkResourceImpl extends AbstractApplicationLinkResourceImpl {

    public ApplicationLinkResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        super(applicationLinksService);
    }

}
