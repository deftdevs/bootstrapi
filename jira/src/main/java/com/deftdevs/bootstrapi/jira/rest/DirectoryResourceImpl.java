package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractDirectoryResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.DirectoriesService;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path(BootstrAPI.DIRECTORY)
@SystemAdminOnly
@Component
public class DirectoryResourceImpl extends AbstractDirectoryResourceImpl {

    public DirectoryResourceImpl(DirectoriesService directoryService) {
        super(directoryService);
    }

}
