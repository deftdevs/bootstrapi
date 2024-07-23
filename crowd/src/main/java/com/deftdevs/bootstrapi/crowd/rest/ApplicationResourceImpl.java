package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.crowd.model.ApplicationBean;
import com.deftdevs.bootstrapi.crowd.rest.api.ApplicationResource;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Component
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
    public Response getApplication(
            final long id) {
        return Response.ok(applicationsService.getApplication(id)).build();
    }

    @Override
    public Response updateApplication(
            final long id,
            final ApplicationBean applicationBean) {
        return Response.ok(applicationsService.setApplication(id, applicationBean)).build();
    }

    @Override
    public Response createApplication(
            final ApplicationBean applicationBean) {
        return Response.ok(applicationsService.addApplication(applicationBean)).build();
    }

    @Override
    public Response deleteApplication(long id) {
        applicationsService.deleteApplication(id);
        return Response.ok().build();
    }

}
