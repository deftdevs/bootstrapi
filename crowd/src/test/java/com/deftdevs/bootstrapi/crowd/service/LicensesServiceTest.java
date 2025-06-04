package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.config.ConfigurationException;
import com.atlassian.config.bootstrap.AtlassianBootstrapManager;
import com.atlassian.crowd.service.license.LicenseService;
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
import static com.deftdevs.bootstrapi.crowd.service.LicensesServiceImpl.LICENSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LicensesServiceTest {

    @Mock
    private AtlassianBootstrapManager bootstrapManager;

    @Mock
    private LicenseService licenseService;

    private LicensesServiceImpl licensesService;

    @BeforeEach
    public void setup() {
        licensesService = new LicensesServiceImpl(bootstrapManager, licenseService);
    }

    @Test
    public void testGetLicenses() {
        doReturn(createMockCrowdLicense()).when(licenseService).getLicense();

        final List<LicenseModel> licenseModels = licensesService.getLicenses();
        final LicenseModel returnedModel = licenseModels.iterator().next();
        assertEquals(returnedModel.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        assertEquals(returnedModel.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        assertEquals(returnedModel.getType(), LicenseType.TESTING.toString());
        assertEquals(returnedModel.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        assertEquals(returnedModel.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test
    public void testSetLicenses() {
        final String license1 = "1";
        final String license2 = "2";
        final List<String> licenses = List.of(license1, license2);
        final LicensesServiceImpl licenseServiceSpy = spy(licensesService);
        doReturn(createMockCrowdLicense()).when(licenseService).getLicense();

        licenseServiceSpy.setLicenses(licenses);
        verify(licenseServiceSpy).addLicense(license1);
        verify(licenseServiceSpy).addLicense(license2);
    }

    @Test
    public void testAddLicense() throws ConfigurationException {
        final CrowdLicense crowdLicense = createMockCrowdLicense();
        final LicensesServiceImpl licenseServiceSpy = spy(licensesService);
        doReturn(crowdLicense).when(licenseService).getLicense();

        final String license = "ABC...";
        final LicenseModel licenseModel = licenseServiceSpy.addLicense(license);
        verify(bootstrapManager).setProperty(LICENSE, license);
        verify(bootstrapManager).save();
        assertEquals(licenseModel.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        assertEquals(licenseModel.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        assertEquals(licenseModel.getType(), LicenseType.TESTING.toString());
        assertEquals(licenseModel.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        assertEquals(licenseModel.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test
    public void testAddLicenseBadRequestException() throws ConfigurationException {
        doThrow(new ConfigurationException("Failed")).when(bootstrapManager).save();

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
