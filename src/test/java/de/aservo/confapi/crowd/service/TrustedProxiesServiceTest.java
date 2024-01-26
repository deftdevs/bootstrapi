package de.aservo.confapi.crowd.service;

import com.atlassian.crowd.manager.proxy.TrustedProxyManager;
import de.aservo.confapi.crowd.model.TrustedProxiesBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TrustedProxiesServiceTest {

    @Mock
    private TrustedProxyManager trustedProxyManager;

    private TrustedProxiesServiceImpl trustedProxiesService;

    private final HashSet<String> trustedProxies = new HashSet<>();

    @Before
    public void setup() {
        // run trustedProxies.add() when trustedProxyManager.addAddress() is called
        doAnswer(invocation -> {
            trustedProxies.add((String) invocation.getArguments()[0]);
            return true;
        }).when(trustedProxyManager).addAddress(anyString());

        // run trustedProxies.remove() when trustedProxyManager.removeAddress() is called
        doAnswer(invocation -> {
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
