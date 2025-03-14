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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class LicensesServiceTest {

    @Mock
    private LicenseHandler licenseHandler;

    private LicensesServiceImpl licenseService;

    @BeforeEach
    public void setup() {
        licenseService = new LicensesServiceImpl(licenseHandler);
    }

    @Test
    void testGetLicense() {
        final DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(LicenseBean.EXAMPLE_1);
        doReturn(testLicense).when(licenseHandler).getProductLicenseDetails(DEFAULT_LICENSE_REGISTRY_KEY);

        final List<LicenseBean> licenses = licenseService.getLicenses();
        assertEquals(testLicense.getDescription(), licenses.iterator().next().getDescription());
    }

    @Test
    void testAddLicense() {
        LicenseBean licenseBean = LicenseBean.EXAMPLE_1;
        DefaultSingleProductLicenseDetailsView testLicense = new DefaultSingleProductLicenseDetailsView(licenseBean);

        LicenseBean updatedLicenseBean = licenseService.addLicense(licenseBean);

        assertEquals(testLicense.getDescription(), updatedLicenseBean.getDescription());
    }

    @Test
    void testAddLicenseWithError() throws InvalidOperationException {
        LicenseBean licenseBean = LicenseBean.EXAMPLE_1;
        doThrow(new InvalidOperationException("", "")).when(licenseHandler).addProductLicense(any(), any());

        assertThrows(BadRequestException.class, () -> {
            licenseService.addLicense(licenseBean);
        });
    }
}
