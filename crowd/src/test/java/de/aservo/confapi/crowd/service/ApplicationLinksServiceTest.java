package de.aservo.confapi.crowd.service;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.ApplicationType;
import com.atlassian.applinks.api.TypeNotInstalledException;
import com.atlassian.applinks.core.ApplinkStatus;
import com.atlassian.applinks.core.ApplinkStatusService;
import com.atlassian.applinks.core.DefaultApplinkStatus;
import com.atlassian.applinks.internal.common.exception.NoAccessException;
import com.atlassian.applinks.internal.common.exception.NoSuchApplinkException;
import com.atlassian.applinks.internal.status.error.SimpleApplinkError;
import com.atlassian.applinks.internal.status.oauth.ApplinkOAuthStatus;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationException;
import com.atlassian.applinks.spi.link.ApplicationLinkDetails;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.atlassian.applinks.spi.manifest.ManifestNotFoundException;
import com.atlassian.applinks.spi.util.TypeAccessor;
import de.aservo.confapi.commons.exception.BadRequestException;
import de.aservo.confapi.commons.exception.NotFoundException;
import de.aservo.confapi.commons.model.ApplicationLinkBean;
import de.aservo.confapi.commons.model.ApplicationLinkBean.ApplicationLinkStatus;
import de.aservo.confapi.commons.model.ApplicationLinksBean;
import de.aservo.confapi.crowd.model.DefaultAuthenticationScenario;
import de.aservo.confapi.crowd.model.util.ApplicationLinkBeanUtil;
import de.aservo.confapi.crowd.settings.setup.DefaultApplicationLink;
import de.aservo.confapi.crowd.settings.setup.DefaultApplicationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.UUID;

