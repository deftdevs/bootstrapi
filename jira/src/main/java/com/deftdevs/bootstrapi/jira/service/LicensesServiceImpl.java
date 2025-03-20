package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.jira.model.util.LicenseBeanUtil;
import com.deftdevs.bootstrapi.jira.service.api.JiraLicensesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@ExportAsService(JiraLicensesService.class)
public class LicensesServiceImpl implements JiraLicensesService {

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
    public Collection<LicenseBean> setLicenses(
            @NotNull final Collection<LicenseBean> licenseBeans) {

        final List<String> licenses = licenseBeans.stream()
                .map(LicenseBean::getKey)
                .collect(Collectors.toList());

        // set all licenses and fire event(s)
        licenseManager.setLicenses(licenses);

        return getLicenses();
    }

    @Override
    public LicenseBean addLicense(
            @NotNull final LicenseBean licenseBean) {

        return LicenseBeanUtil.toLicenseBean(licenseManager.setLicense(licenseBean.getKey()));
    }

}
