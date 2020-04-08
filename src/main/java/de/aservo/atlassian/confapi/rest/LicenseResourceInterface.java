package de.aservo.atlassian.confapi.rest;

import javax.ws.rs.core.Response;

/**
 * The License resource interface.
 */
public interface LicenseResourceInterface {
    /**
     * Returns all licenses.
     *
     * @return the licenses with entity type {@link de.aservo.atlassian.confapi.model.LicensesBean}.
     */
    Response getLicenses();

    /**
     * Sets license.
     *
     * @param clear      true, if licenses shall be cleared before setting the new license
     * @param licenseKey the license key to set
     * @return the added license of type {@link de.aservo.atlassian.confapi.model.LicenseBean}.
     */
    Response setLicense(Boolean clear, final String licenseKey);
}