package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.model.LicensesBean;
import com.deftdevs.bootstrapi.commons.rest.api.LicensesResource;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.ws.rs.core.Response;

public abstract class AbstractLicensesResourceImpl implements LicensesResource {

    private final LicensesService licensesService;

    public AbstractLicensesResourceImpl(final LicensesService licensesService) {
        this.licensesService = licensesService;
    }

    @Override
    public Response getLicenses() {
        final LicensesBean licensesBean = licensesService.getLicenses();
        return Response.ok(licensesBean).build();
    }

    @Override
    public Response addLicense(
            final LicenseBean licenseBean) {
        LicenseBean updatedLicenseBean = licensesService.addLicense(licenseBean);
        return Response.ok(updatedLicenseBean).build();
    }
}
