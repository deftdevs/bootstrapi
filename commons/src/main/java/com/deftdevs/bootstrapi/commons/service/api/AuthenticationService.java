package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;

import java.util.List;

public interface AuthenticationService<IB extends AbstractAuthenticationIdpModel, SB extends AuthenticationSsoModel> {

    List<IB> getAuthenticationIdps();

    List<IB> setAuthenticationIdps(
            List<IB> authenticationIdpModels);

    IB setAuthenticationIdp(
            IB authenticationIdpModel);

    SB getAuthenticationSso();

    SB setAuthenticationSso(
            SB authenticationSsoModel);

}
