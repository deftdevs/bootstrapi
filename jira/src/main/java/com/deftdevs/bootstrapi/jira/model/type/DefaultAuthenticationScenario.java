package com.deftdevs.bootstrapi.jira.model.type;

import com.atlassian.applinks.spi.auth.AuthenticationScenario;

/**
 * The type Default authentication scenario.
 */
public class DefaultAuthenticationScenario implements AuthenticationScenario {

    @Override
    public boolean isCommonUserBase() {
        return true;
    }

    @Override
    public boolean isTrusted() {
        return true;
    }

}
