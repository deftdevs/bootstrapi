package de.aservo.confapi.crowd.rest;

import com.sun.jersey.spi.container.ResourceFilters;
import de.aservo.confapi.crowd.filter.SysadminOnlyResourceFilter;
import de.aservo.confapi.crowd.model.TrustedProxiesBean;
import de.aservo.confapi.crowd.rest.api.TrustedProxiesResource;
import de.aservo.confapi.crowd.service.api.TrustedProxiesService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(TrustedProxiesResource.TRUSTED_PROXIES)
@ResourceFilters(SysadminOnlyResourceFilter.class)
@Component
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
    public Response setTrustedProxies(TrustedProxiesBean trustedProxiesBean) {
        final TrustedProxiesBean result = trustedProxiesService.setTrustedProxies(trustedProxiesBean);
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
