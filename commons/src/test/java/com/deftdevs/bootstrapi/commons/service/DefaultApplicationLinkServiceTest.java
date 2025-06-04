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
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationLink;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationType;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkType;
import com.deftdevs.bootstrapi.commons.model.util.ApplicationLinkModelUtil;
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
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.AVAILABLE;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.CONFIGURATION_ERROR;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkType.CROWD;
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

        final List<ApplicationLinkModel> applicationLinkModels = applicationLinkService.getApplicationLinks();
        final ApplicationLinkModel applicationLinkModel = ApplicationLinkModelUtil.toApplicationLinkModel(applicationLink);
        applicationLinkModel.setOutgoingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH);
        applicationLinkModel.setIncomingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH);
        applicationLinkModel.setStatus(AVAILABLE);
        assertEquals(applicationLinkModels.iterator().next(), applicationLinkModel);
    }

    @Test
    void testGetApplicationLink() throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(OAuthConfig.createDefaultOAuthConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDefaultOAuthConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkModel appLinkResponse = applicationLinkService.getApplicationLink(UUID.randomUUID());

        ApplicationLinkModel applicationLinkModel = ApplicationLinkModelUtil.toApplicationLinkModel(applicationLink);
        applicationLinkModel.setOutgoingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH);
        applicationLinkModel.setIncomingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH);
        applicationLinkModel.setStatus(AVAILABLE);
        assertEquals(applicationLinkModel, appLinkResponse);
    }

    @Test
    void testSetApplicationLinks()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        final ApplicationLink applicationLink = createApplicationLink();
        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        final List<ApplicationLinkModel> applicationLinkModels = Collections.singletonList(createApplicationLinkModel());
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        final List<ApplicationLinkModel> responseApplicationLinkModels = applicationLinkService.setApplicationLinks(applicationLinkModels, true);
        assertEquals(responseApplicationLinkModels.iterator().next().getName(), applicationLinkModel.getName());
    }

    @Test
    void testSetApplicationLink()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();

        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkModel applicationLinkResponse = applicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkModel, true);

        assertEquals(applicationLinkModel.getName(), applicationLinkResponse.getName());
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

        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModelUpdate();
        final DefaultApplicationLinksServiceImpl spyApplicationLinkService = spy(applicationLinkService);
        doReturn(applicationLink.getType()).when(spyApplicationLinkService).buildApplicationType(applicationLinkModel.getType());
        doNothing().when(spyApplicationLinkService).setOutgoingOAuthConfig(any(), any());
        doNothing().when(spyApplicationLinkService).setIncomingOAuthConfig(any(), any(), anyBoolean());

        final ApplicationLinkModel applicationLinkResponse = spyApplicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkModel, true);
        assertEquals(applicationLinkModel.getName(), applicationLinkResponse.getName());
    }

    @Test
    void testAddApplicationLinkWithoutExistingTargetLink() throws Exception {

        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel, true);

        assertEquals(applicationLinkResponse.getName(), applicationLinkModel.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkModel);
    }

    @Test
    void testAddApplicationLinkWithExistingTargetLink() throws Exception {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel, true);

        assertEquals(applicationLinkResponse.getName(), applicationLinkModel.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkModel);
    }

    @Test
    @Disabled
    void testAddApplicationLinkWithAuthenticatorErrorIgnored() throws Exception {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doThrow(new AuthenticationConfigurationException("")).when(mutatingApplicationLinkService).configureAuthenticationForApplicationLink(any(), any(), any(), any());
        doReturn(createApplinkStatus(applicationLink, CONFIGURATION_ERROR)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel, true);

        assertEquals(applicationLinkResponse.getName(), applicationLinkModel.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkModel);
    }

    @Test
    @Disabled
    void testAddApplicationLinkWithAuthenticatorErrorNOTIgnored() throws Exception {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doThrow(new AuthenticationConfigurationException("")).when(mutatingApplicationLinkService).configureAuthenticationForApplicationLink(any(), any(), any(), any());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            applicationLinkService.addApplicationLink(applicationLinkModel, false);
        });
    }

    @Test
    void testApplicationLinkTypeConverter() throws Exception {
        for (ApplicationLinkType linkType : ApplicationLinkType.values()) {
            ApplicationLink applicationLink = createApplicationLink();
            ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
            applicationLinkModel.setType(linkType);

            doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                    any(ApplicationType.class), any(ApplicationLinkDetails.class));
            doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
            doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
            doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
            doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

            ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel, true);

            assertEquals(applicationLinkResponse.getName(), applicationLinkModel.getName());
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

    private ApplicationLinkModel createApplicationLinkModel() throws URISyntaxException {
        ApplicationLinkModel applicationLinkModel = ApplicationLinkModelUtil.toApplicationLinkModel(createApplicationLink());
        applicationLinkModel.setType(CROWD);
        applicationLinkModel.setOutgoingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH);
        applicationLinkModel.setIncomingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH);
        return applicationLinkModel;
    }

    private ApplicationLinkModel createApplicationLinkModelUpdate() throws URISyntaxException {
        ApplicationLinkModel applicationLinkModel = ApplicationLinkModelUtil.toApplicationLinkModel(createApplicationLink());
        applicationLinkModel.setType(CROWD);
        applicationLinkModel.setOutgoingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.DISABLED);
        applicationLinkModel.setIncomingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.DISABLED);
        return applicationLinkModel;
    }

    private ApplicationLink createApplicationLink() throws URISyntaxException {
        ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        URI uri = new URI("http://localhost");
        return new DefaultApplicationLink(applicationId, new DefaultApplicationType(), "test", uri, uri, false, false);
    }

    private ApplinkStatus createApplinkStatus(ApplicationLink link, ApplicationLinkModel.ApplicationLinkStatus linkStatus) {
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
