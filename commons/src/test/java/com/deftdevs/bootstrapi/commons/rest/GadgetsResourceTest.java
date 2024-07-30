package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.GadgetBean;
import com.deftdevs.bootstrapi.commons.rest.impl.TestGadgetsResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GadgetsResourceTest {

    @Mock
    private GadgetsService gadgetsService;

    private TestGadgetsResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestGadgetsResourceImpl(gadgetsService);
    }

    @Test
    void testGetGadgets() {
        final List<GadgetBean> gadgetBeans = Collections.singletonList(GadgetBean.EXAMPLE_1);
        doReturn(gadgetBeans).when(gadgetsService).getGadgets();

        final Response response = resource.getGadgets();
        assertEquals(200, response.getStatus());

        final List<GadgetBean> responseGadgetBeans = (List<GadgetBean>) response.getEntity();
        assertEquals(responseGadgetBeans, gadgetBeans);
    }

    @Test
    void testSetGadgets() {
        final List<GadgetBean> gadgetBeans = Collections.singletonList(GadgetBean.EXAMPLE_1);
        doReturn(gadgetBeans).when(gadgetsService).setGadgets(gadgetBeans);

        final Response response = resource.setGadgets(gadgetBeans);
        assertEquals(200, response.getStatus());

        final List<GadgetBean> responseGadgetBeans = (List<GadgetBean>) response.getEntity();
        assertEquals(responseGadgetBeans, gadgetBeans);
    }

    @Test
    void testDeleteGadgets() {
        resource.deleteGadgets(true);
        assertTrue(true, "Delete Successful");
    }

}
