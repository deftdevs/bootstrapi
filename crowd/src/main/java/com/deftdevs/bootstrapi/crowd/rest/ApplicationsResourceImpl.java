package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.crowd.model.ApplicationModel;
import com.deftdevs.bootstrapi.crowd.rest.api.ApplicationsResource;
import com.deftdevs.bootstrapi.crowd.service.api.ApplicationsService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

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
    public Map<String, ApplicationModel> getApplications() {
        return applicationsService.getApplications();
    }

    @Override
    public Map<String, ApplicationModel> setApplications(
            final Map<String, ApplicationModel> applicationModels) {

        return applicationsService.setApplications(applicationModels);
    }

    @Override
    public void deleteApplications(
            final boolean force) {

        applicationsService.deleteApplications(force);
    }

}
