package com.deftdevs.bootstrapi.commons.rest;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.rest.api.AuthenticationResource;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

import java.util.Map;

public abstract class AbstractAuthenticationResourceImpl implements AuthenticationResource {

    private final AuthenticationService authenticationService;

    protected AbstractAuthenticationResourceImpl(
            final AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;
    }

    @Override
    public Map<String, ? extends AbstractAuthenticationIdpModel> getAuthenticationIdps() {
        return authenticationService.getAuthenticationIdps();
    }

    @Override
    public Map<String, ? extends AbstractAuthenticationIdpModel> setAuthenticationIdps(
            final Map<String, ? extends AbstractAuthenticationIdpModel> authenticationIdpModels) {

        return authenticationService.setAuthenticationIdps(authenticationIdpModels);
    }

    @Override
    public AuthenticationSsoModel getAuthenticationSso() {
        return authenticationService.getAuthenticationSso();
    }

    @Override
    public AuthenticationSsoModel setAuthenticationSso(
            final AuthenticationSsoModel authenticationSsoModel) {

        return authenticationService.setAuthenticationSso(authenticationSsoModel);
    }

}
