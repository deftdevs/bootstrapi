package de.aservo.confapi.commons.service.api;

import de.aservo.confapi.commons.model.AbstractAuthenticationIdpBean;
import de.aservo.confapi.commons.model.AuthenticationIdpsBean;
import de.aservo.confapi.commons.model.AuthenticationSsoBean;

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
