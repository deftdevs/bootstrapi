package com.deftdevs.bootstrapi.commons.model.util;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.application.bamboo.BambooApplicationType;
import com.atlassian.applinks.api.application.bitbucket.BitbucketApplicationType;
import com.atlassian.applinks.api.application.confluence.ConfluenceApplicationType;
import com.atlassian.applinks.api.application.crowd.CrowdApplicationType;
import com.atlassian.applinks.api.application.fecru.FishEyeCrucibleApplicationType;
import com.atlassian.applinks.api.application.jira.JiraApplicationType;
import com.atlassian.applinks.api.application.refapp.RefAppApplicationType;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkType;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationLink;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationType;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ApplicationLinkModelUtilTest {

    @Test
    void testToApplicationLinkModel() throws URISyntaxException {
        final ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        final URI displayUri = new URI("http://localhost");
        final URI rpcUri = new URI("http://rpc.example.com");
        final ApplicationLink applicationLink = new DefaultApplicationLink(
                applicationId, new DefaultApplicationType(), "test", displayUri, rpcUri, false, false);
        final ApplicationLinkModel bean = ApplicationLinkModelUtil.toApplicationLinkModel(applicationLink);

        assertNotNull(bean);
        assertEquals(bean.getName(), applicationLink.getName());
        assertEquals(bean.getDisplayUrl(), applicationLink.getDisplayUrl());
        assertEquals(bean.getRpcUrl(), applicationLink.getRpcUrl());
        assertEquals(bean.isPrimary(), applicationLink.isPrimary());
    }

    @Test
    void testToApplicationLinkDetails() throws Exception {
        final ApplicationLinkModel bean = ApplicationLinkModel.EXAMPLE_1;
        final ApplicationLinkDetails linkDetails = ApplicationLinkModelUtil.toApplicationLinkDetails(bean);

        assertNotNull(linkDetails);
        assertEquals(bean.getName(), linkDetails.getName());
        assertEquals(bean.getDisplayUrl(), linkDetails.getDisplayUrl());
        assertEquals(bean.getRpcUrl(), linkDetails.getRpcUrl());
        assertEquals(bean.isPrimary(), linkDetails.isPrimary());
    }

    @Test
    void testToApplicationLinkAuthTypeDisabled() {
        assertEquals(ApplicationLinkModel.ApplicationLinkAuthType.DISABLED,
                ApplicationLinkModelUtil.toApplicationLinkAuthType(OAuthConfig.createDisabledConfig()));
    }

    @Test
    void testToApplicationLinkAuthTypeOAuth() {
        assertEquals(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH,
                ApplicationLinkModelUtil.toApplicationLinkAuthType(OAuthConfig.createDefaultOAuthConfig()));
    }

    @Test
    void testToApplicationLinkAuthTypeOAuthImpersonation() {
        assertEquals(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH_IMPERSONATION,
                ApplicationLinkModelUtil.toApplicationLinkAuthType(OAuthConfig.createOAuthWithImpersonationConfig()));
    }

    @Test
    void testToOAuthConfigDisabled() {
        assertEquals(OAuthConfig.createDisabledConfig(),
                ApplicationLinkModelUtil.toOAuthConfig(ApplicationLinkModel.ApplicationLinkAuthType.DISABLED));
    }

    @Test
    void testToOAuthConfigOAuth() {
        assertEquals(OAuthConfig.createDefaultOAuthConfig(),
                ApplicationLinkModelUtil.toOAuthConfig(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH));
    }

    @Test
    void testToOAuthConfigOAuthWithImpersonation() {
        assertEquals(OAuthConfig.createOAuthWithImpersonationConfig(),
                ApplicationLinkModelUtil.toOAuthConfig(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH_IMPERSONATION));
    }

    @Test
    void testLinkTypeGenerator() throws URISyntaxException {
        for (ApplicationLinkType linkType : ApplicationLinkType.values()) {
            ApplicationType applicationType = null;
            switch (linkType) {
                case BAMBOO:
                    applicationType = mock(BambooApplicationType.class);
                    break;
                case JIRA:
                    applicationType = mock(JiraApplicationType.class);
                    break;
                case BITBUCKET:
                    applicationType = mock(BitbucketApplicationType.class);
                    break;
                case CONFLUENCE:
                    applicationType = mock(ConfluenceApplicationType.class);
                    break;
                case FISHEYE:
                    applicationType = mock(FishEyeCrucibleApplicationType.class);
                    break;
                case CROWD:
                    applicationType = mock(CrowdApplicationType.class);
                    break;
            }
            ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
            URI uri = new URI("http://localhost");
            ApplicationLink applicationLink = new DefaultApplicationLink(
                    applicationId, applicationType, "test", uri, uri, false, false);
            ApplicationLinkModel bean = ApplicationLinkModelUtil.toApplicationLinkModel(applicationLink);
            assertEquals(linkType, bean.getType());
        }
    }

    @Test
    void testNonImplementedLinkTypeGenerator() throws URISyntaxException {
        ApplicationType applicationType = mock(RefAppApplicationType.class);
        ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        URI uri = new URI("http://localhost");
        ApplicationLink applicationLink = new DefaultApplicationLink(
                applicationId, applicationType, "test", uri, uri, false, false);

        assertThrows(NotImplementedException.class, () -> {
            ApplicationLinkModelUtil.toApplicationLinkModel(applicationLink);
        });
    }

}
