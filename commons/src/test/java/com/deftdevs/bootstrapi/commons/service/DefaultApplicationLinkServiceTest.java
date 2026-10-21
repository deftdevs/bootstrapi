package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.TypeNotInstalledException;
import com.atlassian.applinks.core.ApplinkStatus;
import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.core.DefaultApplinkStatus;
import com.atlassian.applinks.internal.common.exception.ConsumerInformationUnavailableException;
import com.atlassian.applinks.internal.common.exception.NoAccessException;
import com.atlassian.applinks.internal.common.exception.NoSuchApplinkException;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.internal.status.error.SimpleApplinkError;
import com.atlassian.applinks.internal.status.oauth.ApplinkOAuthStatus;
import com.atlassian.applinks.spi.application.ApplicationIdUtil;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.util.TypeAccessor;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel;
import com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkType;
import com.deftdevs.bootstrapi.commons.model.util.ApplicationLinkModelUtil;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationLink;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.atlassian.applinks.internal.common.status.oauth.OAuthConfig.createDefaultOAuthConfig;
import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.AUTH_LEVEL_MISMATCH;
import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.CONNECTION_REFUSED;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.AVAILABLE;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkStatus.CONFIGURATION_ERROR;
import static com.deftdevs.bootstrapi.commons.model.ApplicationLinkModel.ApplicationLinkType.CROWD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        final Map<String, ApplicationLinkModel> applicationLinkModels = applicationLinkService.getApplicationLinks();
        final ApplicationLinkModel applicationLinkModel = ApplicationLinkModelUtil.toApplicationLinkModel(applicationLink);
        applicationLinkModel.setOutgoingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH);
        applicationLinkModel.setIncomingAuthType(ApplicationLinkModel.ApplicationLinkAuthType.OAUTH);
        applicationLinkModel.setStatus(AVAILABLE);
        assertEquals(applicationLinkModels.values().iterator().next(), applicationLinkModel);
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
    void testSetApplicationLinksNullModelMissingLinkSkipsEntry() {
        doReturn(Collections.emptyList()).when(mutatingApplicationLinkService).getApplicationLinks();
        final Map<String, ApplicationLinkModel> applicationLinkModels = Collections.singletonMap("missing-link", null);
        assertTrue(applicationLinkService.setApplicationLinks(applicationLinkModels).isEmpty());
    }

    @Test
    void testSetApplicationLinksNullNameUsesMapKey()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        final ApplicationLink applicationLink = createApplicationLink();
        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        final String mapKey = applicationLinkModel.getName();
        applicationLinkModel.setName(null);
        final Map<String, ApplicationLinkModel> applicationLinkModels = Collections.singletonMap(mapKey, applicationLinkModel);
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        applicationLinkService.setApplicationLinks(applicationLinkModels);
        assertEquals(mapKey, applicationLinkModel.getName());
    }

    @Test
    void testSetApplicationLinks()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        final ApplicationLink applicationLink = createApplicationLink();
        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        final Map<String, ApplicationLinkModel> applicationLinkModels = Collections.singletonMap(applicationLinkModel.getName(), applicationLinkModel);
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        final Map<String, ApplicationLinkModel> responseApplicationLinkModels = applicationLinkService.setApplicationLinks(applicationLinkModels);
        assertEquals(responseApplicationLinkModels.values().iterator().next().getName(), applicationLinkModel.getName());
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

        ApplicationLinkModel applicationLinkResponse = applicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkModel);
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

        final ApplicationLinkModel applicationLinkResponse = spyApplicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkModel);
        assertEquals(applicationLinkModel.getName(), applicationLinkResponse.getName());
    }

    @Test
    void testSetApplicationLinkNotFound() throws TypeNotInstalledException {
        doReturn(null).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());

        assertThrows(NotFoundException.class, () -> {
            applicationLinkService.setApplicationLink(UUID.randomUUID(), createApplicationLinkModel());
        });
    }

    @Test
    void testSetApplicationLinkRestoresOriginalLinkWhenRecreationFails() throws URISyntaxException, TypeNotInstalledException {
        final ApplicationLink applicationLink = createApplicationLink();
        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        applicationLinkModel.setName("updated-name");
        final OAuthConfig outgoingOAuthConfig = OAuthConfig.createDefaultOAuthConfig();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(outgoingOAuthConfig).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doThrow(new RuntimeException("recreation failed")).doReturn(applicationLink)
                .when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());

        assertThrows(BadRequestException.class, () -> {
            applicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkModel);
        });

        final ArgumentCaptor<ApplicationLinkDetails> detailsCaptor = ArgumentCaptor.forClass(ApplicationLinkDetails.class);
        verify(mutatingApplicationLinkService, times(2)).addApplicationLink(any(), any(), detailsCaptor.capture());
        assertEquals(applicationLinkModel.getName(), detailsCaptor.getAllValues().get(0).getName());
        assertEquals(applicationLink.getName(), detailsCaptor.getAllValues().get(1).getName());

        // the restored link must also get its original outgoing auth configuration back
        verify(applicationLinksAuthConfigHelper).setOutgoingOAuthConfig(applicationLink, outgoingOAuthConfig);
    }

    @Test
    void testSetApplicationLinkDoesNotPurgeLinkWithUninstalledType() throws URISyntaxException, TypeNotInstalledException {
        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        doThrow(new TypeNotInstalledException("jira")).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());

        assertThrows(BadRequestException.class, () -> {
            applicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkModel);
        });

        verify(mutatingApplicationLinkService, never()).deleteApplicationLink(any());
    }

    @Test
    void testAddApplicationLinkDoesNotPurgeLinkWithUninstalledType() throws Exception {
        final ApplicationLink applicationLink = createApplicationLink();
        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        doThrow(new TypeNotInstalledException("jira")).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        applicationLinkService.addApplicationLink(applicationLinkModel);

        verify(mutatingApplicationLinkService, never()).deleteApplicationLink(any());
    }

    @Test
    void testSetApplicationLinkRecreatesCorruptedLink()
            throws URISyntaxException, TypeNotInstalledException, NoAccessException, NoSuchApplinkException {
        final ApplicationLink applicationLink = createApplicationLink();
        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        final UUID uuid = UUID.randomUUID();
        doThrow(new TypeNotInstalledException("unknown")).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        final ApplicationLinkModel applicationLinkResponse = applicationLinkService.setApplicationLink(uuid, applicationLinkModel);

        assertEquals(applicationLinkModel.getName(), applicationLinkResponse.getName());
        final ArgumentCaptor<ApplicationLink> deletedLinkCaptor = ArgumentCaptor.forClass(ApplicationLink.class);
        verify(mutatingApplicationLinkService).deleteApplicationLink(deletedLinkCaptor.capture());
        assertEquals(uuid.toString(), deletedLinkCaptor.getValue().getId().get());
    }

    @Test
    void testAddApplicationLinkRemovesCorruptedLinkWithSameGeneratedId() throws Exception {
        final ApplicationLink applicationLink = createApplicationLink();
        final ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        doThrow(new TypeNotInstalledException("unknown")).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        final ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel);

        assertEquals(applicationLinkModel.getName(), applicationLinkResponse.getName());
        final ArgumentCaptor<ApplicationLink> deletedLinkCaptor = ArgumentCaptor.forClass(ApplicationLink.class);
        verify(mutatingApplicationLinkService).deleteApplicationLink(deletedLinkCaptor.capture());
        assertEquals(ApplicationIdUtil.generate(applicationLinkModel.getRpcUrl()), deletedLinkCaptor.getValue().getId());
    }

    @Test
    void testDeleteApplicationLinkNotFound() throws TypeNotInstalledException {
        doReturn(null).when(mutatingApplicationLinkService).getApplicationLink(any());

        assertThrows(NotFoundException.class, () -> {
            applicationLinkService.deleteApplicationLink(UUID.randomUUID());
        });
    }

    @Test
    void testDeleteApplicationLinkCorrupted() throws TypeNotInstalledException {
        final UUID uuid = UUID.randomUUID();
        doThrow(new TypeNotInstalledException("unknown")).when(mutatingApplicationLinkService).getApplicationLink(any());

        applicationLinkService.deleteApplicationLink(uuid);

        final ArgumentCaptor<ApplicationLink> deletedLinkCaptor = ArgumentCaptor.forClass(ApplicationLink.class);
        verify(mutatingApplicationLinkService).deleteApplicationLink(deletedLinkCaptor.capture());
        assertEquals(uuid.toString(), deletedLinkCaptor.getValue().getId().get());
    }

    @Test
    void testBuildApplicationTypeNotInstalled() {
        doReturn(null).when(typeAccessor).getApplicationType(any());

        assertThrows(BadRequestException.class, () -> {
            applicationLinkService.buildApplicationType(CROWD);
        });
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

        ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel);

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

        ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel);

        assertEquals(applicationLinkResponse.getName(), applicationLinkModel.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkModel);
    }

    @Test
    void testAddApplicationLinkWithAuthenticatorErrorIgnored() throws Exception {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();
        applicationLinkModel.setIgnoreSetupErrors(true);

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doThrow(new ConsumerInformationUnavailableException("")).when(applicationLinksAuthConfigHelper).setIncomingOAuthConfig(any(), any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getOutgoingOAuthConfig(any());
        doReturn(OAuthConfig.createDisabledConfig()).when(applicationLinksAuthConfigHelper).getIncomingOAuthConfig(any());
        doReturn(createApplinkStatus(applicationLink, CONFIGURATION_ERROR)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel);

        assertEquals(applicationLinkResponse.getName(), applicationLinkModel.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkModel);
    }

    @Test
    void testAddApplicationLinkWithAuthenticatorErrorNotIgnored() throws Exception {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkModel applicationLinkModel = createApplicationLinkModel();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doThrow(new ConsumerInformationUnavailableException("")).when(applicationLinksAuthConfigHelper).setIncomingOAuthConfig(any(), any());

        assertThrows(BadRequestException.class, () -> {
            applicationLinkService.addApplicationLink(applicationLinkModel);
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

            ApplicationLinkModel applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkModel);

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
