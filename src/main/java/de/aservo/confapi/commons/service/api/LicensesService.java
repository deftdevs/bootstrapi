package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;

import javax.validation.constraints.NotNull;

public interface LicensesService {

    /**
     * Gets all licenses.
     *
     * @return the licenses
     */
    LicensesBean getLicenses();

    /**
     * Gets a single liocense.
     *
     * @param product the license product code to query
     * @return the gadget
     */
    LicenseBean getLicense(
            @NotNull final String product);

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
            @NotNull final String product,
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
