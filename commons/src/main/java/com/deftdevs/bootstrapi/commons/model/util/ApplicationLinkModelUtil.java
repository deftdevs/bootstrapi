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
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkType;
import org.apache.commons.lang3.NotImplementedException;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkType.*;

public class ApplicationLinkModelUtil {

    /**
     * Instantiates a new Application link bean.
     *
     * @param applicationLink the application link
     *
     * @return the application link bean
     */
    @NotNull
    public static ApplicationLinkModel toApplicationLinkModel(
            final ApplicationLink applicationLink) {

        final ApplicationLinkModel applicationLinkModel = new ApplicationLinkModel();
        applicationLinkModel.setUuid(UUID.fromString(applicationLink.getId().get()));
        applicationLinkModel.setName(applicationLink.getName());
        applicationLinkModel.setType(getLinkTypeFromAppType(applicationLink.getType()));
        applicationLinkModel.setDisplayUrl(applicationLink.getDisplayUrl());
        applicationLinkModel.setRpcUrl(applicationLink.getRpcUrl());
        applicationLinkModel.setPrimary(applicationLink.isPrimary());
        return applicationLinkModel;
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
    public static ApplicationLinkModel toApplicationLinkModel(
            final ApplicationLink applicationLink,
            final OAuthConfig outgoingOAuthConfig,
            final OAuthConfig incomingOAuthConfig,
            final ApplicationLinkModel.ApplicationLinkStatus applicationLinkStatus) {

        final ApplicationLinkModel applicationLinkModel = toApplicationLinkModel(applicationLink);
        applicationLinkModel.setOutgoingAuthType(toApplicationLinkAuthType(outgoingOAuthConfig));
        applicationLinkModel.setIncomingAuthType(toApplicationLinkAuthType(incomingOAuthConfig));
        applicationLinkModel.setStatus(applicationLinkStatus);
        return applicationLinkModel;
    }

    /**
     * To application link details application link details.
     *
     * @return the application link details
     */
    @NotNull
    public static ApplicationLinkDetails toApplicationLinkDetails(
            final ApplicationLinkModel applicationLinkModel) {

        return ApplicationLinkDetails.builder()
                .name(applicationLinkModel.getName())
                .displayUrl(applicationLinkModel.getDisplayUrl())
                .rpcUrl(applicationLinkModel.getRpcUrl())
                .isPrimary(applicationLinkModel.isPrimary())
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
    public static ApplicationLinkModel.ApplicationLinkAuthType toApplicationLinkAuthType(
            final OAuthConfig oAuthConfig) {

        if (!oAuthConfig.isEnabled()) {
            return ApplicationLinkModel.ApplicationLinkAuthType.DISABLED;
        }

        if (!oAuthConfig.isTwoLoEnabled()) {
            throw new InternalServerErrorException("ThreeLoOnlyConfig is not supported");
        }

        if (!oAuthConfig.isTwoLoImpersonationEnabled()) {
            return ApplicationLinkModel.ApplicationLinkAuthType.OAUTH;
        }

        return ApplicationLinkModel.ApplicationLinkAuthType.OAUTH_IMPERSONATION;
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
            final ApplicationLinkModel.ApplicationLinkAuthType applicationLinkAuthType) {

        if (applicationLinkAuthType.equals(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH)) {
            return OAuthConfig.createDefaultOAuthConfig();
        }

        if (applicationLinkAuthType.equals(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH_IMPERSONATION)) {
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
            final ApplicationType type) {

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

    private ApplicationLinkModelUtil() {
    }

}
