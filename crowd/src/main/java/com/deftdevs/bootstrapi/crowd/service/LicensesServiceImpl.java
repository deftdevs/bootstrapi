package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.license.CrowdLicenseManager;
import com.atlassian.crowd.manager.license.CrowdLicenseManagerException;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import java.util.Collections;
import java.util.List;

import static com.deftdevs.bootstrapi.crowd.model.util.LicenseModelUtil.toLicenseModel;

public class LicensesServiceImpl implements LicensesService {

    private final CrowdLicenseManager licenseManager;

    public LicensesServiceImpl(
            final CrowdLicenseManager licenseManager) {

        this.licenseManager = licenseManager;
    }

    @Override
    public List<LicenseModel> getLicenses() {
        return Collections.singletonList(toLicenseModel(licenseManager.getLicense()));
    }

    @Override
    public List<LicenseModel> setLicenses(
            final List<String> licenseKeys) {

        licenseKeys.forEach(this::addLicense);
        return getLicenses();
    }

    @Override
    public LicenseModel addLicense(
            final String licenseKey) {

        try {
            return toLicenseModel(licenseManager.storeLicense(licenseKey));
        } catch (CrowdLicenseManagerException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
