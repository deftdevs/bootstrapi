package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoriesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Component
@SystemAdminOnly
@Path(BootstrAPI.DIRECTORIES)
public class DirectoriesResourceImpl extends AbstractDirectoriesResourceImpl {

    @Inject
    public DirectoriesResourceImpl(
            final DirectoriesService directoriesService) {

        super(directoriesService);
    }

}
