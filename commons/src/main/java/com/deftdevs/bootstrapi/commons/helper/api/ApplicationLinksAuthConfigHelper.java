package com.deftdevs.bootstrapi.commons.helper.api;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.internal.common.exception.ConsumerInformationUnavailableException;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;

public interface ApplicationLinksAuthConfigHelper {

    OAuthConfig getOutgoingOAuthConfig(
            ApplicationLink applicationLink);

    void setOutgoingOAuthConfig(
            ApplicationLink applicationLink,
            OAuthConfig oAuthConfig);

    OAuthConfig getIncomingOAuthConfig(
            ApplicationLink applicationLink);

    void setIncomingOAuthConfig(
            ApplicationLink applicationLink,
            OAuthConfig oAuthConfig) throws ConsumerInformationUnavailableException;

}
