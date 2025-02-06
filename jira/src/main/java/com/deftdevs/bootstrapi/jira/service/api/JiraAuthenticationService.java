package com.deftdevs.bootstrapi.jira.service.api;

import com.deftdevs.bootstrapi.commons.model.AbstractAuthenticationIdpBean;
import com.deftdevs.bootstrapi.commons.model.AuthenticationSsoBean;
import com.deftdevs.bootstrapi.commons.service.api.AuthenticationService;

public interface JiraAuthenticationService extends
        AuthenticationService<AbstractAuthenticationIdpBean, AuthenticationSsoBean> {

}
