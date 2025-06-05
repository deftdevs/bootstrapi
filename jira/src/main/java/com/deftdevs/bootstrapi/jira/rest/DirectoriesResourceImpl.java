package com.deftdevs.bootstrapi.jira.rest;

import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoriesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.DIRECTORIES)
@SystemAdminOnly
public class DirectoriesResourceImpl extends AbstractDirectoriesResourceImpl {

    @Inject
    public DirectoriesResourceImpl(
            final DirectoriesService directoryService) {

        super(directoryService);
    }

}
