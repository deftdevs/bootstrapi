package com.deftdevs.bootstrapi.jira.service;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.license.LicensedApplications;
import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.atlassian.extras.api.LicenseType.TESTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LicensesServiceTest {

    private static final String LICENSE_KEY = "Aaa...";

    @Mock
    private JiraLicenseManager licenseManager;

    private LicensesServiceImpl licensesService;

    @BeforeEach
    public void setup() {
        licensesService = new LicensesServiceImpl(licenseManager);
    }

    @Test
    void testGetLicenses() {
        final LicensedApplications licensedApplications = mock(LicensedApplications.class);
        doReturn(Collections.singleton(ApplicationKey.valueOf("jira"))).when(licensedApplications).getKeys();

        final LicenseDetails licenseDetails = mock(LicenseDetails.class);
        doReturn(licensedApplications).when(licenseDetails).getLicensedApplications();
        doReturn(TESTING).when(licenseDetails).getLicenseType();
        doReturn(Collections.singletonList(licenseDetails)).when(licenseManager).getLicenses();

        final List<LicenseModel> licenseModels = licensesService.getLicenses();
        assertEquals(1, licenseModels.size());

        final LicenseModel licenseModel = licenseModels.iterator().next();
        assertNotNull(licenseModel);
    }

    @Test
    void testSetLicenses() {
        final LicensesServiceImpl spy = spy(licensesService);
        doReturn(Collections.emptyList()).when(spy).getLicenses();

        final List<String> licenses = List.of("ABC...", "DEF...");
        spy.setLicenses(licenses);
        // make sure that license is added
        verify(licenseManager).setLicenses(licenses);
        // Make sure that licenses are returned from getter
        verify(spy).getLicenses();
    }

    @Test
    void testAddLicense() {
        final LicensedApplications licensedApplications = mock(LicensedApplications.class);
        doReturn(Collections.singleton(ApplicationKey.valueOf("jira"))).when(licensedApplications).getKeys();

        final LicenseDetails licenseDetails = mock(LicenseDetails.class);
        doReturn(licensedApplications).when(licenseDetails).getLicensedApplications();
        doReturn(TESTING).when(licenseDetails).getLicenseType();

        final LicensesServiceImpl spy = spy(licensesService);
        doReturn(licenseDetails).when(licenseManager).setLicense(any());

        spy.addLicense("ABC...");
        // make sure that license is added
        verify(licenseManager).setLicense(any());
    }

}
