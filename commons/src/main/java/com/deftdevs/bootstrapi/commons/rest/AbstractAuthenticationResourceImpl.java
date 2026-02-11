package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.rest.api.AuthenticationResource;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

import java.util.Map;

public abstract class AbstractAuthenticationResourceImpl<IM extends AbstractAuthenticationIdpModel, SM extends AuthenticationSsoModel, S extends AuthenticationService<IM, SM>>
        implements AuthenticationResource<IM, SM> {

    private final S authenticationService;

    protected AbstractAuthenticationResourceImpl(
            final S authenticationService) {

        this.authenticationService = authenticationService;
    }

    @Override
    public Map<String, ? extends IM> getAuthenticationIdps() {
        return authenticationService.getAuthenticationIdps();
    }

    @Override
    public Map<String, ? extends IM> setAuthenticationIdps(
            final Map<String, ? extends IM> authenticationIdpModels) {

        return authenticationService.setAuthenticationIdps(authenticationIdpModels);
    }

    @Override
    public SM getAuthenticationSso() {
        return authenticationService.getAuthenticationSso();
    }

    @Override
    public SM setAuthenticationSso(
            final SM authenticationSsoModel) {

        return authenticationService.setAuthenticationSso(authenticationSsoModel);
    }

}
