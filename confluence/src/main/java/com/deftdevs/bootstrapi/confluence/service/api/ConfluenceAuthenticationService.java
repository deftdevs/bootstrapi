package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

public interface ConfluenceAuthenticationService extends
        AuthenticationService<AbstractAuthenticationIdpBean, AuthenticationSsoBean> {

}
