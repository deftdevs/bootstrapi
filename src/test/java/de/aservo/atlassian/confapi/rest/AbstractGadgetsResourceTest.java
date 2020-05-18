package de.aservo.atlassian.confapi.rest;

import de.aservo.atlassian.confapi.model.GadgetBean;
import de.aservo.atlassian.confapi.model.GadgetsBean;
import de.aservo.atlassian.confapi.service.api.GadgetsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class AbstractGadgetsResourceTest {

    @Mock
    private GadgetsService gadgetsService;

    private TestGadgetsResourceImpl resource;

    @Before
    public void setup() {
        resource = new TestGadgetsResourceImpl(gadgetsService);
    }

    @Test
    public void testGetGadgets() {
        final GadgetsBean bean = GadgetsBean.EXAMPLE_1;

        doReturn(bean).when(gadgetsService).getGadgets();

        final Response response = resource.getGadgets();
        assertEquals(200, response.getStatus());
        final GadgetsBean gadgetsBean = (GadgetsBean) response.getEntity();

        assertEquals(gadgetsBean, bean);
    }

    @Test
    public void testSetGadgets() {
        final GadgetsBean bean = GadgetsBean.EXAMPLE_1;

        doReturn(bean).when(gadgetsService).setGadgets(bean);

        final Response response = resource.setGadgets(bean);
        assertEquals(200, response.getStatus());
        final GadgetsBean gadgetsBean = (GadgetsBean) response.getEntity();

        assertEquals(gadgetsBean, bean);
    }

    @Test
    public void testAddGadget() {
        final GadgetsBean beanToReturn = GadgetsBean.EXAMPLE_1;
        final GadgetBean beanArg = beanToReturn.getGadgets().iterator().next();

        doReturn(beanToReturn).when(gadgetsService).addGadget(beanArg);

        final Response response = resource.addGadget(beanArg);
        assertEquals(200, response.getStatus());
        final GadgetsBean gadgetsBean = (GadgetsBean) response.getEntity();

        assertEquals(gadgetsBean, beanToReturn);
    }
}
