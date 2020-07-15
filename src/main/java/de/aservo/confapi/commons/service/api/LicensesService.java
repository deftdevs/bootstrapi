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
     * Set the licenses
     *
     * @param licensesBean the licenses to set
     * @return the licenses
     */
    LicensesBean setLicenses(
            @NotNull final LicensesBean licensesBean);

    /**
     * Add a single license
     *
     * @param licenseBean the single license to set
     * @return the added license
     */
    LicenseBean addLicense(
            @NotNull final LicenseBean licenseBean);
}
