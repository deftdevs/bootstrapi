package com.deftdevs.bootstrapi.crowd.service;

import com.atlassian.crowd.manager.proxy.TrustedProxyManager;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.crowd.model.TrustedProxiesBean;
import com.deftdevs.bootstrapi.crowd.service.api.TrustedProxiesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.stream.Collectors;

@Component
@ExportAsService(TrustedProxiesService.class)
public class TrustedProxiesServiceImpl implements TrustedProxiesService {

    @ComponentImport
    private final TrustedProxyManager trustedProxyManager;

    @Inject
    public TrustedProxiesServiceImpl(
            final TrustedProxyManager trustedProxyManager) {

        this.trustedProxyManager = trustedProxyManager;
    }

    @Override
    public TrustedProxiesBean getTrustedProxies() {
        return new TrustedProxiesBean(trustedProxyManager.getAddresses()
                .stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    @Override
    public TrustedProxiesBean setTrustedProxies(
            final TrustedProxiesBean trustedProxiesBean) {

        for (String trustedProxy : trustedProxiesBean.getTrustedProxies()) {
            if (!trustedProxyManager.isTrusted(trustedProxy)) {
                trustedProxyManager.addAddress(trustedProxy);
            }
        }

        for (String trustedProxy : trustedProxyManager.getAddresses()) {
            if (!trustedProxiesBean.getTrustedProxies().contains(trustedProxy)) {
                trustedProxyManager.removeAddress(trustedProxy);
            }
        }

        return getTrustedProxies();
    }

    @Override
    public TrustedProxiesBean addTrustedProxy(
            final String trustedProxy) {

        trustedProxyManager.addAddress(trustedProxy);
        return getTrustedProxies();
    }

    @Override
    public TrustedProxiesBean removeTrustedProxy(
            final String trustedProxy) {

        trustedProxyManager.removeAddress(trustedProxy);
        return getTrustedProxies();
    }

}
