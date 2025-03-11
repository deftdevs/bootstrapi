package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.license.CrowdLicenseManager;
import com.atlassian.crowd.manager.license.CrowdLicenseManagerException;
import com.atlassian.extras.api.LicenseType;
import com.atlassian.extras.api.Organisation;
import com.atlassian.extras.api.Product;
import com.atlassian.extras.api.crowd.CrowdLicense;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.deftdevs.bootstrapi.commons.model.LicenseBean.EXAMPLE_2_DEVELOPER_LICENSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LicensesServiceTest {

    @Mock
    private CrowdLicenseManager licenseManager;

    private LicensesServiceImpl licensesService;

    @BeforeEach
    public void setup() {
        licensesService = new LicensesServiceImpl(licenseManager);
    }

    @Test
    public void testGetLicenses() {
        final CrowdLicense license = toMockCrowdLicense();
        doReturn(license).when(licenseManager).getLicense();

        final List<LicenseBean> licenseBeans = licensesService.getLicenses();
        final LicenseBean returnedBean = licenseBeans.iterator().next();
        assertEquals(returnedBean.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        assertEquals(returnedBean.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        assertEquals(returnedBean.getType(), LicenseType.TESTING.toString());
        assertEquals(returnedBean.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        assertEquals(returnedBean.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test
    public void testAddLicense() throws CrowdLicenseManagerException {
        CrowdLicense license = toMockCrowdLicense();

        doReturn(license).when(licenseManager).storeLicense(EXAMPLE_2_DEVELOPER_LICENSE.getKey());

        LicenseBean licenseBean = licensesService.addLicense(EXAMPLE_2_DEVELOPER_LICENSE);

        assertEquals(licenseBean.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        assertEquals(licenseBean.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        assertEquals(licenseBean.getType(), LicenseType.TESTING.toString());
        assertEquals(licenseBean.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        assertEquals(licenseBean.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test
    public void testAddLicenseBadRequestException() throws CrowdLicenseManagerException {
        doThrow(new CrowdLicenseManagerException()).when(licenseManager).storeLicense(anyString());

        assertThrows(BadRequestException.class, () -> {
            licensesService.addLicense(EXAMPLE_2_DEVELOPER_LICENSE);
        });
    }

    private CrowdLicense toMockCrowdLicense() {
        CrowdLicense license = mock(CrowdLicense.class);

        Organisation organisation = mock(Organisation.class);
        doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getOrganization()).when(organisation).toString();
        doReturn(organisation).when(license).getOrganisation();

        List<Product> products = new ArrayList<>();
        doReturn(products).when(license).getProducts();

        doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getDescription()).when(license).getDescription();
        doReturn(LicenseType.TESTING).when(license).getLicenseType();
        doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate()).when(license).getExpiryDate();
        doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers()).when(license).getMaximumNumberOfUsers();

        return license;
    }
}
