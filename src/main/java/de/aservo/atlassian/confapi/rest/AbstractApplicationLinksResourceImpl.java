package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.model.ApplicationLinkBean;
import de.aservo.atlassian.confapi.model.ApplicationLinksBean;
import de.aservo.atlassian.confapi.rest.api.ApplicationLinksResource;
import de.aservo.atlassian.confapi.service.api.ApplicationLinksService;

import javax.ws.rs.core.Response;

public abstract class AbstractApplicationLinksResourceImpl implements ApplicationLinksResource {

    private final ApplicationLinksService applicationLinksService;

    public AbstractApplicationLinksResourceImpl(final ApplicationLinksService applicationLinksService) {
        this.applicationLinksService = applicationLinksService;
    }

    @Override
    public Response getApplicationLinks() {
        final ApplicationLinksBean linksBean = applicationLinksService.getApplicationLinks();
        return Response.ok(linksBean).build();
    }

    @Override
    public Response setApplicationLinks(ApplicationLinksBean linksBean) {
        final ApplicationLinksBean updatedLinksBean = applicationLinksService.setApplicationLinks(linksBean);
        return Response.ok(updatedLinksBean).build();
    }

    @Override
    public Response addApplicationLink(ApplicationLinkBean linkBean) {
        final ApplicationLinksBean updatedLinksBean = applicationLinksService.addApplicationLink(linkBean);
        return Response.ok(updatedLinksBean).build();
    }
}
