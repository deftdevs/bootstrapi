package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import de.aservo.confapi.commons.rest.api.LicensesResource;
import de.aservo.confapi.commons.service.api.LicensesService;

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
    public Response getLicense(
            final String product) {
        final LicenseBean licenseBean = licensesService.getLicense(product);
        return Response.ok(licenseBean).build();
    }

    @Override
    public Response setLicenses(
            final LicensesBean licensesBean) {
        LicensesBean updatedLicensesBean = licensesService.setLicenses(licensesBean);
        return Response.ok(updatedLicensesBean).build();
    }

    @Override
    public Response setLicense(
            final String product,
            final LicenseBean licensesBean) {
        LicenseBean updatedLicenseBean = licensesService.setLicense(
                product,
                licensesBean);
        return Response.ok(updatedLicenseBean).build();
    }

    @Override
    public Response addLicense(
            final LicenseBean licenseBean) {
        LicenseBean updatedLicenseBean = licensesService.addLicense(licenseBean);
        return Response.ok(updatedLicenseBean).build();
    }
}
