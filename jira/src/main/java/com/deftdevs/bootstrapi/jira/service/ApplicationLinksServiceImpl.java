package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.commons.service.DefaultApplicationLinksServiceImpl;

public class ApplicationLinksServiceImpl extends DefaultApplicationLinksServiceImpl {

    public ApplicationLinksServiceImpl(
            final MutatingApplicationLinkService mutatingApplicationLinkService,
            final ApplinkStatusService applinkStatusService,
            final TypeAccessor typeAccessor,
            final ApplicationLinksAuthConfigHelper applicationLinksAuthConfigHelper) {

        super(mutatingApplicationLinkService, applinkStatusService, typeAccessor, applicationLinksAuthConfigHelper);
    }
}
