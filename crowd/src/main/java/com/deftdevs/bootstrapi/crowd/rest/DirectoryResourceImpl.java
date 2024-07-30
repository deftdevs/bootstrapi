package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoryResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Component
@SystemAdminOnly
@Path(BootstrAPI.DIRECTORY)
public class DirectoryResourceImpl extends AbstractDirectoryResourceImpl {

    @Inject
    public DirectoryResourceImpl(
            final DirectoriesService directoriesService) {

        super(directoriesService);
    }

}
