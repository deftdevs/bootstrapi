package de.aservo.confapi.crowd.rest;

import de.aservo.confapi.crowd.model.TrustedProxiesBean;
import de.aservo.confapi.crowd.service.api.TrustedProxiesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TrustedProxiesResourceTest {

    @Mock
    private TrustedProxiesService trustedProxiesService;

    private TrustedProxiesResourceImpl trustedProxiesResource;

    private final HashSet<String> trustedProxies = new HashSet<>();

    @Before
    public void setup() {
        trustedProxiesResource = new TrustedProxiesResourceImpl(trustedProxiesService);
    }

    @Test
    public void testGetTrustedProxies() {
        final TrustedProxiesBean trustedProxiesBean = TrustedProxiesBean.EXAMPLE_1;
        doReturn(trustedProxiesBean).when(trustedProxiesService).getTrustedProxies();

        final Response response = trustedProxiesResource.getTrustedProxies();
        assertEquals(200, response.getStatus());

        final TrustedProxiesBean responseTrustedProxiesBean = (TrustedProxiesBean) response.getEntity();
        assertEquals(trustedProxiesBean, responseTrustedProxiesBean);
    }

    @Test
    public void testSetTrustedProxies() {
        final TrustedProxiesBean trustedProxiesBean = TrustedProxiesBean.EXAMPLE_1;
        doReturn(trustedProxiesBean).when(trustedProxiesService).setTrustedProxies(trustedProxiesBean);

        final Response response = trustedProxiesResource.setTrustedProxies(TrustedProxiesBean.EXAMPLE_1);
        assertEquals(200, response.getStatus());

        final TrustedProxiesBean responseTrustedProxiesBean = (TrustedProxiesBean) response.getEntity();
        assertEquals(trustedProxiesBean, responseTrustedProxiesBean);
    }

    @Test
    public void testAddTrustedProxy() {
        final TrustedProxiesBean trustedProxiesBean = TrustedProxiesBean.EXAMPLE_1;
        final String trustedProxy = trustedProxiesBean.getTrustedProxies().iterator().next();
        doReturn(trustedProxiesBean).when(trustedProxiesService).addTrustedProxy(trustedProxy);

        final Response response = trustedProxiesResource.addTrustedProxy(trustedProxy);
        assertEquals(200, response.getStatus());

        final TrustedProxiesBean responseTrustedProxiesBean = (TrustedProxiesBean) response.getEntity();
        assertEquals(trustedProxiesBean, responseTrustedProxiesBean);
    }

    @Test
    public void testRemoveTrustedProxy() {
        final String trustedProxy = TrustedProxiesBean.EXAMPLE_1.getTrustedProxies().iterator().next();
        final TrustedProxiesBean trustedProxiesBean = new TrustedProxiesBean(Collections.emptySet());
        doReturn(trustedProxiesBean).when(trustedProxiesService).removeTrustedProxy(trustedProxy);

        final Response response = trustedProxiesResource.removeTrustedProxy(trustedProxy);
        assertEquals(200, response.getStatus());

        final TrustedProxiesBean responseTrustedProxiesBean = (TrustedProxiesBean) response.getEntity();
        assertTrue(responseTrustedProxiesBean.getTrustedProxies().isEmpty());
    }

}
