package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.LicenseModel;
import com.deftdevs.bootstrapi.commons.rest.impl.TestLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class LicensesResourceTest {

    @Mock
    private LicensesService licensesService;

    private TestLicensesResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestLicensesResourceImpl(licensesService);
    }

    @Test
    void testGetLicenses() {
        final List<LicenseModel> licenseModels = Collections.singletonList(LicenseModel.EXAMPLE_1);
        doReturn(licenseModels).when(licensesService).getLicenses();

        final Response response = resource.getLicenses();
        assertEquals(200, response.getStatus());

        final List<LicenseModel> resultLicenseModels = (List<LicenseModel>) response.getEntity();
        assertEquals(resultLicenseModels, licenseModels);
    }

    @Test
    void testSetLicenses() {
        final List<String> licenses = List.of("ABC...");
        final List<LicenseModel> licenseModels = List.of(LicenseModel.EXAMPLE_1);
        doReturn(licenseModels).when(licensesService).setLicenses(licenses);

        final Response response = resource.setLicenses(licenses);
        assertEquals(200, response.getStatus());

        final List<LicenseModel> resultLicenseModels = (List<LicenseModel>) response.getEntity();
        assertEquals(licenseModels, resultLicenseModels);
    }
}
