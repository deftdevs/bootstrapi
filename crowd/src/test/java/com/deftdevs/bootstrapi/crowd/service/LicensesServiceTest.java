package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.config.ConfigurationException;
import com.atlassian.config.bootstrap.AtlassianBootstrapManager;
import com.atlassian.extras.api.LicenseType;
import com.atlassian.extras.api.Organisation;
import com.atlassian.extras.api.Product;
import com.atlassian.extras.api.crowd.CrowdLicense;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.deftdevs.bootstrapi.commons.model.LicenseModel.EXAMPLE_2_DEVELOPER_LICENSE;
import static com.deftdevs.bootstrapi.crowd.service.LicensesServiceImpl.LICENSE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LicensesServiceTest {

    @Mock
    private AtlassianBootstrapManager bootstrapManager;

    private LicensesServiceImpl licensesService;

    @BeforeEach
    public void setup() {
        licensesService = new LicensesServiceImpl(bootstrapManager);
    }

    @Test
    public void testSetLicenses() {
        final String license1 = "1";
        final String license2 = "2";
        final List<String> licenses = List.of(license1, license2);
        final LicensesServiceImpl licenseServiceSpy = spy(licensesService);

        licenseServiceSpy.setLicenses(licenses);
        verify(licenseServiceSpy).addLicense(license1);
        verify(licenseServiceSpy).addLicense(license2);
    }

    @Test
    public void testAddLicense() throws ConfigurationException {
        final CrowdLicense crowdLicense = createMockCrowdLicense();
        final LicensesServiceImpl licenseServiceSpy = spy(licensesService);

        final String license = "ABC...";
        /* final LicenseModel licenseModel = */ licenseServiceSpy.addLicense(license);
        verify(bootstrapManager).setProperty(LICENSE, license);
        verify(bootstrapManager).save();
        // assertEquals(licenseModel.getDescription(), EXAMPLE_2_DEVELOPER_LICENSE.getDescription());
        // assertEquals(licenseModel.getOrganization(), EXAMPLE_2_DEVELOPER_LICENSE.getOrganization());
        // assertEquals(licenseModel.getType(), LicenseType.TESTING.toString());
        // assertEquals(licenseModel.getExpiryDate(), EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate());
        // assertEquals(licenseModel.getMaxUsers(), EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers());
    }

    @Test
    public void testAddLicenseBadRequestException() throws ConfigurationException {
        doThrow(new ConfigurationException("Failed")).when(bootstrapManager).save();

        assertThrows(BadRequestException.class, () -> {
            licensesService.addLicense("ABC...");
        });
    }

    private CrowdLicense createMockCrowdLicense() {
        final CrowdLicense license = mock(CrowdLicense.class);

        final Organisation organisation = mock(Organisation.class);
        lenient().doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getOrganization()).when(organisation).toString();
        lenient().doReturn(organisation).when(license).getOrganisation();

        final List<Product> products = new ArrayList<>();
        lenient().doReturn(products).when(license).getProducts();

        lenient().doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getDescription()).when(license).getDescription();
        lenient().doReturn(LicenseType.TESTING).when(license).getLicenseType();
        lenient().doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getExpiryDate()).when(license).getExpiryDate();
        lenient().doReturn(EXAMPLE_2_DEVELOPER_LICENSE.getMaxUsers()).when(license).getMaximumNumberOfUsers();

        return license;
    }
}
