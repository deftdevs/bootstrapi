package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestLicenseResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class LicenseResourceTest {

    @Mock
    private LicensesService licensesService;

    private TestLicenseResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestLicenseResourceImpl(licensesService);
    }

    @Test
    void testAddLicense() {
        final String licenseKey = "ABC...";
        final LicenseModel licenseModel = LicenseModel.EXAMPLE_1;
        doReturn(licenseModel).when(licensesService).addLicense(licenseKey);

        final Response response = resource.addLicense(licenseKey);
        assertEquals(200, response.getStatus());

        final LicenseModel responseModel = (LicenseModel) response.getEntity();
        assertEquals(licenseModel, responseModel);
    }
}
