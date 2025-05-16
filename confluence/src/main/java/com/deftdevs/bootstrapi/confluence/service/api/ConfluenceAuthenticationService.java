package com.deftdevs.bootstrapi.confluence.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

public interface ConfluenceAuthenticationService extends
        AuthenticationService<AbstractAuthenticationIdpModel, AuthenticationSsoModel> {

}
