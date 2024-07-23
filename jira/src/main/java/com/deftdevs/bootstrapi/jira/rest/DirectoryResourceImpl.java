package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoryResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.jira.filter.SysadminOnlyResourceFilter;
import com.sun.jersey.spi.container.ResourceFilters;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.DIRECTORY)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
public class DirectoryResourceImpl extends AbstractDirectoryResourceImpl {

    @Inject
    public DirectoryResourceImpl(DirectoriesService directoryService) {
        super(directoryService);
    }

}
