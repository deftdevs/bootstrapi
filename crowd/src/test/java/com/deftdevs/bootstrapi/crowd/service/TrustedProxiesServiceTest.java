package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.deftdevs.bootstrapi.crowd.service.TrustedProxiesServiceImpl.SEPARATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TrustedProxiesServiceTest {

    @Mock
    private PropertyManager propertyManager;

    private TrustedProxiesServiceImpl trustedProxiesService;

    private final HashSet<String> trustedProxies = new HashSet<>();

    @BeforeEach
    public void setup() throws PropertyManagerException {
        // return trustedProxies as comma-separated string when propertyManager.getTrustedProxyServers() is called
        lenient().doAnswer(invocation -> {
            return StringUtils.join(trustedProxies, SEPARATOR);
        }).when(propertyManager).getTrustedProxyServers();

        // set trustedProxies when propertyManager.setTrustedProxyServers() is called
        lenient().doAnswer(invocation -> {
            final String[] trustedProxiesStrings = StringUtils.split((String) invocation.getArguments()[0], SEPARATOR);
            trustedProxies.clear();
            trustedProxies.addAll(Set.copyOf(Arrays.asList(trustedProxiesStrings)));
            return null; // return null since the method is void
        }).when(propertyManager).setTrustedProxyServers(anyString());

        trustedProxiesService = new TrustedProxiesServiceImpl(propertyManager);
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
