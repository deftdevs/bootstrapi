package de.aservo.confapi.crowd.service.api;

import de.aservo.confapi.crowd.model.TrustedProxiesBean;

public interface TrustedProxiesService {

    TrustedProxiesBean getTrustedProxies();

    TrustedProxiesBean setTrustedProxies(
            TrustedProxiesBean trustedProxiesBean);

    TrustedProxiesBean addTrustedProxy(
            String trustedProxy);

    TrustedProxiesBean removeTrustedProxy(
            String trustedProxy);

}
