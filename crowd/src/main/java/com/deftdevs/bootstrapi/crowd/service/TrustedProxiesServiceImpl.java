package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.property.PropertyManager;
import com.atlassian.crowd.manager.property.PropertyManagerException;
import com.deftdevs.bootstrapi.crowd.service.api.TrustedProxiesService;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TrustedProxiesServiceImpl implements TrustedProxiesService {

    public static final String SEPARATOR = ",";

    private final PropertyManager propertyManager;

    public TrustedProxiesServiceImpl(
            final PropertyManager propertyManager) {

        this.propertyManager = propertyManager;
    }

    @Override
    public List<String> getTrustedProxies() {
        return getTrustedProxiesInternal()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> setTrustedProxies(
            final List<String> trustedProxies) {

        setTrustedProxiesInternal(trustedProxies);
        return getTrustedProxies();
    }

    @Override
    public List<String> addTrustedProxy(
            final String trustedProxy) {

        final Set<String> trustedProxies = new HashSet<>(getTrustedProxiesInternal());
        final boolean added = trustedProxies.add(trustedProxy);
        if (added) setTrustedProxiesInternal(trustedProxies);
        return getTrustedProxies();
    }

    @Override
    public List<String> removeTrustedProxy(
            final String trustedProxy) {

        final Set<String> trustedProxies = new HashSet<>(getTrustedProxiesInternal());
        final boolean removed = trustedProxies.remove(trustedProxy);
        if (removed) setTrustedProxiesInternal(trustedProxies);
        return getTrustedProxies();
    }

    private Set<String> getTrustedProxiesInternal() {
        try {
            final String trustedProxyServers = this.propertyManager.getTrustedProxyServers();
            if (!StringUtils.isBlank(trustedProxyServers)) {
                final String[] trustedProxyServerStrings = StringUtils.split(trustedProxyServers, SEPARATOR);
                return Set.copyOf(Arrays.asList(trustedProxyServerStrings));
            }
        } catch (PropertyManagerException ignored) {}

        return Collections.emptySet();
    }

    private void setTrustedProxiesInternal(
            final Collection<String> trustedProxies) {

        final String proxies = StringUtils.join(trustedProxies, SEPARATOR);
        propertyManager.setTrustedProxyServers(proxies);
    }

}
