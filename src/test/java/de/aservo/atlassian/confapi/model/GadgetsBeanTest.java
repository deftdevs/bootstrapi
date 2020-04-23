package de.aservo.atlassian.confapi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;

import static de.aservo.atlassian.confapi.model.GadgetBeanTest.GADGET_URL;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GadgetsBeanTest {

    @Test
    public void testBean() {
        final GadgetBean gadgetBean = new GadgetBean();
        gadgetBean.setUrl(GADGET_URL);
        final Collection<GadgetBean> gadgetBeans = Collections.singletonList(gadgetBean);
        final GadgetsBean gadgetsBean = new GadgetsBean(gadgetBeans);

        assertEquals(gadgetBeans, gadgetsBean.getGadgets());
    }

}
