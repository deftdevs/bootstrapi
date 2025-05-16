package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.i18n.InvalidOperationException;
import com.atlassian.sal.api.license.LicenseHandler;
import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import com.deftdevs.bootstrapi.confluence.model.util.LicenseBeanUtil;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.atlassian.confluence.setup.ConfluenceBootstrapConstants.DEFAULT_LICENSE_REGISTRY_KEY;

@Component
public class LicensesServiceImpl implements LicensesService {

    private final LicenseHandler licenseHandler;

    @Inject
    public LicensesServiceImpl(@ComponentImport final LicenseHandler licenseHandler) {
        this.licenseHandler = licenseHandler;
    }

    @Override
    public List<LicenseBean> getLicenses() {
        final SingleProductLicenseDetailsView confluenceLicenseView = licenseHandler.getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);

        if (confluenceLicenseView == null) {
            throw new InternalServerErrorException("Cannot get license details");
        }

        return Collections.singletonList(LicenseBeanUtil.toLicenseBean(confluenceLicenseView));
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
            licenseHandler.addProductLicense(DEFAULT_LICENSE_REGISTRY_KEY, licenseKey);
        } catch (InvalidOperationException e) {
            throw new BadRequestException("The new license cannot be set");
        }

        final SingleProductLicenseDetailsView productLicenseDetails = licenseHandler.getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);
        if (productLicenseDetails == null) {
            throw new InternalServerErrorException("Cannot get license details");
        }

        return LicenseBeanUtil.toLicenseBean(productLicenseDetails);
    }

}
