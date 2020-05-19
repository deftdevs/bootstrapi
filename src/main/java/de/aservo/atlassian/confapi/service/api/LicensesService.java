package de.aservo.atlassian.confapi.service.api;

import de.aservo.atlassian.confapi.model.LicenseBean;
import de.aservo.atlassian.confapi.model.LicensesBean;

import javax.validation.constraints.NotNull;

public interface LicensesService {

    /**
     * Get the licenses.
     *
     * @return the licenses
     */
    public LicensesBean getLicenses();


    /**
     * Set the licenses
     *
     * @param licensesBean the licenses to set
     * @return the licenses
     */
    public LicensesBean setLicenses(
            @NotNull final LicensesBean licensesBean);

    /**
     * Set a single license
     *
     * @param licenseBean the single license to set
     * @return the licenses
     */
    public LicensesBean setLicense(
            @NotNull final LicenseBean licenseBean);
}
