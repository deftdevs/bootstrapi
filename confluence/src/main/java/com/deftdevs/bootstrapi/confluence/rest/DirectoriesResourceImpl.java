package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoriesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import javax.ws.rs.Path;

@Path(BootstrAPI.DIRECTORIES)
@SystemAdminOnly
public class DirectoriesResourceImpl extends AbstractDirectoriesResourceImpl {

    public DirectoriesResourceImpl(
            final DirectoriesService directoryService) {

        super(directoryService);
    }

    // Completely inhering the implementation of AbstractDirectoriesResourceImpl

}
