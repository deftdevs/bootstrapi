package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.proxy.TrustedProxyManager;
import com.deftdevs.bootstrapi.crowd.model.TrustedProxiesBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrustedProxiesServiceTest {

    @Mock
    private TrustedProxyManager trustedProxyManager;

    private TrustedProxiesServiceImpl trustedProxiesService;

    private final HashSet<String> trustedProxies = new HashSet<>();

    @BeforeEach
    public void setup() {
        // run trustedProxies.add() when trustedProxyManager.addAddress() is called
        lenient().doAnswer(invocation -> {
            trustedProxies.add((String) invocation.getArguments()[0]);
            return true;
        }).when(trustedProxyManager).addAddress(anyString());

        // run trustedProxies.remove() when trustedProxyManager.removeAddress() is called
        lenient().doAnswer(invocation -> {
            trustedProxies.remove((String) invocation.getArguments()[0]);
            return null; // return null since the method is void
        }).when(trustedProxyManager).removeAddress(anyString());

        // return trustedProxies when trustedProxyManager.getAddresses() is called
        doReturn(trustedProxies).when(trustedProxyManager).getAddresses();

        trustedProxiesService = new TrustedProxiesServiceImpl(trustedProxyManager);
    }

    @Test
    public void testGetTrustedProxies() {
        assertTrue(trustedProxies.isEmpty());

        final Collection<String> trustedProxyStrings = TrustedProxiesBean.EXAMPLE_1.getTrustedProxies();
        trustedProxies.addAll(trustedProxyStrings);

        final TrustedProxiesBean trustedProxiesBean = trustedProxiesService.getTrustedProxies();
        assertEquals(trustedProxyStrings, trustedProxiesBean.getTrustedProxies());
    }

    @Test
    public void testSetTrustedProxies() {
        final Collection<String> trustedProxyStrings = TrustedProxiesBean.EXAMPLE_1.getTrustedProxies();
        trustedProxies.addAll(trustedProxyStrings);
        assertEquals(new HashSet<>(trustedProxyStrings), trustedProxies);

        final Collection<String> otherTrustedProxyStrings = new HashSet<>();
        otherTrustedProxyStrings.add("1.2.3.4");
        otherTrustedProxyStrings.add("5.6.7.8");
        final TrustedProxiesBean otherTrustedProxiesBean = new TrustedProxiesBean(otherTrustedProxyStrings);

        final TrustedProxiesBean trustedProxiesBean = trustedProxiesService.setTrustedProxies(otherTrustedProxiesBean);
        assertEquals(trustedProxies, new HashSet<>(trustedProxiesBean.getTrustedProxies()));
    }

    @Test
    public void testAddTrustedProxy() {
        final String trustedProxy = TrustedProxiesBean.EXAMPLE_1.getTrustedProxies().iterator().next();
        trustedProxies.add(trustedProxy);
        assertEquals(Collections.singleton(trustedProxy), trustedProxies);

        final String otherTrustedProxy = TrustedProxiesBean.EXAMPLE_2.getTrustedProxies().iterator().next();
        final TrustedProxiesBean trustedProxiesBean = trustedProxiesService.addTrustedProxy(otherTrustedProxy);
        assertTrue(trustedProxiesBean.getTrustedProxies().contains(trustedProxy));
        assertTrue(trustedProxiesBean.getTrustedProxies().contains(otherTrustedProxy));
    }

    @Test
    public void testRemoveTrustedProxy() {
        final String trustedProxy = TrustedProxiesBean.EXAMPLE_1.getTrustedProxies().iterator().next();
        final String otherTrustedProxy = TrustedProxiesBean.EXAMPLE_2.getTrustedProxies().iterator().next();
        trustedProxies.add(trustedProxy);
        trustedProxies.add(otherTrustedProxy);
        assertTrue(trustedProxies.contains(trustedProxy));
        assertTrue(trustedProxies.contains(otherTrustedProxy));

        final TrustedProxiesBean trustedProxiesBean = trustedProxiesService.removeTrustedProxy(otherTrustedProxy);
        assertTrue(trustedProxiesBean.getTrustedProxies().contains(trustedProxy));
        assertFalse(trustedProxiesBean.getTrustedProxies().contains(otherTrustedProxy));
    }

}
