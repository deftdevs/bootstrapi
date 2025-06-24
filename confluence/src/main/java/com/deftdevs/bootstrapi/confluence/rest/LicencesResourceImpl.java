package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import jakarta.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.LICENSES)
@SystemAdminOnly
public class LicencesResourceImpl extends AbstractLicensesResourceImpl {

    @Inject
    public LicencesResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }

    // Completely inhering the implementation of AbstractLicensesResourceImpl
}
