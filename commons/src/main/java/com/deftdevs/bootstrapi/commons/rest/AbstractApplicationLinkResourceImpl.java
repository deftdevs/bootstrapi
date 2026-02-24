package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.rest.api.ApplicationLinkResource;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;

import java.util.UUID;

public abstract class AbstractApplicationLinkResourceImpl implements ApplicationLinkResource {

    private final ApplicationLinksService applicationLinksService;

    public AbstractApplicationLinkResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        this.applicationLinksService = applicationLinksService;
    }

    @Override
    public ApplicationLinkModel getApplicationLink(
            final UUID uuid) {

        return applicationLinksService.getApplicationLink(uuid);
    }

    @Override
    public ApplicationLinkModel createApplicationLink(
            final ApplicationLinkModel applicationLinkModel) {

        return applicationLinksService.addApplicationLink(applicationLinkModel);
    }

    @Override
    public ApplicationLinkModel updateApplicationLink(
            final UUID uuid,
            final ApplicationLinkModel applicationLinkModel) {

        return applicationLinksService.setApplicationLink(uuid, applicationLinkModel);
    }

    @Override
    public void deleteApplicationLink(
            final UUID uuid) {

        applicationLinksService.deleteApplicationLink(uuid);
    }
}
