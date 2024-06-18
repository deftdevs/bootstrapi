package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.LicenseBean;
import com.deftdevs.bootstrapi.commons.model.LicensesBean;
import com.deftdevs.bootstrapi.commons.rest.impl.TestLicensesResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.LicensesService;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        final LicensesBean bean = LicensesBean.EXAMPLE_1;

        doReturn(bean).when(licensesService).getLicenses();

        final Response response = resource.getLicenses();
        assertEquals(200, response.getStatus());
        final LicensesBean licensesBean = (LicensesBean) response.getEntity();

        assertEquals(licensesBean, bean);
    }

    @Test
    void testAddLicense() {
        final LicenseBean bean = LicenseBean.EXAMPLE_1;

        doReturn(bean).when(licensesService).addLicense(bean);

        final Response response = resource.addLicense(bean);
        assertEquals(200, response.getStatus());
        final LicenseBean responseBean = (LicenseBean) response.getEntity();

        assertEquals(bean, responseBean);
    }
}
