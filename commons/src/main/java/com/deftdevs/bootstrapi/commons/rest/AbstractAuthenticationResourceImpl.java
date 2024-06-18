package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpsBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.commons.rest.api.AuthenticationResource;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

import javax.ws.rs.core.Response;

public abstract class AbstractAuthenticationResourceImpl implements AuthenticationResource {

    private final AuthenticationService authenticationService;

    protected AbstractAuthenticationResourceImpl(
            final AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;
    }

    @Override
    public Response getAuthenticationIdps() {
        final AuthenticationIdpsBean resultAuthenticationIdpsBean = authenticationService.getAuthenticationIdps();
        return Response.ok(resultAuthenticationIdpsBean).build();
    }

    @Override
    public Response setAuthenticationIdps(
            final AuthenticationIdpsBean authenticationIdpsBean) {

        final AuthenticationIdpsBean resultAuthenticationIdpsBean = authenticationService.setAuthenticationIdps(authenticationIdpsBean);
        return Response.ok(resultAuthenticationIdpsBean).build();
    }

    @Override
    public Response getAuthenticationSso() {
        final AuthenticationSsoBean resultAuthenticationSsoBean = authenticationService.getAuthenticationSso();
        return Response.ok(resultAuthenticationSsoBean).build();
    }

    @Override
    public Response setAuthenticationSso(
            final AuthenticationSsoBean authenticationSsoBean) {

        final AuthenticationSsoBean resultAuthenticationSsoBean = authenticationService.setAuthenticationSso(authenticationSsoBean);
        return Response.ok(resultAuthenticationSsoBean).build();
    }

}
