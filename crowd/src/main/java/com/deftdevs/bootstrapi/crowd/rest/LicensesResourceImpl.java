package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Component
@SystemAdminOnly
@Path(BootstrAPI.LICENSES)
public class LicensesResourceImpl extends AbstractLicensesResourceImpl {

    @Inject
    public LicensesResourceImpl(LicensesService licensesService) {
        super(licensesService);
    }
}
