package com.deftdevs.bootstrapi.confluence.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import com.deftdevs.bootstrapi.commons.constants.ConfAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoriesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.deftdevs.bootstrapi.confluence.filter.SysAdminOnlyResourceFilter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(ConfAPI.DIRECTORIES)
@ResourceFilters(SysAdminOnlyResourceFilter.class)
@Component
public class DirectoriesResourceImpl extends AbstractDirectoriesResourceImpl {

    @Inject
    public DirectoriesResourceImpl(DirectoriesService directoryService) {
        super(directoryService);
    }

    // Completely inhering the implementation of AbstractDirectoriesResourceImpl

}
