package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.confluence.settings.setup.DefaultSingleProductLicenseDetailsView;
import com.atlassian.sal.api.i18n.InvalidOperationException;
import com.atlassian.sal.api.license.LicenseHandler;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.atlassian.confluence.setup.ConfluenceBootstrapConstants.DEFAULT_LICENSE_REGISTRY_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LicensesServiceTest {

    @Mock
    private LicenseHandler licenseHandler;

    private LicensesServiceImpl licensesService;

    @BeforeEach
    public void setup() {
        licensesService = new LicensesServiceImpl(licenseHandler);
    }

    @Test
    void testGetLicense() {
        final DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(LicenseBean.EXAMPLE_1);
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);

        final List<LicenseBean> licenses = licensesService.getLicenses();
        assertEquals(testLicense.getDescription(), licenses.iterator().next().getDescription());
    }

    @Test
    public void testSetLicenses() {
        final String license1 = "1";
        final String license2 = "2";
        final List<String> licenses = List.of(license1, license2);
        final DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(LicenseBean.EXAMPLE_1);
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(any());

        final LicensesServiceImpl spy = spy(licensesService);
        spy.setLicenses(licenses);
        verify(spy).addLicense(license1);
        verify(spy).addLicense(license2);
    }

    @Test
    void testAddLicense() {
        final LicenseBean licenseBean = LicenseBean.EXAMPLE_1;
        final DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(licenseBean);
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(any());

        final LicenseBean updatedLicenseBean = licensesService.addLicense("ABC...");
        assertEquals(testLicense.getDescription(), updatedLicenseBean.getDescription());
    }

    @Test
    void testAddLicenseWithError() throws InvalidOperationException {
        final LicenseBean licenseBean = LicenseBean.EXAMPLE_1;
        doThrow(new InvalidOperationException("", "")).when(licenseHandler).addProductLicense(any(), any());

        assertThrows(BadRequestException.class, () -> {
            licensesService.addLicense("ABC...");
        });
    }
}
