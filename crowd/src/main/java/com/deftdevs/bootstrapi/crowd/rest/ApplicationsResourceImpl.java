package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.commons.constants.ConfAPI;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.crowd.model.ApplicationBean;
import de.aservo.confapi.crowd.model.ApplicationsBean;
import de.aservo.confapi.crowd.rest.api.ApplicationsResource;
import de.aservo.confapi.crowd.service.api.ApplicationsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(ConfAPI.APPLICATIONS)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
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
            final ApplicationsBean applicationsBean) {
        return Response.ok(applicationsService.setApplications(applicationsBean)).build();
    }

    @Override
    public Response setApplication(
            final long id,
            final ApplicationBean applicationBean) {
        return Response.ok(applicationsService.setApplication(id, applicationBean)).build();
    }

    @Override
    public Response addApplication(
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
