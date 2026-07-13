package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.util.LicenseKeyRedactor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
     * Set licenses from a map keyed by license key.
     * <p>
     * Input values may be {@code null} (key-only entries are valid); the map keys are the
     * license keys to apply. The returned map is keyed by a redacted form of each input
     * key so that full license keys are not echoed in the response.
     *
     * @param licenseInputs map of license key to (optional) input model; values are ignored
     * @return map of redacted-key to applied {@link LicenseModel}
     */
    default Map<String, LicenseModel> setLicenses(
            final Map<String, LicenseModel> licenseInputs) {

        final Map<String, LicenseModel> result = new LinkedHashMap<>();
        for (final String licenseKey : licenseInputs.keySet()) {
            result.put(LicenseKeyRedactor.redact(licenseKey), addLicense(licenseKey));
        }
        return result;
    }

    /**
     * Add a single license
     *
     * @param licenseModel the single license to set
     * @return the added license
     */
    LicenseModel addLicense(
            final String licenseModel);

}
