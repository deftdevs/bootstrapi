package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractApplicationLinkResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Component
@SystemAdminOnly
@Path(BootstrAPI.APPLICATION_LINK)
public class ApplicationLinkResourceImpl extends AbstractApplicationLinkResourceImpl {

    @Inject
    public ApplicationLinkResourceImpl(
            final ApplicationLinksService applicationLinksService) {

        super(applicationLinksService);
    }
}
