package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicenseResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.ws.rs.Path;

@Path(BootstrAPI.LICENSE)
@SystemAdminOnly
public class LicenceResourceImpl extends AbstractLicenseResourceImpl {

    public LicenceResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }

    // Completely inhering the implementation of AbstractLicenseResourceImpl
}
