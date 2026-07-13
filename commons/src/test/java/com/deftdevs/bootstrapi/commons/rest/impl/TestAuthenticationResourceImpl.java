package com.deftdevs.bootstrapi.commons.rest.impl;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.rest.AbstractAuthenticationResourceImpl;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

public class TestAuthenticationResourceImpl extends AbstractAuthenticationResourceImpl {

    public TestAuthenticationResourceImpl(
            AuthenticationService authenticationService) {

        super(authenticationService);
    }

}
