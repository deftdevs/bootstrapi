package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.model.LicenseBean;
import de.aservo.atlassian.confapi.model.LicensesBean;
import de.aservo.atlassian.confapi.rest.api.LicensesResource;
import de.aservo.atlassian.confapi.service.api.LicensesService;

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
    public Response setLicenses(boolean clear, LicensesBean licensesBean) {
        LicensesBean updatedLicensesBean = licensesService.setLicenses(clear, licensesBean);
        return Response.ok(updatedLicensesBean).build();
    }

    @Override
    public Response setLicense(boolean clear, LicenseBean licenseBean) {
        LicensesBean updatedLicensesBean = licensesService.setLicense(clear, licenseBean);
        return Response.ok(updatedLicensesBean).build();
    }
}
