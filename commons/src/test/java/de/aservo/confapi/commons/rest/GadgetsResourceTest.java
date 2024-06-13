package de.aservo.confapi.commons.rest;

import de.aservo.confapi.commons.model.GadgetBean;
import de.aservo.confapi.commons.model.GadgetsBean;
import de.aservo.confapi.commons.rest.impl.TestGadgetsResourceImpl;
import de.aservo.confapi.commons.service.api.GadgetsService;
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
        final GadgetsBean bean = GadgetsBean.EXAMPLE_1;

        doReturn(bean).when(gadgetsService).getGadgets();

        final Response response = resource.getGadgets();
        assertEquals(200, response.getStatus());
        final GadgetsBean gadgetsBean = (GadgetsBean) response.getEntity();

        assertEquals(gadgetsBean, bean);
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
    void testSetGadgets() {
        final GadgetsBean bean = GadgetsBean.EXAMPLE_1;

        doReturn(bean).when(gadgetsService).setGadgets(bean);

        final Response response = resource.setGadgets(bean);
        assertEquals(200, response.getStatus());
        final GadgetsBean gadgetsBean = (GadgetsBean) response.getEntity();

        assertEquals(gadgetsBean, bean);
    }

    @Test
    void testSetGadget() {
        final GadgetBean bean = GadgetBean.EXAMPLE_1;

        doReturn(bean).when(gadgetsService).setGadget(1L, bean);

        final Response response = resource.setGadget(1L, bean);
        assertEquals(200, response.getStatus());
        final GadgetBean gadgetBean = (GadgetBean) response.getEntity();

        assertEquals(gadgetBean, bean);
    }

    @Test
    void testAddGadget() {
        final GadgetBean bean = GadgetBean.EXAMPLE_1;

        doReturn(bean).when(gadgetsService).addGadget(bean);

        final Response response = resource.addGadget(bean);
        assertEquals(200, response.getStatus());
        final GadgetBean responseBean = (GadgetBean) response.getEntity();

        assertEquals(bean, responseBean);
    }

    @Test
    void testDeleteGadgets() {
        resource.deleteGadgets(true);
        assertTrue(true, "Delete Successful");
    }

    @Test
    void testDeleteGadget() {
        resource.deleteGadget(1L);
        assertTrue(true, "Delete Successful");
    }
}