import static com.atlassian.applinks.internal.common.status.oauth.OAuthConfig.createDefaultOAuthConfig;
import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.AUTH_LEVEL_MISMATCH;
import static com.atlassian.applinks.internal.status.error.ApplinkErrorType.CONNECTION_REFUSED;
import static de.aservo.confapi.commons.model.ApplicationLinkBean.ApplicationLinkStatus.AVAILABLE;
import static de.aservo.confapi.commons.model.ApplicationLinkBean.ApplicationLinkStatus.CONFIGURATION_ERROR;
import static de.aservo.confapi.commons.model.ApplicationLinkBean.ApplicationLinkType.CROWD;
import static de.aservo.confapi.crowd.model.util.ApplicationLinkBeanUtil.toApplicationLinkBean;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLinksServiceTest {

    @Mock
    private MutatingApplicationLinkService mutatingApplicationLinkService;

    @Mock
    private TypeAccessor typeAccessor;

    @Mock
    private ApplinkStatusService applinkStatusService;

    @InjectMocks
    private ApplicationLinksServiceImpl applicationLinkService;

    @Test
    public void testDefaultDefaultAuthenticationScenarioImpl() {
        DefaultAuthenticationScenario defaultAuthenticationScenario = new DefaultAuthenticationScenario();
        assertTrue(defaultAuthenticationScenario.isCommonUserBase());
        assertTrue(defaultAuthenticationScenario.isTrusted());
    }

    @Test
    public void testGetApplicationLinks() throws URISyntaxException, NoAccessException, NoSuchApplinkException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinksBean applicationLinks = applicationLinkService.getApplicationLinks();

        ApplicationLinkBean applicationLinkBean = toApplicationLinkBean(applicationLink);
        applicationLinkBean.setStatus(AVAILABLE);
        assertEquals(applicationLinks.getApplicationLinks().iterator().next(), applicationLinkBean);
    }

    @Test
    public void testGetApplicationLink() throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkBean appLinkResponse = applicationLinkService.getApplicationLink(UUID.randomUUID());

        ApplicationLinkBean applicationLinkBean = ApplicationLinkBeanUtil.toApplicationLinkBean(applicationLink);
        applicationLinkBean.setStatus(AVAILABLE);
        assertEquals(applicationLinkBean, appLinkResponse);
    }

    @Test(expected = BadRequestException.class)
    public void testSetApplicationLinkException() throws TypeNotInstalledException, URISyntaxException {
        doThrow(new TypeNotInstalledException("")).when(mutatingApplicationLinkService).getApplicationLink(any());
        applicationLinkService.setApplicationLink(UUID.randomUUID(), createApplicationLinkBean(), true);
    }

    @Test
    public void testSetApplicationLinks()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();
        ApplicationLinksBean applicationLinksBean = new ApplicationLinksBean(Collections.singletonList(createApplicationLinkBean()));

        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();
        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());

        ApplicationLinksBean applicationLinkResponse = applicationLinkService.setApplicationLinks(applicationLinksBean, true);

        assertEquals(applicationLinkResponse.getApplicationLinks().iterator().next().getName(), applicationLinkBean.getName());
    }

    @Test
    public void testSetApplicationLinksNonExisting()
            throws URISyntaxException, ManifestNotFoundException, NoAccessException, NoSuchApplinkException {

        ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        URI uri = new URI("http://localhostdsadsa");
        ApplicationLink applicationLink_existing = new DefaultApplicationLink(applicationId, new DefaultApplicationType(), "test2", uri, uri, false, false);
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinksBean applicationLinksBean = new ApplicationLinksBean(Collections.singletonList(createApplicationLinkBean()));

        doReturn(Collections.singletonList(applicationLink_existing)).when(mutatingApplicationLinkService).getApplicationLinks();
        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinksServiceImpl spy = spy(applicationLinkService);
        spy.setApplicationLinks(applicationLinksBean, true);
        verify(spy).addApplicationLink(any(),anyBoolean());
    }

    @Test(expected = NotFoundException.class)
    public void testFindApplicationLink() throws TypeNotInstalledException {
        doReturn(null).when(mutatingApplicationLinkService).getApplicationLink(any());
        applicationLinkService.getApplicationLink(UUID.randomUUID());
    }

    @Test
    public void testSetApplicationLink()
            throws URISyntaxException, NoAccessException, NoSuchApplinkException, TypeNotInstalledException {

        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).addApplicationLink(any(), any(), any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());

        ApplicationLinkBean applicationLinkResponse = applicationLinkService.setApplicationLink(UUID.randomUUID(), applicationLinkBean, true);

        assertEquals(applicationLinkBean.getName(), applicationLinkResponse.getName());
    }

    @Test
    public void testDeleteApplicationLinks() throws URISyntaxException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(Collections.singletonList(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();

        applicationLinkService.deleteApplicationLinks(true);
        verify(mutatingApplicationLinkService).deleteApplicationLink(applicationLink);
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteApplicationLinksWithoutForceParameter() {
        applicationLinkService.deleteApplicationLinks(false);
    }

    @Test
    public void testDeleteApplicationLink() throws URISyntaxException, TypeNotInstalledException {
        ApplicationLink applicationLink = createApplicationLink();
        doReturn(applicationLink).when(mutatingApplicationLinkService).getApplicationLink(any());

        applicationLinkService.deleteApplicationLink(UUID.randomUUID());
        verify(mutatingApplicationLinkService).deleteApplicationLink(applicationLink);
    }

    @Test(expected = BadRequestException.class)
    public void testDeleteApplicationLinkException() throws TypeNotInstalledException {
        doThrow(new TypeNotInstalledException("abc")).when(mutatingApplicationLinkService).getApplicationLink(any());
        applicationLinkService.deleteApplicationLink(UUID.randomUUID());
    }

    @Test
    public void testAddApplicationLinkWithoutExistingTargetLink()
            throws URISyntaxException, ManifestNotFoundException, NoAccessException, NoSuchApplinkException {

        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkBean applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean, true);

        assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkBean);
    }

    @Test
    public void testAddApplicationLinkWithExistingTargetLink() throws URISyntaxException, ManifestNotFoundException, NoAccessException, NoSuchApplinkException {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

        ApplicationLinkBean applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean, true);

        assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
        assertNotEquals(applicationLinkResponse, applicationLinkBean);
    }

    @Test
    public void testAddApplicationLinkWithAuthenticatorErrorIgnored() throws URISyntaxException, ManifestNotFoundException, AuthenticationConfigurationException, NoAccessException, NoSuchApplinkException {
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

    @Test(expected = BadRequestException.class)
    public void testAddApplicationLinkWithAuthenticatorErrorNOTIgnored() throws URISyntaxException, ManifestNotFoundException, AuthenticationConfigurationException {
        ApplicationLink applicationLink = createApplicationLink();
        ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();

        doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                any(ApplicationType.class), any(ApplicationLinkDetails.class));
        doReturn(applicationLink).when(mutatingApplicationLinkService).getPrimaryApplicationLink(any());
        doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
        doThrow(new AuthenticationConfigurationException("")).when(mutatingApplicationLinkService).configureAuthenticationForApplicationLink(any(), any(), any(), any());

        applicationLinkService.addApplicationLink(applicationLinkBean, false);
    }

    @Test
    public void testApplicationLinkTypeConverter() throws URISyntaxException, ManifestNotFoundException, NoAccessException, NoSuchApplinkException {
        for (ApplicationLinkBean.ApplicationLinkType linkType : ApplicationLinkBean.ApplicationLinkType.values()) {
            ApplicationLink applicationLink = createApplicationLink();
            ApplicationLinkBean applicationLinkBean = createApplicationLinkBean();
            applicationLinkBean.setType(linkType);

            doReturn(applicationLink).when(mutatingApplicationLinkService).createApplicationLink(
                    any(ApplicationType.class), any(ApplicationLinkDetails.class));
            doReturn(new DefaultApplicationType()).when(typeAccessor).getApplicationType(any());
            doReturn(createApplinkStatus(applicationLink, AVAILABLE)).when(applinkStatusService).getApplinkStatus(any());

            ApplicationLinkBean applicationLinkResponse = applicationLinkService.addApplicationLink(applicationLinkBean, true);

            assertEquals(applicationLinkResponse.getName(), applicationLinkBean.getName());
        }
    }

    private ApplicationLinkBean createApplicationLinkBean() throws URISyntaxException {
        ApplicationLinkBean bean = toApplicationLinkBean(createApplicationLink());
        bean.setType(CROWD);
        bean.setUsername("test");
        bean.setPassword("test");
        return bean;
    }

    private ApplicationLink createApplicationLink() throws URISyntaxException {
        ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        URI uri = new URI("http://localhost");
        return new DefaultApplicationLink(applicationId, new DefaultApplicationType(), "test", uri, uri, false, false);
    }

    private ApplinkStatus createApplinkStatus(ApplicationLink link, ApplicationLinkStatus linkStatus) {
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
