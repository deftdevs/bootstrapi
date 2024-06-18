package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.model.LicensesBean;

import javax.validation.constraints.NotNull;

public interface LicensesService {

    /**
     * Gets all licenses.
     *
     * @return the licenses
     */
    LicensesBean getLicenses();

    /**
     * Add a single license
     *
     * @param licenseBean the single license to set
     * @return the added license
     */
    LicenseBean addLicense(
            @NotNull final LicenseBean licenseBean);

}
