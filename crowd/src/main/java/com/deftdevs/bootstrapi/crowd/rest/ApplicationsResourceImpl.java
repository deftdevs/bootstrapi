package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;
import com.deftdevs.bootstrapi.crowd.rest.api.ApplicationsResource;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;

import jakarta.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@SystemAdminOnly
@Path(BootstrAPI.APPLICATIONS)
public class ApplicationsResourceImpl implements ApplicationsResource {

    private final ApplicationsService applicationsService;

    @Inject
    public ApplicationsResourceImpl(
            final ApplicationsService applicationsService) {

        this.applicationsService = applicationsService;
    }

    @Override
    public Response getApplications() {
        return Response.ok(applicationsService.getApplications()).build();
    }

    @Override
    public Response setApplications(
            final List<ApplicationModel> applicationModels) {
        return Response.ok(applicationsService.setApplications(applicationModels)).build();
    }

    @Override
    public Response deleteApplications(boolean force) {
        applicationsService.deleteApplications(force);
        return Response.ok().build();
    }

}
