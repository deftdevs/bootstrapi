package com.deftdevs.bootstrapi.crowd.model.util;

import com.atlassian.extras.api.crowd.CrowdLicense;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;

import java.util.ArrayList;
import java.util.List;

public class LicenseModelUtil {

    public static LicenseModel toLicenseModel(
            final CrowdLicense license) {

        List<String> products = new ArrayList<>();
        license.getProducts().forEach(product -> products.add(product.toString()));
        final LicenseModel licenseModel = new LicenseModel();
        licenseModel.setProducts(products);
        licenseModel.setMaxUsers(license.getMaximumNumberOfUsers());
        licenseModel.setExpiryDate(license.getExpiryDate());
        licenseModel.setDescription(license.getDescription());
        licenseModel.setOrganization(license.getOrganisation().toString());
        licenseModel.setType(license.getLicenseType().toString());

        return licenseModel;
    }

    private LicenseModelUtil() {

    }
}
