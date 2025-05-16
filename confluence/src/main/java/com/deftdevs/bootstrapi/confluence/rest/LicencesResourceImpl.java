package com.deftdevs.bootstrapi.confluence.rest;

import com.atlassian.plugins.rest.common.security.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.LICENSES)
@SystemAdminOnly
@Component
public class LicencesResourceImpl extends AbstractLicensesResourceImpl {

    @Inject
    public LicencesResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }

    // Completely inhering the implementation of AbstractLicensesResourceImpl
}
