package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;
import com.deftdevs.bootstrapi.crowd.rest.api.ApplicationResource;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@SystemAdminOnly
@Path(BootstrAPI.APPLICATION)
public class ApplicationResourceImpl implements ApplicationResource {

    private final ApplicationsService applicationsService;

    @Inject
    public ApplicationResourceImpl(
            final ApplicationsService applicationsService) {

        this.applicationsService = applicationsService;
    }

    @Override
    public ApplicationModel getApplication(
            final long id) {

        return applicationsService.getApplication(id);
    }

    @Override
    public ApplicationModel updateApplication(
            final long id,
            final ApplicationModel applicationModel) {

        return applicationsService.setApplication(id, applicationModel);
    }

    @Override
    public ApplicationModel createApplication(
            final ApplicationModel applicationModel) {

        return applicationsService.addApplication(applicationModel);
    }

    @Override
    public void deleteApplication(
            final long id) {

        applicationsService.deleteApplication(id);
    }

}
