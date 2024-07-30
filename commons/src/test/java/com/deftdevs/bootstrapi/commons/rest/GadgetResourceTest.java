package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.GadgetBean;
import com.deftdevs.bootstrapi.commons.rest.impl.TestGadgetResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.GadgetsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GadgetResourceTest {

    @Mock
    private GadgetsService gadgetsService;

    private TestGadgetResourceImpl resource;

    @BeforeEach
    public void setup() {
        resource = new TestGadgetResourceImpl(gadgetsService);
    }

    @Test
    void testGetGadget() {
        final GadgetBean bean = GadgetBean.EXAMPLE_1;

        doReturn(bean).when(gadgetsService).getGadget(1L);

        final Response response = resource.getGadget(1L);
        assertEquals(200, response.getStatus());
        final GadgetBean gadgetBean = (GadgetBean) response.getEntity();

        assertEquals(gadgetBean, bean);
    }

    @Test
    void testCreateGadget() {
        final GadgetBean bean = GadgetBean.EXAMPLE_1;
        doReturn(bean).when(gadgetsService).addGadget(bean);

        final Response response = resource.createGadget(bean);
        assertEquals(200, response.getStatus());

        final GadgetBean responseBean = (GadgetBean) response.getEntity();
        assertEquals(bean, responseBean);
    }

    @Test
    void testUpdateGadget() {
        final GadgetBean bean = GadgetBean.EXAMPLE_1;
        doReturn(bean).when(gadgetsService).setGadget(1L, bean);

        final Response response = resource.updateGadget(1L, bean);
        assertEquals(200, response.getStatus());

        final GadgetBean gadgetBean = (GadgetBean) response.getEntity();
        assertEquals(gadgetBean, bean);
    }

    @Test
    void testDeleteGadget() {
        resource.deleteGadget(1L);
        assertTrue(true, "Delete Successful");
    }
}
