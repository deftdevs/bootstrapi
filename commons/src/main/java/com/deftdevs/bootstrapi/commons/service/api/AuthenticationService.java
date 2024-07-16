package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;

import java.util.List;

public interface AuthenticationService {

    List<AbstractAuthenticationIdpBean> getAuthenticationIdps();

    List<AbstractAuthenticationIdpBean> setAuthenticationIdps(
            List<AbstractAuthenticationIdpBean> authenticationIdpBeans);

    AbstractAuthenticationIdpBean setAuthenticationIdp(
            AbstractAuthenticationIdpBean authenticationIdpBean);

    AuthenticationSsoBean getAuthenticationSso();

    AuthenticationSsoBean setAuthenticationSso(
            AuthenticationSsoBean authenticationSsoBean);

}
