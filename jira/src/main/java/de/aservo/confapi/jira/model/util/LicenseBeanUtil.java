package de.aservo.confapi.jira.model.util;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.LicenseDetails;
import de.aservo.confapi.commons.model.LicenseBean;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class LicenseBeanUtil {

    /**
     * Instantiates a new License bean.
     *
     * @param licenseDetails the product license
     */
    @Nonnull
    public static LicenseBean toLicenseBean(
            @Nonnull final LicenseDetails licenseDetails) {

        final LicenseBean licenseBean = new LicenseBean();

        licenseBean.setProducts(licenseDetails.getLicensedApplications().getKeys().stream()
                .map(ApplicationKey::value)
                .collect(Collectors.toList()));

        if (licenseDetails.getLicenseType() != null) {
            licenseBean.setType(licenseDetails.getLicenseType().name());
        }

        licenseBean.setOrganization(licenseDetails.getOrganisation());
        licenseBean.setDescription(licenseDetails.getDescription());
        licenseBean.setExpiryDate(licenseDetails.getMaintenanceExpiryDate());
        // cannot set max users with license details
        return licenseBean;
    }

    private LicenseBeanUtil() {
    }

}
