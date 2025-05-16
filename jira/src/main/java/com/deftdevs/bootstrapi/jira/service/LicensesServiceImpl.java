package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.jira.model.util.LicenseBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
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

    @Override
    public List<LicenseBean> setLicenses(
            final List<String> licenseKeys) {

        // set all licenses and fire event(s)
        licenseManager.setLicenses(licenseKeys);

        return getLicenses();
    }

    @Override
    public LicenseBean addLicense(
            final String license) {

        return LicenseBeanUtil.toLicenseBean(licenseManager.setLicense(license));
    }

}
