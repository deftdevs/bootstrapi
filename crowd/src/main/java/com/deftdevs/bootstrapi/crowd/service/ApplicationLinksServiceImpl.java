package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.commons.service.DefaultApplicationLinksServiceImpl;
import com.deftdevs.bootstrapi.commons.service.api.ApplicationLinksService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@ExportAsService(ApplicationLinksService.class)
public class ApplicationLinksServiceImpl extends DefaultApplicationLinksServiceImpl {

    @Inject
    public ApplicationLinksServiceImpl(
            @ComponentImport final MutatingApplicationLinkService mutatingApplicationLinkService,
            @ComponentImport final ApplinkStatusService applinkStatusService,
            @ComponentImport final TypeAccessor typeAccessor,
            final ApplicationLinksAuthConfigHelper applicationLinksAuthConfigHelper) {

        super(mutatingApplicationLinkService, applinkStatusService, typeAccessor, applicationLinksAuthConfigHelper);
    }
}
