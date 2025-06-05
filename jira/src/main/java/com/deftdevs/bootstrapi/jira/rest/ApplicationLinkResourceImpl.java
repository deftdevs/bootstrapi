package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractApplicationLinkResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.APPLICATION_LINK)
@SystemAdminOnly
public class ApplicationLinkResourceImpl extends AbstractApplicationLinkResourceImpl {

    @Inject
    public ApplicationLinkResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        super(applicationLinksService);
    }

}
