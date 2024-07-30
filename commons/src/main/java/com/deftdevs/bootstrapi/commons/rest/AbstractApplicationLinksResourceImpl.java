package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
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
        final List<ApplicationLinkBean> applicationLinkBeans = applicationLinksService.getApplicationLinks();
        return Response.ok(applicationLinkBeans).build();
    }

    @Override
    public Response setApplicationLinks(
            final boolean ignoreSetupErrors,
            final List<ApplicationLinkBean> applicationLinkBeans) {

        final List<ApplicationLinkBean> updatedApplicationLinkBeans = applicationLinksService.setApplicationLinks(
                applicationLinkBeans, ignoreSetupErrors);
        return Response.ok(updatedApplicationLinkBeans).build();
    }

    @Override
    public Response deleteApplicationLinks(
            final boolean force) {

        applicationLinksService.deleteApplicationLinks(force);
        return Response.ok().build();
    }
}
