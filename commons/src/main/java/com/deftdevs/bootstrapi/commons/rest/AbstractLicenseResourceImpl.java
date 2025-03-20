package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.rest.api.LicenseResource;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.ws.rs.core.Response;

public abstract class AbstractLicenseResourceImpl implements LicenseResource {

    private final LicensesService licensesService;

    public AbstractLicenseResourceImpl(final LicensesService licensesService) {
        this.licensesService = licensesService;
    }

    @Override
    public Response addLicense(
            final String licenseKey) {

        final LicenseBean addedLicenseBean = licensesService.addLicense(licenseKey);
        return Response.ok(addedLicenseBean).build();
    }
}
