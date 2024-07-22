package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
import com.deftdevs.bootstrapi.commons.rest.api.ApplicationLinksResource;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;

import javax.ws.rs.core.Response;
import java.util.*;

public abstract class AbstractApplicationLinksResourceImpl implements ApplicationLinksResource {

    private final ApplicationLinksService applicationLinksService;

    public AbstractApplicationLinksResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        this.applicationLinksService = applicationLinksService;
    }

    @Override
    public Map<String, ApplicationLinkBean> getTest() {
        return Collections.singletonMap(ApplicationLinkBean.EXAMPLE_1.getName(), ApplicationLinkBean.EXAMPLE_1);
    }

    @Override
    public Response getApplicationLinks() {
        final Collection<ApplicationLinkBean> applicationLinkBeans = applicationLinksService.getApplicationLinks();
        return Response.ok(applicationLinkBeans).build();
    }

    @Override
    public Response getApplicationLink(
            final UUID uuid) {

        final ApplicationLinkBean linkBean = applicationLinksService.getApplicationLink(uuid);
        return Response.ok(linkBean).build();
    }

    @Override
    public Response setApplicationLinks(
            final boolean ignoreSetupErrors,
            final List<ApplicationLinkBean> applicationLinkBeans) {

        final Collection<ApplicationLinkBean> updatedApplicationLinkBeans = applicationLinksService.setApplicationLinks(
                applicationLinkBeans, ignoreSetupErrors);
        return Response.ok(updatedApplicationLinkBeans).build();
    }

    @Override
    public Response setApplicationLink(
            final UUID uuid,
            final boolean ignoreSetupErrors,
            final ApplicationLinkBean linkBean) {

        final ApplicationLinkBean updatedLinkBean = applicationLinksService.setApplicationLink(
                uuid, linkBean, ignoreSetupErrors);
        return Response.ok(updatedLinkBean).build();
    }

    @Override
    public Response addApplicationLink(
            final boolean ignoreSetupErrors,
            final ApplicationLinkBean linkBean) {

        final ApplicationLinkBean addedApplicationLink = applicationLinksService.addApplicationLink(
                linkBean, ignoreSetupErrors);
        return Response.ok(addedApplicationLink).build();
    }

    @Override
    public Response deleteApplicationLinks(
            final boolean force) {

        applicationLinksService.deleteApplicationLinks(force);
        return Response.ok().build();
    }

    @Override
    public Response deleteApplicationLink(
            final UUID uuid) {

        applicationLinksService.deleteApplicationLink(uuid);
        return Response.ok().build();
    }
}
