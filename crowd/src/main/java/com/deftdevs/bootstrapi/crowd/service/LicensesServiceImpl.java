package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.license.CrowdLicenseManager;
import com.atlassian.crowd.manager.license.CrowdLicenseManagerException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.List;

import static com.deftdevs.bootstrapi.crowd.model.util.LicenseBeanUtil.toLicenseBean;

@Named
public class LicensesServiceImpl implements LicensesService {

    private final CrowdLicenseManager licenseManager;

    @Inject
    public LicensesServiceImpl(@ComponentImport CrowdLicenseManager licenseManager) {
        this.licenseManager = licenseManager;
    }

    @Override
    public List<LicenseBean> getLicenses() {
        return Collections.singletonList(toLicenseBean(licenseManager.getLicense()));
    }

    @Override
    public List<LicenseBean> setLicenses(
            final List<String> licenseKeys) {

        licenseKeys.forEach(this::addLicense);
        return getLicenses();
    }

    @Override
    public LicenseBean addLicense(
            final String licenseKey) {

        try {
            return toLicenseBean(licenseManager.storeLicense(licenseKey));
        } catch (CrowdLicenseManagerException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
