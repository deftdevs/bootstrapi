package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.LicenseDetails;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class LicenseModelUtil {

    /**
     * Instantiates a new License bean.
     *
     * @param licenseDetails the product license
     */
    @Nonnull
    public static LicenseModel toLicenseModel(
            @Nonnull final LicenseDetails licenseDetails) {

        final LicenseModel licenseModel = LicenseModel.builder()
            .products(licenseDetails.getLicensedApplications().getKeys().stream()
                .map(ApplicationKey::value)
                .collect(Collectors.toList()))
            .type(licenseDetails.getLicenseType() != null ? licenseDetails.getLicenseType().name() : null)
            .organization(licenseDetails.getOrganisation())
            .description(licenseDetails.getDescription())
            .expiryDate(licenseDetails.getMaintenanceExpiryDate())
            // cannot set max users with license details
            .build();
        return licenseModel;
    }

    private LicenseModelUtil() {
    }

}
