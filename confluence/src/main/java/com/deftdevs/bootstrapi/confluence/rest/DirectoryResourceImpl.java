package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoryResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.DIRECTORY)
@SystemAdminOnly
public class DirectoryResourceImpl extends AbstractDirectoryResourceImpl {

    @Inject
    public DirectoryResourceImpl(
            final DirectoriesService directoryService) {

        super(directoryService);
    }

    // Completely inhering the implementation of AbstractDirectoriesResourceImpl

}
