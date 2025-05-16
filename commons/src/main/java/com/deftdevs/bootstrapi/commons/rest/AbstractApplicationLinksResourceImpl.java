package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.rest.api.ApplicationLinksResource;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;

import javax.ws.rs.core.Response;
import java.util.List;

public abstract class AbstractApplicationLinksResourceImpl implements ApplicationLinksResource {

    private final ApplicationLinksService applicationLinksService;

    public AbstractApplicationLinksResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        this.applicationLinksService = applicationLinksService;
    }

    @Override
    public Response getApplicationLinks() {
        final List<ApplicationLinkModel> applicationLinkModels = applicationLinksService.getApplicationLinks();
        return Response.ok(applicationLinkModels).build();
    }

    @Override
    public Response setApplicationLinks(
            final boolean ignoreSetupErrors,
            final List<ApplicationLinkModel> applicationLinkModels) {

        final List<ApplicationLinkModel> updatedApplicationLinkModels = applicationLinksService.setApplicationLinks(
                applicationLinkModels, ignoreSetupErrors);
        return Response.ok(updatedApplicationLinkModels).build();
    }

    @Override
    public Response deleteApplicationLinks(
            final boolean force) {

        applicationLinksService.deleteApplicationLinks(force);
        return Response.ok().build();
    }
}
