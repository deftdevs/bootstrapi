package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicenseResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.inject.Inject;
import javax.ws.rs.Path;

@SystemAdminOnly
@Path(BootstrAPI.LICENSE)
public class LicenseResourceImpl extends AbstractLicenseResourceImpl {

    @Inject
    public LicenseResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }
}
