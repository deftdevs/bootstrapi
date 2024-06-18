package com.deftdevs.bootstrapi.commons.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationIdpsBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;

public interface AuthenticationService {

    AuthenticationIdpsBean getAuthenticationIdps();

    AuthenticationIdpsBean setAuthenticationIdps(
            AuthenticationIdpsBean authenticationIdpsBean);

    AbstractAuthenticationIdpBean setAuthenticationIdp(
            AbstractAuthenticationIdpBean authenticationIdpBean);

    AuthenticationSsoBean getAuthenticationSso();

    AuthenticationSsoBean setAuthenticationSso(
            AuthenticationSsoBean authenticationSsoBean);

}
