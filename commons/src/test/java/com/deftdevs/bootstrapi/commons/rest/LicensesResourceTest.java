package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.LicenseBean;
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
        final List<LicenseBean> licenseBeans = Collections.singletonList(LicenseBean.EXAMPLE_1);
        doReturn(licenseBeans).when(licensesService).getLicenses();

        final Response response = resource.getLicenses();
        assertEquals(200, response.getStatus());

        final List<LicenseBean> resultLicenseBeans = (List<LicenseBean>) response.getEntity();
        assertEquals(resultLicenseBeans, licenseBeans);
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
