package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.TypeNotInstalledException;
import com.atlassian.applinks.core.ApplinkStatus;
import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.core.DefaultApplinkStatus;
import com.atlassian.applinks.internal.common.exception.NoAccessException;
import com.atlassian.applinks.internal.common.exception.NoSuchApplinkException;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.internal.status.error.SimpleApplinkError;
import com.atlassian.applinks.internal.status.oauth.ApplinkOAuthStatus;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationException;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.manifest.ManifestNotFoundException;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationLink;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationType;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkType;
import com.deftdevs.bootstrapi.commons.model.util.ApplicationLinkBeanUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.atlassian.applinks.internal.common.status.oauth.OAuthConfig.createDefaultOAuthConfig;
import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.AUTH_LEVEL_MISMATCH;
import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.CONNECTION_REFUSED;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkStatus.AVAILABLE;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkStatus.CONFIGURATION_ERROR;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkBean.ApplicationLinkType.CROWD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultApplicationLinkServiceTest {

    @Mock
    private MutatingApplicationLinkService mutatingApplicationLinkService;

    @Mock
    private ApplinkStatusService applinkStatusService;

    @Mock
    private ApplicationLinksAuthConfigHelper applicationLinksAuthConfigHelper;

    @Mock
    private TypeAccessor typeAccessor;

    private DefaultApplicationLinksServiceImpl applicationLinkService;

    @BeforeEach
    public void setup() {
        applicationLinkService = new DefaultApplicationLinksServiceImpl(mutatingApplicationLinkService, applinkStatusService, typeAccessor, applicationLinksAuthConfigHelper);
    }

    @Test
    void testGetApplicationLinks() throws URISyntaxException, NoAccessException, NoSuchApplinkException {
        final ApplicationLink applicationLink = createApplicationLink();
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();
        doReturn(OAuthConfig.createDefaultOAuthConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDefaultOAuthConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        final List<ApplicationLinkBean> applicationLinkBeans = applicationLinkService.getApplicationLinks();
        final ApplicationLinkBean applicationLinkBean = ApplicationLinkBeanUtil.toApplicationLinkBean(applicationLink);
        applicationLinkBean.setOutgoingAuthType(ApplicationLinkBean.ApplicationLinkAuthType.OAUTH);
        applicationLinkBean.setIncomingAuthType(ApplicationLinkBean.ApplicationLinkAuthType.OAUTH);
        applicationLinkBean.setStatus(AVAILABLE);
        assertEquals(applicationLinkBeans.iterator().next(), applicationLinkBean);
    }

    @Test
    void testGetApplicationLink() throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(OAuthConfig.createDefaultOAuthConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDefaultOAuthConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkBean appLinkResponse = applicationLinkService.getApplicationLink(UUID.randomUUID());

        ApplicationLinkBean applicationLinkBean = ApplicationLinkBeanUtil.toApplicationLinkBean(applicationLink);
        applicationLinkBean.setOutgoingAuthType(ApplicationLinkBean.ApplicationLinkAuthType.OAUTH);
        applicationLinkBean.setIncomingAuthType(ApplicationLinkBean.ApplicationLinkAuthType.OAUTH);
        applicationLinkBean.setStatus(AVAILABLE);
        assertEquals(applicationLinkBean, appLinkResponse);
    }

    @Test
    void testSetApplicationLinks()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        final ApplicationLink applicationLink = createApplicationLink();
        final ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();
        final List<ApplicationLinkBean> applicationLinkBeans = Collections.singletonList(createApplicationLinkBean());
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        final List<ApplicationLinkBean> responseApplicationLinkBeans = applicationLinkService.setApplicationLinks(applicationLinkBeans, true);
        assertEquals(responseApplicationLinkBeans.iterator().next().getName(), applicationLinkBean.getName());
    }

    @Test
    void testSetApplicationLink()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkBean applicationLinkResponse = applicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkBean, true);

        assertEquals(applicationLinkBean.getName(), applicationLinkResponse.getName());
    }

    @Test
    void testSetApplicationLinkUpdate()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        final ApplicationLink applicationLink = createApplicationLink();
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());

        final ApplicationLinkBean applicationLinkBean = createApplicationLinkBeanUpdate();
        final DefaultApplicationLinksServiceImpl spyApplicationLinkService = spy(applicationLinkService);
        doReturn(applicationLink.getType()).when(spyApplicationLinkService).buildApplicationType(applicationLinkBean.getType());
        doNothing().when(spyApplicationLinkService).setOutgoingOAuthConfig(any(), any());
        doNothing().when(spyApplicationLinkService).setIncomingOAuthConfig(any(), any(), anyBoolean());

        final ApplicationLinkBean applicationLinkResponse = spyApplicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkBean, true);
        assertEquals(applicationLinkBean.getName(), applicationLinkResponse.getName());
    }

    @Test
    void testAddApplicationLinkWithoutExistingTargetLink()
            throws URISyntaxException, ManifestNotFoundException, NoAccessException, NoSuchApplinkException {

        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkBean applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean, true);

        assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkBean);
    }

    @Test
    void testAddApplicationLinkWithExistingTargetLink() throws URISyntaxException, ManifestNotFoundException, NoAccessException, NoSuchApplinkException {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkBean applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean, true);

        assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkBean);
    }

    @Test
    @Disabled
    void testAddApplicationLinkWithAuthenticatorErrorIgnored() throws URISyntaxException, ManifestNotFoundException, AuthenticationConfigurationException, NoAccessException, NoSuchApplinkException {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doThrow(new AuthenticationConfigurationException("")).when(mutatingApplicationLinkService).configureAuthenticationForApplicationLink(any(), any(), any(), any());
        doReturn(createApplinkStatus(applicationLink, CONFIGURATION_ERROR)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkBean applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean, true);

        assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkBean);
    }

    @Test
    @Disabled
    void testAddApplicationLinkWithAuthenticatorErrorNOTIgnored() throws URISyntaxException, ManifestNotFoundException, AuthenticationConfigurationException {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doThrow(new AuthenticationConfigurationException("")).when(mutatingApplicationLinkService).configureAuthenticationForApplicationLink(any(), any(), any(), any());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            applicationLinkService.addApplicationLink(applicationLinkBean, false);
        });
    }

    @Test
    void testApplicationLinkTypeConverter() throws URISyntaxException, ManifestNotFoundException, NoAccessException, NoSuchApplinkException {
        for (ApplicationLinkType linkType : ApplicationLinkType.values()) {
            ApplicationLink applicationLink = createApplicationLink();
            ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();
            applicationLinkBean.setType(linkType);

            doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                    any(ApplicationType.class), any(ApplicationLinkDetails.class));
            doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
            doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
            doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
            doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

            ApplicationLinkBean applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean, true);

            assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
        }
    }

    @Test
    void testDeleteApplicationLinks() throws URISyntaxException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();

        applicationLinkService.deleteApplicationLinks(true);

        verify(mutatingApplicationLinkService).deleteApplicationLink(any());
    }

    @Test
    void testDeleteApplicationLinksWithoutForceParameter() throws URISyntaxException {
        createApplicationLink();

        Exception exception = assertThrows(BadRequestException.class, () -> {
            applicationLinkService.deleteApplicationLinks(false);
        });
    }

    @Test
    void testDeleteApplicationLink() throws URISyntaxException, TypeNotInstalledException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());

        applicationLinkService.deleteApplicationLink(UUID.randomUUID());

        verify(mutatingApplicationLinkService).deleteApplicationLink(any());
    }

    private ApplicationLinkBean createApplicationLinkBean() throws URISyntaxException {
        ApplicationLinkBean applicationLinkBean = ApplicationLinkBeanUtil.toApplicationLinkBean(createApplicationLink());
        applicationLinkBean.setType(CROWD);
        applicationLinkBean.setOutgoingAuthType(ApplicationLinkBean.ApplicationLinkAuthType.OAUTH);
        applicationLinkBean.setIncomingAuthType(ApplicationLinkBean.ApplicationLinkAuthType.OAUTH);
        return applicationLinkBean;
    }

    private ApplicationLinkBean createApplicationLinkBeanUpdate() throws URISyntaxException {
        ApplicationLinkBean applicationLinkBean = ApplicationLinkBeanUtil.toApplicationLinkBean(createApplicationLink());
        applicationLinkBean.setType(CROWD);
        applicationLinkBean.setOutgoingAuthType(ApplicationLinkBean.ApplicationLinkAuthType.DISABLED);
        applicationLinkBean.setIncomingAuthType(ApplicationLinkBean.ApplicationLinkAuthType.DISABLED);
        return applicationLinkBean;
    }

    private ApplicationLink createApplicationLink() throws URISyntaxException {
        ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        URI uri = new URI("http://localhost");
        return new DefaultApplicationLink(applicationId, new DefaultApplicationType(), "test", uri, uri, false, false);
    }

    private ApplinkStatus createApplinkStatus(ApplicationLink link, ApplicationLinkBean.ApplicationLinkStatus linkStatus) {
        ApplinkOAuthStatus oAuthStatus = new ApplinkOAuthStatus(createDefaultOAuthConfig(), createDefaultOAuthConfig());
        switch (linkStatus) {
            case AVAILABLE:
                return DefaultApplinkStatus.working (link, oAuthStatus, oAuthStatus);
            case UNAVAILABLE:
                return DefaultApplinkStatus.disabled (link, new SimpleApplinkError(CONNECTION_REFUSED));
            case CONFIGURATION_ERROR:
            default:
                return DefaultApplinkStatus.configError (link, oAuthStatus, oAuthStatus, new SimpleApplinkError(AUTH_LEVEL_MISMATCH));
        }
    }
}
