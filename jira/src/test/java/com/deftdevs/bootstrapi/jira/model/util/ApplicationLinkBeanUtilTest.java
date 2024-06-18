package com.deftdevs.bootstrapi.jira.model.util;

import atlassian.settings.setup.DefaultApplicationLink;
import atlassian.settings.setup.DefaultApplicationType;
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
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkType;
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
class ApplicationLinkBeanUtilTest {

    @Test
    void testToApplicationLinkBean() throws URISyntaxException {
        final ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        final URI displayUri = new URI("http://localhost");
        final URI rpcUri = new URI("http://rpc.example.com");
        final ApplicationLink applicationLink = new DefaultApplicationLink(
                applicationId, new DefaultApplicationType(), "test", displayUri, rpcUri, false, false);
        final ApplicationLinkBean bean =ApplicationLinkBeanUtil.toApplicationLinkBean(applicationLink);

        assertNotNull(bean);
        assertEquals(bean.getName(), applicationLink.getName());
        assertEquals(bean.getDisplayUrl(), applicationLink.getDisplayUrl());
        assertEquals(bean.getRpcUrl(), applicationLink.getRpcUrl());
        assertEquals(bean.isPrimary(), applicationLink.isPrimary());
    }

    @Test
    void testToApplicationLinkDetails() {
        final ApplicationLinkBean bean = ApplicationLinkBean.EXAMPLE_1;
        final ApplicationLinkDetails linkDetails = ApplicationLinkBeanUtil.toApplicationLinkDetails(bean);

        assertNotNull(linkDetails);
        assertEquals(bean.getName(), linkDetails.getName());
        assertEquals(bean.getDisplayUrl(), linkDetails.getDisplayUrl());
        assertEquals(bean.getRpcUrl(), linkDetails.getRpcUrl());
        assertEquals(bean.isPrimary(), linkDetails.isPrimary());
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
            ApplicationLinkBean bean = ApplicationLinkBeanUtil.toApplicationLinkBean(applicationLink);
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
            ApplicationLinkBeanUtil.toApplicationLinkBean(applicationLink);
        });
    }

}
