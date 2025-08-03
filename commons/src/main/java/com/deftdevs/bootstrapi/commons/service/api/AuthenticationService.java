package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;

import java.util.Map;

public interface AuthenticationService<IB extends AbstractAuthenticationIdpModel, SB extends AuthenticationSsoModel> {

    Map<String, ? extends IB> getAuthenticationIdps();

    Map<String, ? extends IB> setAuthenticationIdps(
            Map<String, ? extends IB> authenticationIdpModels);

    IB setAuthenticationIdp(
            IB authenticationIdpModel);

    SB getAuthenticationSso();

    SB setAuthenticationSso(
            SB authenticationSsoModel);

}
