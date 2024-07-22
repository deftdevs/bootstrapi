package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.LicenseBean;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface LicensesService {

    /**
     * Gets all licenses.
     *
     * @return the licenses
     */
    List<LicenseBean> getLicenses();

    /**
     * Add a single license
     *
     * @param licenseBean the single license to set
     * @return the added license
     */
    LicenseBean addLicense(
            @NotNull final LicenseBean licenseBean);

}
