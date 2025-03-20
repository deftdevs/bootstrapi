package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.rest.api.LicensesResource;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.ws.rs.core.Response;
import java.util.List;

public abstract class AbstractLicensesResourceImpl implements LicensesResource {

    private final LicensesService licensesService;

    public AbstractLicensesResourceImpl(final LicensesService licensesService) {
        this.licensesService = licensesService;
    }

    @Override
    public Response getLicenses() {
        final List<LicenseBean> licenseBeans = licensesService.getLicenses();
        return Response.ok(licenseBeans).build();
    }

    @Override
    public Response setLicenses(
            final List<String> licenseKeys) {

        final List<LicenseBean> setLicenseBeans = licensesService.setLicenses(licenseKeys);
        return Response.ok(setLicenseBeans).build();
    }

}
