package de.aservo.confapi.jira.service;

import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import de.aservo.confapi.commons.service.api.LicensesService;
import de.aservo.confapi.jira.model.util.LicenseBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Component
@ExportAsService(LicensesService.class)
public class LicensesServiceImpl implements LicensesService {

    private final JiraLicenseManager licenseManager;

    @Inject
    public LicensesServiceImpl(
            @ComponentImport JiraLicenseManager licenseManager) {

        this.licenseManager = licenseManager;
    }

    @Override
    public LicensesBean getLicenses() {
        Collection<LicenseBean> licenseBeans = new ArrayList<>();

        for (LicenseDetails licenseDetails : licenseManager.getLicenses()) {
            licenseBeans.add(LicenseBeanUtil.toLicenseBean(licenseDetails));
        }

        return new LicensesBean(licenseBeans);
    }

    public LicensesBean setLicenses(@NotNull LicensesBean licensesBean) {
        // clear all licenses first
        licenseManager.removeLicenses(licenseManager.getLicenses());

        // set all licenses and fire event(s)
        licensesBean.getLicenses().stream()
                .map(LicenseBean::getKey)
                .forEach(licenseManager::setLicense);

        return getLicenses();
    }

    @Override
    public LicenseBean addLicense(@NotNull LicenseBean licenseBean) {
        return LicenseBeanUtil.toLicenseBean(licenseManager.setLicense(licenseBean.getKey()));
    }

}
