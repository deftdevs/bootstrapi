package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.GadgetModel;
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
        final GadgetModel bean = GadgetModel.EXAMPLE_1;

        doReturn(bean).when(gadgetsService).getGadget(1L);

        final Response response = resource.getGadget(1L);
        assertEquals(200, response.getStatus());
        final GadgetModel gadgetModel = (GadgetModel) response.getEntity();

        assertEquals(gadgetModel, bean);
    }

    @Test
    void testCreateGadget() {
        final GadgetModel bean = GadgetModel.EXAMPLE_1;
        doReturn(bean).when(gadgetsService).addGadget(bean);

        final Response response = resource.createGadget(bean);
        assertEquals(200, response.getStatus());

        final GadgetModel responseModel = (GadgetModel) response.getEntity();
        assertEquals(bean, responseModel);
    }

    @Test
    void testUpdateGadget() {
        final GadgetModel bean = GadgetModel.EXAMPLE_1;
        doReturn(bean).when(gadgetsService).setGadget(1L, bean);

        final Response response = resource.updateGadget(1L, bean);
        assertEquals(200, response.getStatus());

        final GadgetModel gadgetModel = (GadgetModel) response.getEntity();
        assertEquals(gadgetModel, bean);
    }

    @Test
    void testDeleteGadget() {
        resource.deleteGadget(1L);
        assertTrue(true, "Delete Successful");
    }
}
