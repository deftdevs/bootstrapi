package com.deftdevs.bootstrapi.crowd.rest;

import com.atlassian.plugins.rest.api.security.annotation.SystemAdminOnly;
import com.deftdevs.bootstrapi.crowd.rest.api.TrustedProxiesResource;
import com.deftdevs.bootstrapi.crowd.service.api.TrustedProxiesService;

import jakarta.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@SystemAdminOnly
@Path(TrustedProxiesResource.TRUSTED_PROXIES)
public class TrustedProxiesResourceImpl implements TrustedProxiesResource {

    private final TrustedProxiesService trustedProxiesService;

    @Inject
    public TrustedProxiesResourceImpl(
            final TrustedProxiesService trustedProxiesService) {

        this.trustedProxiesService = trustedProxiesService;
    }

    @Override
    public Response getTrustedProxies() {
        return Response.ok(trustedProxiesService.getTrustedProxies()).build();
    }

    @Override
    public Response setTrustedProxies(List<String> trustedProxies) {
        final List<String> result = trustedProxiesService.setTrustedProxies(trustedProxies);
        return Response.ok(result).build();
    }

    @Override
    public Response addTrustedProxy(String trustedProxy) {
        return Response.ok(trustedProxiesService.addTrustedProxy(trustedProxy)).build();
    }

    @Override
    public Response removeTrustedProxy(String trustedProxy) {
        return Response.ok(trustedProxiesService.removeTrustedProxy(trustedProxy)).build();
    }

}
