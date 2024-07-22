package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.proxy.TrustedProxyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
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

        final List<String> trustedProxyStrings = Collections.singletonList(getExample1());
        trustedProxies.addAll(trustedProxyStrings);

        final List<String> trustedProxies = trustedProxiesService.getTrustedProxies();
        assertEquals(trustedProxyStrings, trustedProxies);
    }

    @Test
    public void testSetTrustedProxies() {
        final List<String> trustedProxies = Collections.singletonList(getExample1());
        final List<String> otherTrustedProxies = Arrays.asList("1.2.3.4", "5.6.7.8");
        final List<String> responseTrustedProxies = trustedProxiesService.setTrustedProxies(otherTrustedProxies);
        assertEquals(responseTrustedProxies, otherTrustedProxies);
    }

    @Test
    public void testAddTrustedProxy() {
        final String trustedProxy = getExample1();
        trustedProxies.add(trustedProxy);
        assertEquals(Collections.singleton(trustedProxy), trustedProxies);

        final String otherTrustedProxy = getExample2();
        final List<String> trustedProxies = trustedProxiesService.addTrustedProxy(otherTrustedProxy);
        assertTrue(trustedProxies.contains(trustedProxy));
        assertTrue(trustedProxies.contains(otherTrustedProxy));
    }

    @Test
    public void testRemoveTrustedProxy() {
        final String trustedProxy = getExample1();
        final String otherTrustedProxy = getExample2();
        trustedProxies.add(trustedProxy);
        trustedProxies.add(otherTrustedProxy);
        assertTrue(trustedProxies.contains(trustedProxy));
        assertTrue(trustedProxies.contains(otherTrustedProxy));

        final List<String> trustedProxies = trustedProxiesService.removeTrustedProxy(otherTrustedProxy);
        assertTrue(trustedProxies.contains(trustedProxy));
        assertFalse(trustedProxies.contains(otherTrustedProxy));
    }

    private String getExample1() {
        return "0.0.0.0/0";
    }

    private String getExample2() {
        return "10.10.10.10/10"; // NOSONAR - hardcoded IP only for testing
    }

}
