package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.rest.api.AuthenticationResource;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

import javax.ws.rs.core.Response;
import java.util.List;

public abstract class AbstractAuthenticationResourceImpl<IB extends AbstractAuthenticationIdpModel, SB extends AuthenticationSsoModel, S extends AuthenticationService<IB, SB>>
        implements AuthenticationResource<IB, SB> {

    private final S authenticationService;

    protected AbstractAuthenticationResourceImpl(
            final S authenticationService) {

        this.authenticationService = authenticationService;
    }

    @Override
    public Response getAuthenticationIdps() {
        final List<IB> resultAuthenticationIdpModels = authenticationService.getAuthenticationIdps();
        return Response.ok(resultAuthenticationIdpModels).build();
    }

    @Override
    public Response setAuthenticationIdps(
            final List<IB> authenticationIdpModels) {

        final List<IB> resultAuthenticationIdpModels = authenticationService.setAuthenticationIdps(authenticationIdpModels);
        return Response.ok(resultAuthenticationIdpModels).build();
    }

    @Override
    public Response getAuthenticationSso() {
        final SB resultAuthenticationSsoModel = authenticationService.getAuthenticationSso();
        return Response.ok(resultAuthenticationSsoModel).build();
    }

    @Override
    public Response setAuthenticationSso(
            final SB authenticationSsoModel) {

        final SB resultAuthenticationSsoModel = authenticationService.setAuthenticationSso(authenticationSsoModel);
        return Response.ok(resultAuthenticationSsoModel).build();
    }

}
