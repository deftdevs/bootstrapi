package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.manager.license.CrowdLicenseManager;
import com.atlassian.crowd.manager.license.CrowdLicenseManagerException;
import com.atlassian.extras.api.LicenseType;
import com.atlassian.extras.api.Organisation;
import com.atlassian.extras.api.Product;
import com.atlassian.extras.api.crowd.CrowdLicense;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;

import static de.aservo.confapi.commons.model.LicenseBean.EXAMPLE_2_DEVELOPER_LICENSE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LicensesServiceImplTest {

    @Mock
    private CrowdLicenseManager licenseManager;

    private LicensesServiceImpl licensesService;

    @Before
    public void setup() {
        licensesService = new LicensesServiceImpl(licenseManager);
    }

    @Test
    public void testGetLicenses() throws CrowdLicenseManagerException {
        CrowdLicense license = toMockCrowdLicense(EXAMPLE_2_DEVELOPER_LICENSE);

        doReturn(license).when(licenseManager).getLicense();

        LicensesBean licensesBean = licensesService.getLicenses();
        LicenseBean returnedBean = licensesBean.getLicenses().iterator().next();

        assertEquals(returnedBean.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        assertEquals(returnedBean.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        assertEquals(returnedBean.getType(), LicenseType.TESTING.toString());
        assertEquals(returnedBean.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        assertEquals(returnedBean.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test
    public void testAddLicense() throws CrowdLicenseManagerException {
        CrowdLicense license = toMockCrowdLicense(EXAMPLE_2_DEVELOPER_LICENSE);

        doReturn(license).when(licenseManager).storeLicense(EXAMPLE_2_DEVELOPER_LICENSE.getKey());

        LicenseBean licenseBean = licensesService.addLicense(EXAMPLE_2_DEVELOPER_LICENSE);

        assertEquals(licenseBean.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        assertEquals(licenseBean.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        assertEquals(licenseBean.getType(), LicenseType.TESTING.toString());
        assertEquals(licenseBean.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        assertEquals(licenseBean.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test(expected = BadRequestException.class)
    public void testAddLicenseBadRequestException() throws CrowdLicenseManagerException {
        doThrow(new CrowdLicenseManagerException()).when(licenseManager).storeLicense(anyString());

        licensesService.addLicense(EXAMPLE_2_DEVELOPER_LICENSE);
    }

    private CrowdLicense toMockCrowdLicense(LicenseBean licenseBean) throws CrowdLicenseManagerException {
        CrowdLicense license = mock(CrowdLicense.class);

        Organisation organisation = mock(Organisation.class);
        doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getOrganization()).when(organisation).toString();
        doReturn(organisation).when(license).getOrganisation();

        Collection<Product> products = new ArrayList<>();
        doReturn(products).when(license).getProducts();

        doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getDescription()).when(license).getDescription();
        doReturn(LicenseType.TESTING).when(license).getLicenseType();
        doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate()).when(license).getExpiryDate();
        doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers()).when(license).getMaximumNumberOfUsers();

        return license;
    }
}
