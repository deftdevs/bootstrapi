package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.license.CrowdLicenseManager;
import com.atlassian.crowd.manager.license.CrowdLicenseManagerException;
import com.atlassian.extras.api.crowd.CrowdLicense;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Collections;

import static com.deftdevs.bootstrapi.crowd.model.util.LicenseBeanUtil.toLicenseBean;

@Named
@ExportAsService(LicensesService.class)
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
    public LicenseBean addLicense(@NotNull LicenseBean licenseBean) {
        CrowdLicense license = null;
        try {
            license = licenseManager.storeLicense(licenseBean.getKey());
        } catch (CrowdLicenseManagerException e) {
            throw new BadRequestException(e.getMessage());
        }
        return toLicenseBean(license);
    }
}
