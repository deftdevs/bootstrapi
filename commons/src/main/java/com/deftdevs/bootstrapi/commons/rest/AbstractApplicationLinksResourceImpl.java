package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.rest.api.ApplicationLinksResource;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;

import java.util.Map;

public abstract class AbstractApplicationLinksResourceImpl implements ApplicationLinksResource {

    private final ApplicationLinksService applicationLinksService;

    public AbstractApplicationLinksResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        this.applicationLinksService = applicationLinksService;
    }

    @Override
    public Map<String, ApplicationLinkModel> getApplicationLinks() {
        return applicationLinksService.getApplicationLinks();
    }

    @Override
    public Map<String, ApplicationLinkModel> setApplicationLinks(
            final Map<String, ApplicationLinkModel> applicationLinkModels) {

        return applicationLinksService.setApplicationLinks(applicationLinkModels);
    }

    @Override
    public void deleteApplicationLinks(
            final boolean force) {

        applicationLinksService.deleteApplicationLinks(force);
    }
}
