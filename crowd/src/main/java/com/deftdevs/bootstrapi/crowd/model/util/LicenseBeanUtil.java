package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.extras.api.crowd.CrowdLicense;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class LicenseBeanUtil {

    public static LicenseBean toLicenseBean(
            @NotNull final CrowdLicense license) {

        List<String> products = new ArrayList<>();
        license.getProducts().forEach(product -> products.add(product.toString()));
        final LicenseBean licenseBean = new LicenseBean();
        licenseBean.setProducts(products);
        licenseBean.setMaxUsers(license.getMaximumNumberOfUsers());
        licenseBean.setExpiryDate(license.getExpiryDate());
        licenseBean.setDescription(license.getDescription());
        licenseBean.setOrganization(license.getOrganisation().toString());
        licenseBean.setType(license.getLicenseType().toString());

        return licenseBean;
    }

    private LicenseBeanUtil() {

    }
}
