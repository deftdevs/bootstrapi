package com.deftdevs.bootstrapi.crowd.service.api;

import java.util.List;

public interface TrustedProxiesService {

    List<String> getTrustedProxies();

    List<String> setTrustedProxies(
            List<String> trustedProxies);

    List<String> addTrustedProxy(
            String trustedProxy);

    List<String> removeTrustedProxy(
            String trustedProxy);

}
