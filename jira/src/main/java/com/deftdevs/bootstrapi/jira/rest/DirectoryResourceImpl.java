package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoryResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;

import javax.ws.rs.Path;

@Path(BootstrAPI.DIRECTORY)
@ResourceFilters(SysadminOnlyResourceFilter.class)
public class DirectoryResourceImpl extends AbstractDirectoryResourceImpl {

    public DirectoryResourceImpl(DirectoriesService directoryService) {
        super(directoryService);
    }

}
