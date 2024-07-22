package com.deftdevs.bootstrapi.crowd.rest;

import com.deftdevs.bootstrapi.crowd.service.api.TrustedProxiesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TrustedProxiesResourceTest {

    @Mock
    private TrustedProxiesService trustedProxiesService;

    private TrustedProxiesResourceImpl trustedProxiesResource;

    private final HashSet<String> trustedProxies = new HashSet<>();

    @BeforeEach
    public void setup() {
        trustedProxiesResource = new TrustedProxiesResourceImpl(trustedProxiesService);
    }

    @Test
    public void testGetTrustedProxies() {
        final List<String> trustedProxies = Collections.singletonList(getExample());
        doReturn(trustedProxies).when(trustedProxiesService).getTrustedProxies();

        final Response response = trustedProxiesResource.getTrustedProxies();
        assertEquals(200, response.getStatus());

        final List<String> responseTrustedProxies = (List<String>) response.getEntity();
        assertEquals(trustedProxies, responseTrustedProxies);
    }

    @Test
    public void testSetTrustedProxies() {
        final List<String> trustedProxies = Collections.singletonList(getExample());
        doReturn(trustedProxies).when(trustedProxiesService).setTrustedProxies(trustedProxies);

        final Response response = trustedProxiesResource.setTrustedProxies(trustedProxies);
        assertEquals(200, response.getStatus());

        final List<String> responseTrustedProxies = (List<String>) response.getEntity();
        assertEquals(trustedProxies, responseTrustedProxies);
    }

    @Test
    public void testAddTrustedProxy() {
        final List<String> trustedProxies = Collections.singletonList(getExample());
        final String trustedProxy = trustedProxies.iterator().next();
        doReturn(trustedProxies).when(trustedProxiesService).addTrustedProxy(trustedProxy);

        final Response response = trustedProxiesResource.addTrustedProxy(trustedProxy);
        assertEquals(200, response.getStatus());

        final List<String> responseTrustedProxies = (List<String>) response.getEntity();
        assertEquals(trustedProxies, responseTrustedProxies);
    }

    @Test
    public void testRemoveTrustedProxy() {
        final String trustedProxy = getExample();
        final List<String> trustedProxies = Collections.emptyList();
        doReturn(trustedProxies).when(trustedProxiesService).removeTrustedProxy(trustedProxy);

        final Response response = trustedProxiesResource.removeTrustedProxy(trustedProxy);
        assertEquals(200, response.getStatus());

        final List<String> responseTrustedProxies = (List<String>) response.getEntity();
        assertTrue(responseTrustedProxies.isEmpty());
    }

    private String getExample() {
        return "0.0.0.0/0";
    }

}
