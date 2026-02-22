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
        return LicenseModel.builder()
            .products(products)
            .maxUsers(license.getMaximumNumberOfUsers())
            .expiryDate(license.getExpiryDate())
            .description(license.getDescription())
            .organization(license.getOrganisation().toString())
            .type(license.getLicenseType().toString())
            .build();
    }

    private LicenseModelUtil() {

    }
}
