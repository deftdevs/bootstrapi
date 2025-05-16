package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.rest.api.ApplicationLinkResource;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;

import javax.ws.rs.core.Response;
import java.util.UUID;

public abstract class AbstractApplicationLinkResourceImpl implements ApplicationLinkResource {

    private final ApplicationLinksService applicationLinksService;

    public AbstractApplicationLinkResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        this.applicationLinksService = applicationLinksService;
    }

    @Override
    public Response getApplicationLink(
            final UUID uuid) {

        final ApplicationLinkModel linkModel = applicationLinksService.getApplicationLink(uuid);
        return Response.ok(linkModel).build();
    }

    @Override
    public Response createApplicationLink(
            final boolean ignoreSetupErrors,
            final ApplicationLinkModel linkModel) {

        final ApplicationLinkModel addedApplicationLink = applicationLinksService.addApplicationLink(
                linkModel, ignoreSetupErrors);
        return Response.ok(addedApplicationLink).build();
    }

    @Override
    public Response updateApplicationLink(
            final UUID uuid,
            final boolean ignoreSetupErrors,
            final ApplicationLinkModel linkModel) {

        final ApplicationLinkModel updatedLinkModel = applicationLinksService.setApplicationLink(
                uuid, linkModel, ignoreSetupErrors);
        return Response.ok(updatedLinkModel).build();
    }

    @Override
    public Response deleteApplicationLink(
            final UUID uuid) {

        applicationLinksService.deleteApplicationLink(uuid);
        return Response.ok().build();
    }
}
