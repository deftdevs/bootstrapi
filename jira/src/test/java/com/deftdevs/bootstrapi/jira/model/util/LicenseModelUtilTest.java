package com.deftdevs.bootstrapi.jira.model.util;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.license.LicensedApplications;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LicenseModelUtilTest {

    @Test
    void testToLicenseModel() {
        final LicenseModel exampleLicenseModel = LicenseModel.EXAMPLE_1;

        final LicensedApplications licensedApplications = mock(LicensedApplications.class);
        doReturn(Collections.singleton(ApplicationKey.valueOf(exampleLicenseModel.getProducts().iterator().next()))).when(licensedApplications).getKeys();

        final LicenseDetails licenseDetails = mock(LicenseDetails.class);
        // license type of example bean is null and usually a string but this way we can notice if it changes in the future
        doReturn(exampleLicenseModel.getType()).when(licenseDetails).getLicenseType();
        doReturn(exampleLicenseModel.getOrganization()).when(licenseDetails).getOrganisation();
        doReturn(exampleLicenseModel.getDescription()).when(licenseDetails).getDescription();
        doReturn(exampleLicenseModel.getExpiryDate()).when(licenseDetails).getMaintenanceExpiryDate();
        // we cannot check the max users limit with license details
        doReturn(licensedApplications).when(licenseDetails).getLicensedApplications();

        final LicenseModel bean = LicenseModelUtil.toLicenseModel(licenseDetails);

        assertNotNull(bean);
        assertEquals(exampleLicenseModel.getType(), bean.getType());
        assertEquals(exampleLicenseModel.getOrganization(), bean.getOrganization());
        assertEquals(exampleLicenseModel.getDescription(), bean.getDescription());
        assertEquals(exampleLicenseModel.getExpiryDate(), bean.getExpiryDate());
        assertEquals(0, bean.getMaxUsers());
        assertEquals(exampleLicenseModel.getProducts().iterator().next(), bean.getProducts().iterator().next());
    }

}
