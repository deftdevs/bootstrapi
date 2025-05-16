package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.LicenseModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface LicensesService {

    /**
     * Gets all licenses.
     *
     * @return the licenses
     */
    List<LicenseModel> getLicenses();

    /**
     * Set a list of licenses.
     *
     * @return the licenses
     */
    List<LicenseModel> setLicenses(
            List<String> licenseKeys);

    /**
     * Add a single license
     *
     * @param licenseModel the single license to set
     * @return the added license
     */
    LicenseModel addLicense(
            @NotNull final String licenseModel);

}
