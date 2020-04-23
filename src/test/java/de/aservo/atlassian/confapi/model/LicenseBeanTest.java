package de.aservo.atlassian.confapi.model;

import com.atlassian.sal.api.license.SingleProductLicenseDetailsView;
import de.aservo.atlassian.confapi.junit.AbstractBeanTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class LicenseBeanTest extends AbstractBeanTest {

    @Test
    public void testParameterConstructor() {
        SingleProductLicenseDetailsView license = mock(SingleProductLicenseDetailsView.class);
        LicenseBean bean = new LicenseBean(license);

        assertNotNull(bean);
        assertEquals(bean.getProducts().iterator().next(), license.getProductDisplayName());
        assertEquals(bean.getOrganization(), license.getOrganisationName());
        assertEquals(bean.getLicenseType(), license.getLicenseTypeName());
        assertEquals(bean.getDescription(), license.getDescription());
        assertEquals(bean.getExpiryDate(), license.getMaintenanceExpiryDate());
        assertEquals(bean.getNumUsers(), license.getNumberOfUsers());
    }
}
