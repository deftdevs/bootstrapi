package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.config.ConfigurationException;
import com.atlassian.config.bootstrap.AtlassianBootstrapManager;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import java.util.Collections;
import java.util.List;

public class LicensesServiceImpl implements LicensesService {

    public static final String LICENSE = "license";

    private static final String ERROR_MESSAGE = "It is currently not possible to display license details in BootstrAPI for Crowd because of API changes made by Atlassian";

    private final AtlassianBootstrapManager bootstrapManager;

    public LicensesServiceImpl(
            final AtlassianBootstrapManager bootstrapManager) {

        this.bootstrapManager = bootstrapManager;
    }

    @Override
    public List<LicenseModel> getLicenses() {
        throw new InternalServerErrorException(ERROR_MESSAGE);
    }

    @Override
    public List<LicenseModel> setLicenses(
            final List<String> licenseKeys) {

        licenseKeys.forEach(this::addLicense);

        final LicenseModel licenseModel = new LicenseModel();
        licenseModel.setDescription(ERROR_MESSAGE);
        return Collections.singletonList(licenseModel);
    }

    public LicenseModel addLicense(
            final String licenseKey) {

        try {
            bootstrapManager.setProperty(LICENSE, licenseKey);
            bootstrapManager.save();
        } catch (ConfigurationException e) {
            throw new BadRequestException(e.getMessage());
        }

        final LicenseModel licenseModel = new LicenseModel();
        licenseModel.setDescription(ERROR_MESSAGE);
        return licenseModel;
    }

}
