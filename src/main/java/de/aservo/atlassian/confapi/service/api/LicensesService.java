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
     * @param clear whether or not to clear all licenses before setting the new ones
     * @return the licenses
     */
    public LicensesBean setLicenses(
            boolean clear,
            @NotNull final LicensesBean licensesBean);

    /**
     * Set a single license
     *
     * @param licenseBean the single license to set
     * @param clear whether or not to clear all licenses before setting the new one
     * @return the licenses
     */
    public LicensesBean setLicense(
            boolean clear,
            @NotNull final LicenseBean licenseBean);
}
