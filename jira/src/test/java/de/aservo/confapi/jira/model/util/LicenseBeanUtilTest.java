package de.aservo.confapi.jira.model.util;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.license.LicensedApplications;
import de.aservo.confapi.commons.model.LicenseBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LicenseBeanUtilTest {

    @Test
    void testToLicenseBean() {
        final LicenseBean exampleLicenseBean = LicenseBean.EXAMPLE_1;

        final LicensedApplications licensedApplications = mock(LicensedApplications.class);
        doReturn(Collections.singleton(ApplicationKey.valueOf(exampleLicenseBean.getProducts().iterator().next()))).when(licensedApplications).getKeys();

        final LicenseDetails licenseDetails = mock(LicenseDetails.class);
        // license type of example bean is null and usually a string but this way we can notice if it changes in the future
        doReturn(exampleLicenseBean.getType()).when(licenseDetails).getLicenseType();
        doReturn(exampleLicenseBean.getOrganization()).when(licenseDetails).getOrganisation();
        doReturn(exampleLicenseBean.getDescription()).when(licenseDetails).getDescription();
        doReturn(exampleLicenseBean.getExpiryDate()).when(licenseDetails).getMaintenanceExpiryDate();
        // we cannot check the max users limit with license details
        doReturn(licensedApplications).when(licenseDetails).getLicensedApplications();

        final LicenseBean bean = LicenseBeanUtil.toLicenseBean(licenseDetails);

        assertNotNull(bean);
        assertEquals(exampleLicenseBean.getType(), bean.getType());
        assertEquals(exampleLicenseBean.getOrganization(), bean.getOrganization());
        assertEquals(exampleLicenseBean.getDescription(), bean.getDescription());
        assertEquals(exampleLicenseBean.getExpiryDate(), bean.getExpiryDate());
        assertEquals(0, bean.getMaxUsers());
        assertEquals(exampleLicenseBean.getProducts().iterator().next(), bean.getProducts().iterator().next());
    }

}
