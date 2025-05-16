package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.proxy.TrustedProxyManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.crowd.service.api.TrustedProxiesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrustedProxiesServiceImpl implements TrustedProxiesService {

    @ComponentImport
    private final TrustedProxyManager trustedProxyManager;

    @Inject
    public TrustedProxiesServiceImpl(
            final TrustedProxyManager trustedProxyManager) {

        this.trustedProxyManager = trustedProxyManager;
    }

    @Override
    public List<String> getTrustedProxies() {
        return trustedProxyManager.getAddresses()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> setTrustedProxies(
            final List<String> trustedProxies) {

        for (String trustedProxy : trustedProxies) {
            if (!trustedProxyManager.isTrusted(trustedProxy)) {
                trustedProxyManager.addAddress(trustedProxy);
            }
        }

        for (String trustedProxy : trustedProxyManager.getAddresses()) {
            if (!trustedProxies.contains(trustedProxy)) {
                trustedProxyManager.removeAddress(trustedProxy);
            }
        }

        return getTrustedProxies();
    }

    @Override
    public List<String> addTrustedProxy(
            final String trustedProxy) {

        trustedProxyManager.addAddress(trustedProxy);
        return getTrustedProxies();
    }

    @Override
    public List<String> removeTrustedProxy(
            final String trustedProxy) {

        trustedProxyManager.removeAddress(trustedProxy);
        return getTrustedProxies();
    }

}
