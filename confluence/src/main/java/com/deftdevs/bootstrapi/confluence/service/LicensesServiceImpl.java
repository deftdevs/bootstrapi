package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.i18n.InvalidOperationException;
import com.atlassian.sal.api.license.LicenseHandler;
import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import com.deftdevs.bootstrapi.commons.exception.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.model.LicensesBean;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.confluence.model.util.LicenseBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;

import static com.atlassian.confluence.setup.ConfluenceBootstrapConstants.DEFAULT_LICENSE_REGISTRY_KEY;

@Component
@ExportAsService(LicensesService.class)
public class LicensesServiceImpl implements LicensesService {

    private final LicenseHandler licenseHandler;

    @Inject
    public LicensesServiceImpl(@ComponentImport final LicenseHandler licenseHandler) {
        this.licenseHandler = licenseHandler;
    }

    @Override
    public LicensesBean getLicenses() {
        SingleProductLicenseDetailsView confluenceLicenseView = licenseHandler.getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);
        LicensesBean licensesBean = new LicensesBean();
        licensesBean.setLicenses(Collections.singletonList(LicenseBeanUtil.toLicenseBean(confluenceLicenseView)));
        return licensesBean;
    }

    @Override
    public LicenseBean addLicense(LicenseBean licenseBean) {
        try {
            licenseHandler.addProductLicense(DEFAULT_LICENSE_REGISTRY_KEY, licenseBean.getKey());
        } catch (InvalidOperationException e) {
            throw new BadRequestException("The new license cannot be set");
        }
        return licenseBean;
    }

}
