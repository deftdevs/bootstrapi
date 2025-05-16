package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.license.CrowdLicenseManager;
import com.atlassian.crowd.manager.license.CrowdLicenseManagerException;
import com.atlassian.extras.api.LicenseType;
import com.atlassian.extras.api.Organisation;
import com.atlassian.extras.api.Product;
import com.atlassian.extras.api.crowd.CrowdLicense;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.deftdevs.bootstrapi.commons.model.LicenseModel.EXAMPLE_2_DEVELOPER_LICENSE;
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
        final CrowdLicense license = createMockCrowdLicense();
        doReturn(license).when(licenseManager).getLicense();

        final List<LicenseModel> licenseModels = licensesService.getLicenses();
        final LicenseModel returnedModel = licenseModels.iterator().next();
        assertEquals(returnedModel.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        assertEquals(returnedModel.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        assertEquals(returnedModel.getType(), LicenseType.TESTING.toString());
        assertEquals(returnedModel.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        assertEquals(returnedModel.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test
    public void testSetLicenses() throws CrowdLicenseManagerException {
        final CrowdLicense license = createMockCrowdLicense();
        doReturn(license).when(licenseManager).getLicense();
        doReturn(license).when(licenseManager).storeLicense(anyString());

        final String license1 = "1";
        final String license2 = "2";
        final List<String> licenses = List.of(license1, license2);
        final LicensesServiceImpl spy = spy(licensesService);
        spy.setLicenses(licenses);
        verify(spy).addLicense(license1);
        verify(spy).addLicense(license2);
    }

    @Test
    public void testAddLicense() throws CrowdLicenseManagerException {
        final CrowdLicense license = createMockCrowdLicense();
        doReturn(license).when(licenseManager).storeLicense("ABC...");

        final LicenseModel licenseModel = licensesService.addLicense("ABC...");
        assertEquals(licenseModel.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        assertEquals(licenseModel.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        assertEquals(licenseModel.getType(), LicenseType.TESTING.toString());
        assertEquals(licenseModel.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        assertEquals(licenseModel.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test
    public void testAddLicenseBadRequestException() throws CrowdLicenseManagerException {
        doThrow(new CrowdLicenseManagerException()).when(licenseManager).storeLicense(anyString());

        assertThrows(BadRequestException.class, () -> {
            licensesService.addLicense("ABC...");
        });
    }

    private CrowdLicense createMockCrowdLicense() {
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
