package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;

import java.util.List;

public interface AuthenticationService<IB extends AbstractAuthenticationIdpBean, SB extends AuthenticationSsoBean> {

    List<IB> getAuthenticationIdps();

    List<IB> setAuthenticationIdps(
            List<IB> authenticationIdpBeans);

    IB setAuthenticationIdp(
            IB authenticationIdpBean);

    SB getAuthenticationSso();

    SB setAuthenticationSso(
            SB authenticationSsoBean);

}
