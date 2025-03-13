package com.deftdevs.bootstrapi.commons.model.util;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.application.bamboo.BambooApplicationType;
import com.atlassian.applinks.api.application.bitbucket.BitbucketApplicationType;
import com.atlassian.applinks.api.application.confluence.ConfluenceApplicationType;
import com.atlassian.applinks.api.application.crowd.CrowdApplicationType;
import com.atlassian.applinks.api.application.fecru.FishEyeCrucibleApplicationType;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkType;
import org.apache.commons.lang3.NotImplementedException;

import javax.validation.constraints.NotNull;
import java.util.UUID;

import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkType.*;

public class ApplicationLinkBeanUtil {

    /**
     * Instantiates a new Application link bean.
     *
     * @param applicationLink the application link
     *
     * @return the application link bean
     */
    @NotNull
    public static ApplicationLinkBean toApplicationLinkBean(
            @NotNull final ApplicationLink applicationLink) {

        final ApplicationLinkBean applicationLinkBean = new ApplicationLinkBean();
        applicationLinkBean.setUuid(UUID.fromString(applicationLink.getId().get()));
        applicationLinkBean.setName(applicationLink.getName());
        applicationLinkBean.setType(getLinkTypeFromAppType(applicationLink.getType()));
        applicationLinkBean.setDisplayUrl(applicationLink.getDisplayUrl());
        applicationLinkBean.setRpcUrl(applicationLink.getRpcUrl());
        applicationLinkBean.setPrimary(applicationLink.isPrimary());
        return applicationLinkBean;
    }

    /**
     * Instantiates a new Application link bean.
     *
     * @param applicationLink the application link
     * @param outgoingOAuthConfig  the outgoing OAuth config
     * @param incomingOAuthConfig  the incoming OAuth config
     *
     * @return the application link bean
     */
    @NotNull
    public static ApplicationLinkBean toApplicationLinkBean(
            @NotNull final ApplicationLink applicationLink,
            @NotNull final OAuthConfig outgoingOAuthConfig,
            @NotNull final OAuthConfig incomingOAuthConfig,
            @NotNull final ApplicationLinkBean.ApplicationLinkStatus applicationLinkStatus) {

        final ApplicationLinkBean applicationLinkBean = toApplicationLinkBean(applicationLink);
        applicationLinkBean.setOutgoingAuthType(toApplicationLinkAuthType(outgoingOAuthConfig));
        applicationLinkBean.setIncomingAuthType(toApplicationLinkAuthType(incomingOAuthConfig));
        applicationLinkBean.setStatus(applicationLinkStatus);
        return applicationLinkBean;
    }

    /**
     * To application link details application link details.
     *
     * @return the application link details
     */
    @NotNull
    public static ApplicationLinkDetails toApplicationLinkDetails(
            @NotNull final ApplicationLinkBean applicationLinkBean) {

        return ApplicationLinkDetails.builder()
                .name(applicationLinkBean.getName())
                .displayUrl(applicationLinkBean.getDisplayUrl())
                .rpcUrl(applicationLinkBean.getRpcUrl())
                .isPrimary(applicationLinkBean.isPrimary())
                .build();
    }

    /**
     * Create application link auth type from OAuth confi
     *
     * @param oAuthConfig application link auth type
     *
     * @return the OAuth config
     */
    @NotNull
    public static ApplicationLinkBean.ApplicationLinkAuthType toApplicationLinkAuthType(
            @NotNull final OAuthConfig oAuthConfig) {

        if (!oAuthConfig.isEnabled()) {
            return ApplicationLinkBean.ApplicationLinkAuthType.DISABLED;
        }

        if (!oAuthConfig.isTwoLoEnabled()) {
            throw new InternalServerErrorException("ThreeLoOnlyConfig is not supported");
        }

        if (!oAuthConfig.isTwoLoImpersonationEnabled()) {
            return ApplicationLinkBean.ApplicationLinkAuthType.OAUTH;
        }

        return ApplicationLinkBean.ApplicationLinkAuthType.OAUTH_IMPERSONATION;
    }

    /**
     * Create OAuth config from an application link auth type
     *
     * @param applicationLinkAuthType application link auth type
     *
     * @return the OAuth config
     */
    @NotNull
    public static OAuthConfig toOAuthConfig(
            @NotNull final ApplicationLinkBean.ApplicationLinkAuthType applicationLinkAuthType) {

        if (applicationLinkAuthType.equals(ApplicationLinkBean.ApplicationLinkAuthType.OAUTH)) {
            return OAuthConfig.createDefaultOAuthConfig();
        }

        if (applicationLinkAuthType.equals(ApplicationLinkBean.ApplicationLinkAuthType.OAUTH_IMPERSONATION)) {
            return OAuthConfig.createOAuthWithImpersonationConfig();
        }

        return OAuthConfig.createDisabledConfig();
    }

    /**
     * Gets the linktype ApplicationLinkTypes enum value.
     *
     * @param type the ApplicationType
     * @return the linktype
     */
    private static ApplicationLinkType getLinkTypeFromAppType(
            @NotNull final ApplicationType type) {

        if (type instanceof BambooApplicationType) {
            return BAMBOO;
        } else if (type instanceof JiraApplicationType) {
            return JIRA;
        } else if (type instanceof BitbucketApplicationType) {
            return BITBUCKET;
        } else if (type instanceof ConfluenceApplicationType) {
            return CONFLUENCE;
        } else if (type instanceof FishEyeCrucibleApplicationType) {
            return FISHEYE;
        } else if (type instanceof CrowdApplicationType) {
            return CROWD;
        } else {
            throw new NotImplementedException("application type '" + type.getClass() + "' not implemented");
        }
    }

    private ApplicationLinkBeanUtil() {
    }

}
