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

        final LicenseModel licenseModel = new LicenseModel();

        licenseModel.setProducts(licenseDetails.getLicensedApplications().getKeys().stream()
                .map(ApplicationKey::value)
                .collect(Collectors.toList()));

        if (licenseDetails.getLicenseType() != null) {
            licenseModel.setType(licenseDetails.getLicenseType().name());
        }

        licenseModel.setOrganization(licenseDetails.getOrganisation());
        licenseModel.setDescription(licenseDetails.getDescription());
        licenseModel.setExpiryDate(licenseDetails.getMaintenanceExpiryDate());
        // cannot set max users with license details
        return licenseModel;
    }

    private LicenseModelUtil() {
    }

}
