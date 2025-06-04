package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.config.ConfigurationException;
import com.atlassian.config.bootstrap.AtlassianBootstrapManager;
import com.atlassian.crowd.service.license.LicenseService;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import java.util.Collections;
import java.util.List;

import static com.deftdevs.bootstrapi.crowd.model.util.LicenseModelUtil.toLicenseModel;

public class LicensesServiceImpl implements LicensesService {

    public static final String LICENSE = "license";

    private final AtlassianBootstrapManager bootstrapManager;

    private final LicenseService licenseService;

    public LicensesServiceImpl(
            final AtlassianBootstrapManager bootstrapManager,
            final LicenseService licenseService) {

        this.bootstrapManager = bootstrapManager;
        this.licenseService = licenseService;
    }

    @Override
    public List<LicenseModel> getLicenses() {
        return Collections.singletonList(toLicenseModel(licenseService.getLicense()));
    }

    @Override
    public List<LicenseModel> setLicenses(
            final List<String> licenseKeys) {

        licenseKeys.forEach(this::addLicense);
        return getLicenses();
    }

    public LicenseModel addLicense(
            final String licenseKey) {

        try {
            bootstrapManager.setProperty(LICENSE, licenseKey);
            bootstrapManager.save();
        } catch (ConfigurationException e) {
            throw new BadRequestException(e.getMessage());
        }

        return toLicenseModel(licenseService.getLicense());
    }

}
