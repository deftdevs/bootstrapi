package com.deftdevs.bootstrapi.crowd.service.api;

import com.deftdevs.bootstrapi.crowd.model.TrustedProxiesBean;

public interface TrustedProxiesService {

    TrustedProxiesBean getTrustedProxies();

    TrustedProxiesBean setTrustedProxies(
            TrustedProxiesBean trustedProxiesBean);

    TrustedProxiesBean addTrustedProxy(
            String trustedProxy);

    TrustedProxiesBean removeTrustedProxy(
            String trustedProxy);

}
