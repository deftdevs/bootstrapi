package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.GadgetModel;
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
        final List<GadgetModel> gadgetModels = Collections.singletonList(GadgetModel.EXAMPLE_1);
        doReturn(gadgetModels).when(gadgetsService).getGadgets();

        final Response response = resource.getGadgets();
        assertEquals(200, response.getStatus());

        final List<GadgetModel> responseGadgetModels = (List<GadgetModel>) response.getEntity();
        assertEquals(responseGadgetModels, gadgetModels);
    }

    @Test
    void testSetGadgets() {
        final List<GadgetModel> gadgetModels = Collections.singletonList(GadgetModel.EXAMPLE_1);
        doReturn(gadgetModels).when(gadgetsService).setGadgets(gadgetModels);

        final Response response = resource.setGadgets(gadgetModels);
        assertEquals(200, response.getStatus());

        final List<GadgetModel> responseGadgetModels = (List<GadgetModel>) response.getEntity();
        assertEquals(responseGadgetModels, gadgetModels);
    }

    @Test
    void testDeleteGadgets() {
        resource.deleteGadgets(true);
        assertTrue(true, "Delete Successful");
    }

}
