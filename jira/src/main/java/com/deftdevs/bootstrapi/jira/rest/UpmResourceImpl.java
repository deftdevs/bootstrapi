package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractUpmResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.UpmService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

@Path(BootstrAPI.UPM)
@SystemAdminOnly
public class UpmResourceImpl extends AbstractUpmResourceImpl {

    @Inject
    public UpmResourceImpl(
            final UpmService upmService) {

        super(upmService);
    }

    // Completely inheriting the implementation of AbstractUpmResourceImpl
}
