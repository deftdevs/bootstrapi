package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicenseResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.ws.rs.Path;

@SystemAdminOnly
@Path(BootstrAPI.LICENSE)
public class LicenseResourceImpl extends AbstractLicenseResourceImpl {

    public LicenseResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }
}
