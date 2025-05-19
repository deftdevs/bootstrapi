package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.license.JiraLicenseManager;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.jira.model.util.LicenseModelUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LicensesServiceImpl implements LicensesService {

    private final JiraLicenseManager licenseManager;

    public LicensesServiceImpl(
            JiraLicenseManager licenseManager) {

        this.licenseManager = licenseManager;
    }

    @Override
    public List<LicenseModel> getLicenses() {
        return StreamSupport.stream(licenseManager.getLicenses().spliterator(), false)
                .map(LicenseModelUtil::toLicenseModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<LicenseModel> setLicenses(
            final List<String> licenseKeys) {

        // set all licenses and fire event(s)
        licenseManager.setLicenses(licenseKeys);

        return getLicenses();
    }

    @Override
    public LicenseModel addLicense(
            final String license) {

        return LicenseModelUtil.toLicenseModel(licenseManager.setLicense(license));
    }

}
