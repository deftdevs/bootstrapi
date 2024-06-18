package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.manager.license.CrowdLicenseManager;
import com.atlassian.crowd.manager.license.CrowdLicenseManagerException;
import com.atlassian.extras.api.crowd.CrowdLicense;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import de.aservo.confapi.commons.service.api.LicensesService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

import static de.aservo.confapi.crowd.model.util.LicenseBeanUtil.toLicenseBean;

@Named
@ExportAsService(LicensesService.class)
public class LicensesServiceImpl implements LicensesService {

    private final CrowdLicenseManager licenseManager;

    @Inject
    public LicensesServiceImpl(@ComponentImport CrowdLicenseManager licenseManager) {
        this.licenseManager = licenseManager;
    }

    @Override
    public LicensesBean getLicenses() {
        Collection<LicenseBean> licenseCollection = Collections.singletonList(toLicenseBean(licenseManager.getLicense()));
        return new LicensesBean(licenseCollection);
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
