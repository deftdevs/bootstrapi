package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.jira.model.util.LicenseBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public List<LicenseBean> getLicenses() {
        return StreamSupport.stream(licenseManager.getLicenses().spliterator(), false)
                .map(LicenseBeanUtil::toLicenseBean)
                .collect(Collectors.toList());
    }

    public List<LicenseBean> setLicenses(
            @NotNull final List<LicenseBean> licenseBeans) {

        // clear all licenses first
        licenseManager.removeLicenses(licenseManager.getLicenses());

        // set all licenses and fire event(s)
        licenseBeans.stream()
                .map(LicenseBean::getKey)
                .forEach(licenseManager::setLicense);

        return getLicenses();
    }

    @Override
    public LicenseBean addLicense(
            @NotNull final LicenseBean licenseBean) {

        return LicenseBeanUtil.toLicenseBean(licenseManager.setLicense(licenseBean.getKey()));
    }

}
