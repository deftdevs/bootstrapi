package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;

import javax.validation.constraints.NotNull;

public interface LicensesService {

    /**
     * Get the licenses.
     *
     * @return the licenses
     */
    LicensesBean getLicenses();


    /**
     * Set or updates a list of licenses
     *
     * @param licensesBean the licenses to set
     * @return the licenses
     */
    LicensesBean setLicenses(
            @NotNull final LicensesBean licensesBean);

    /**
     * Updates a single license
     *
     * @param product     the product license type to update
     * @param licenseBean the licenses to set
     * @return the licenses
     */
    LicenseBean setLicense(
            final String product,
            @NotNull final LicenseBean licenseBean);

    /**
     * Add a single license
     *
     * @param licenseBean the single license to set
     * @return the added license
     */
    LicenseBean addLicense(
            @NotNull final LicenseBean licenseBean);
}
