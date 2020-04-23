package de.aservo.atlassian.confapi.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GadgetBeanTest {

    public static final String GADGET_URL = "http://localhost/gadget";

    @Test
    public void testBean() {
        final GadgetBean gadgetBean = new GadgetBean();
        gadgetBean.setUrl(GADGET_URL);

        assertEquals(GADGET_URL, gadgetBean.getUrl());
    }

}
