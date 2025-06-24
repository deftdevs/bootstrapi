package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoryResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import jakarta.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.DIRECTORY)
@SystemAdminOnly
public class DirectoryResourceImpl extends AbstractDirectoryResourceImpl {

    @Inject
    public DirectoryResourceImpl(
            final DirectoriesService directoryService) {

        super(directoryService);
    }

}
