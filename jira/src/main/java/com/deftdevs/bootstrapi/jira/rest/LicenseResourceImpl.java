package com.deftdevs.bootstrapi.jira.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.commons.constants.BootstrAPI;
import com.deftdevs.bootstrapi.commons.rest.AbstractLicenseResourceImpl;
import com.deftdevs.bootstrapi.commons.rest.api.LicenseResource;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import jakarta.inject.Inject;
import javax.ws.rs.Path;

@Path(BootstrAPI.LICENSE)
@SystemAdminOnly
public class LicenseResourceImpl extends AbstractLicenseResourceImpl implements LicenseResource {

    @Inject
    public LicenseResourceImpl(
            final LicensesService licensesService) {

        super(licensesService);
    }

}
