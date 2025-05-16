package com.deftdevs.bootstrapi.confluence.model.util;

import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LicenseModelUtilTest {

    @Test
    void testToLicenseModel() {
        final SingleProductLicenseDetailsView license = mock(SingleProductLicenseDetailsView.class);
        final LicenseModel bean = LicenseModelUtil.toLicenseModel(license);

        assertNotNull(bean);
        assertEquals(bean.getProducts().iterator().next(), license.getProductDisplayName());
        assertEquals(bean.getOrganization(), license.getOrganisationName());
        assertEquals(bean.getType(), license.getLicenseTypeName());
        assertEquals(bean.getDescription(), license.getDescription());
        assertEquals(bean.getExpiryDate(), license.getMaintenanceExpiryDate());
        assertEquals(bean.getMaxUsers(), license.getNumberOfUsers());
    }

}
