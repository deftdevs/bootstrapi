package com.deftdevs.bootstrapi.jira.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpModel;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoModel;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

public interface JiraAuthenticationService extends
        AuthenticationService<AbstractAuthenticationIdpModel, AuthenticationSsoModel> {

}
