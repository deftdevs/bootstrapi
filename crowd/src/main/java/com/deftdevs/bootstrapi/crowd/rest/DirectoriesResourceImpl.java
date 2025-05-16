package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoriesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;

import javax.ws.rs.Path;

@SystemAdminOnly
@Path(BootstrAPI.DIRECTORIES)
public class DirectoriesResourceImpl extends AbstractDirectoriesResourceImpl {

    public DirectoriesResourceImpl(
            final DirectoriesService directoriesService) {

        super(directoriesService);
    }

}
