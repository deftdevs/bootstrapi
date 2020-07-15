package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.ApplicationLinkBean;
import de.aservo.confapi.commons.model.ApplicationLinksBean;
import de.aservo.confapi.commons.rest.api.ApplicationLinksResource;
import de.aservo.confapi.commons.service.api.ApplicationLinksService;

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
        final ApplicationLinkBean addedApplicationLink = applicationLinksService.addApplicationLink(linkBean);
        return Response.ok(addedApplicationLink).build();
    }
}
