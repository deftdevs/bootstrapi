package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.rest.api.LicensesResource;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import jakarta.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.LICENSES)
@SystemAdminOnly
public class LicensesResourceImpl extends AbstractLicensesResourceImpl implements LicensesResource {

    @Inject
    public LicensesResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }

}
