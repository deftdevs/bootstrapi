package de.aservo.confapi.jira.service;

import com.atlassian.application.api.ApplicationKey;
import com.atlassian.jira.license.JiraLicenseManager;
import com.atlassian.jira.license.LicenseDetails;
import com.atlassian.jira.license.LicensedApplications;
import de.aservo.confapi.commons.model.LicenseBean;
import de.aservo.confapi.commons.model.LicensesBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

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

        final LicensesBean licensesBean = licensesService.getLicenses();
        assertEquals(1, licensesBean.getLicenses().size());

        final LicenseBean licenseBean = licensesBean.getLicenses().iterator().next();
        assertNotNull(licenseBean);
    }

    @Test
    void testSetLicenses() {
        final LicenseBean licenseBean = new LicenseBean();

        final LicensesServiceImpl spy = spy(licensesService);

        final Iterable<? extends LicenseDetails> existingLicenses = Collections.emptyList();
        doReturn(existingLicenses).when(licenseManager).getLicenses();

        spy.setLicenses(new LicensesBean(Collections.singletonList(licenseBean)));

        // make sure that all licenses are deleted first
        verify(licenseManager).removeLicenses(existingLicenses);
        // make sure that license is added
        verify(licenseManager).setLicense(any());
        // Make sure that licenses are returned from getter
        verify(spy).getLicenses();
    }

}
