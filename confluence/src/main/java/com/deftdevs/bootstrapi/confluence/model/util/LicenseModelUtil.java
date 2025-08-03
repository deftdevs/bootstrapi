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

        return LicenseModel.builder()
                .products(Collections.singletonList(productLicense.getProductDisplayName()))
                .type(productLicense.getLicenseTypeName())
                .organization(productLicense.getOrganisationName())
                .description(productLicense.getDescription())
                .expiryDate(productLicense.getMaintenanceExpiryDate())
                .maxUsers(productLicense.getNumberOfUsers())
                .build();
    }

    private LicenseModelUtil() {
    }

}
