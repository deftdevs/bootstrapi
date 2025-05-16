package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;

import javax.annotation.Nonnull;
import java.util.Collections;

public class LicenseModelUtil {

    /**
     * Instantiates a new License bean.
     *
     * @param productLicense the product license
     */
    @Nonnull
    public static LicenseModel toLicenseModel(
            @Nonnull final SingleProductLicenseDetailsView productLicense) {

        final LicenseModel licenseModel = new LicenseModel();
        licenseModel.setProducts(Collections.singletonList(productLicense.getProductDisplayName()));
        licenseModel.setType(productLicense.getLicenseTypeName());
        licenseModel.setOrganization(productLicense.getOrganisationName());
        licenseModel.setDescription(productLicense.getDescription());
        licenseModel.setExpiryDate(productLicense.getMaintenanceExpiryDate());
        licenseModel.setMaxUsers(productLicense.getNumberOfUsers());
        return licenseModel;
    }

    private LicenseModelUtil() {
    }

}
