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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.atlassian.extras.api.LicenseType.TESTING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void testSetLicensesMapRedactsKeysAndPairsPerKey() {
        // Set up two distinct LicenseDetails so we can verify each map entry's
        // value comes from that key's own addLicense call (not from index-into-getLicenses).
        final LicensedApplications apps = mock(LicensedApplications.class);
        doReturn(Collections.singleton(ApplicationKey.valueOf("jira"))).when(apps).getKeys();
        final LicenseDetails detailsA = mock(LicenseDetails.class);
        doReturn(apps).when(detailsA).getLicensedApplications();
        doReturn(TESTING).when(detailsA).getLicenseType();
        final LicenseDetails detailsB = mock(LicenseDetails.class);
        doReturn(apps).when(detailsB).getLicensedApplications();
        doReturn(TESTING).when(detailsB).getLicenseType();

        doReturn(detailsA).when(licenseManager).setLicense("KEY_A_payload_aaaa");
        doReturn(detailsB).when(licenseManager).setLicense("KEY_B_payload_bbbb");

        final Map<String, LicenseModel> input = new LinkedHashMap<>();
        input.put("KEY_A_payload_aaaa", null);
        input.put("KEY_B_payload_bbbb", null);

        final Map<String, LicenseModel> result = licensesService.setLicenses(input);

        assertEquals(2, result.size());
        // Keys in the response must be redacted (not the original keys).
        assertTrue(result.keySet().stream().allMatch(k -> k.contains("...") && k.contains("#")));
        assertTrue(result.keySet().stream().noneMatch(k -> k.contains("payload")));
        // Each addLicense was called per input key (verifies we aren't relying
        // on JiraLicenseManager.getLicenses() ordering).
        verify(licenseManager).setLicense("KEY_A_payload_aaaa");
        verify(licenseManager).setLicense("KEY_B_payload_bbbb");
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
