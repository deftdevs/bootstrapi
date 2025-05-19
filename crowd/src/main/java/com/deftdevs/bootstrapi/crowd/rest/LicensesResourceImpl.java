package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.ws.rs.Path;

@SystemAdminOnly
@Path(BootstrAPI.LICENSES)
public class LicensesResourceImpl extends AbstractLicensesResourceImpl {

    public LicensesResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }
}
