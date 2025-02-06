package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.commons.rest.api.AuthenticationResource;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

import javax.ws.rs.core.Response;
import java.util.List;

public abstract class AbstractAuthenticationResourceImpl<IB extends AbstractAuthenticationIdpBean, SB extends AuthenticationSsoBean, S extends AuthenticationService<IB, SB>>
        implements AuthenticationResource<IB, SB> {

    private final S authenticationService;

    protected AbstractAuthenticationResourceImpl(
            final S authenticationService) {

        this.authenticationService = authenticationService;
    }

    @Override
    public Response getAuthenticationIdps() {
        final List<IB> resultAuthenticationIdpBeans = authenticationService.getAuthenticationIdps();
        return Response.ok(resultAuthenticationIdpBeans).build();
    }

    @Override
    public Response setAuthenticationIdps(
            final List<IB> authenticationIdpBeans) {

        final List<IB> resultAuthenticationIdpBeans = authenticationService.setAuthenticationIdps(authenticationIdpBeans);
        return Response.ok(resultAuthenticationIdpBeans).build();
    }

    @Override
    public Response getAuthenticationSso() {
        final SB resultAuthenticationSsoBean = authenticationService.getAuthenticationSso();
        return Response.ok(resultAuthenticationSsoBean).build();
    }

    @Override
    public Response setAuthenticationSso(
            final SB authenticationSsoBean) {

        final SB resultAuthenticationSsoBean = authenticationService.setAuthenticationSso(authenticationSsoBean);
        return Response.ok(resultAuthenticationSsoBean).build();
    }

}
