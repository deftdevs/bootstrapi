package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.crowd.model.ApplicationBean;
import com.deftdevs.bootstrapi.crowd.rest.api.ApplicationsResource;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
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
    public Response getApplication(
            final long id) {
        return Response.ok(applicationsService.getApplication(id)).build();
    }

    @Override
    public Response setApplications(
            final List<ApplicationBean> applicationBeans) {
        return Response.ok(applicationsService.setApplications(applicationBeans)).build();
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
    public Response deleteApplications(boolean force) {
        applicationsService.deleteApplications(force);
        return Response.ok().build();
    }

    @Override
    public Response deleteApplication(long id) {
        applicationsService.deleteApplication(id);
        return Response.ok().build();
    }

}
