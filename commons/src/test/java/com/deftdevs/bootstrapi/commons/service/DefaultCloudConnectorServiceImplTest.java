package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.internal.rest.uniconn.exception.BackboneConnectionException;
import com.atlassian.applinks.internal.rest.uniconn.exception.CannotDisableBackboneException;
import com.atlassian.oauth2.client.api.storage.config.ClientConfigStorageService;
import com.atlassian.oauth2.client.api.storage.config.ClientConfigurationEntity;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.module.ContainerAccessor;
import com.atlassian.plugin.module.ContainerManagedPlugin;
import com.atlassian.sal.api.features.DarkFeatureManager;
import com.atlassian.sal.api.user.UserKey;
import com.atlassian.sal.api.user.UserManager;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.CloudConnectorModel;
import com.deftdevs.bootstrapi.commons.model.CloudConnectorModel.CloudConnectorStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static com.deftdevs.bootstrapi.commons.service.DefaultCloudConnectorServiceImpl.APPLINKS_PLUGIN_KEY;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudConnectorServiceImpl.BACKBONE_CONNECTION_SERVICE_BEAN;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudConnectorServiceImpl.PROPERTY_CONNECTION_STATUS;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudConnectorServiceImpl.PROPERTY_LINK_ID;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudConnectorServiceImpl.PROPERTY_ORG_ID;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudConnectorServiceImpl.PROPERTY_ORG_NAME;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudConnectorServiceImpl.UNICONN_FEATURE_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class DefaultCloudConnectorServiceImplTest {

    private static final String LINK_ID = "link-id";
    private static final String ORG_ID = "org-id";
    private static final String ORG_NAME = "Example Organization";
    private static final String CLIENT_ID = "client-id";
    private static final String CLIENT_SECRET = "client-secret";
    private static final String CLIENT_CONFIGURATION_ID = "client-configuration-id";
    private static final String CONNECTOR_NAME = "Example Connector";

    @Mock
    private ClientConfigStorageService clientConfigStorageService;

    @Mock
    private PluginAccessor pluginAccessor;

    @Mock
    private DarkFeatureManager darkFeatureManager;

    @Mock
    private UserManager userManager;

    @Mock
    private ContainerManagedPlugin applinksPlugin;

    @Mock
    private ContainerAccessor containerAccessor;

    private FakeBackboneConnectionService backboneConnectionService;

    private DefaultCloudConnectorServiceImpl cloudConnectorService;

    @BeforeEach
    void setup() {
        backboneConnectionService = new FakeBackboneConnectionService();
        cloudConnectorService = new DefaultCloudConnectorServiceImpl(
                clientConfigStorageService, pluginAccessor, darkFeatureManager, userManager);

        lenient().doReturn(new UserKey("admin")).when(userManager).getRemoteUserKey();
        lenient().doReturn(true).when(darkFeatureManager).isFeatureEnabledForCurrentUser(UNICONN_FEATURE_KEY);
        lenient().doReturn(applinksPlugin).when(pluginAccessor).getEnabledPlugin(APPLINKS_PLUGIN_KEY);
        lenient().doReturn(containerAccessor).when(applinksPlugin).getContainerAccessor();
        lenient().doReturn(backboneConnectionService).when(containerAccessor).getBean(BACKBONE_CONNECTION_SERVICE_BEAN);
    }

    @Test
    void testGetCloudConnector() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("CONNECTED");
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;

        final CloudConnectorModel cloudConnectorModel = cloudConnectorService.getCloudConnector();

        assertNull(cloudConnectorModel.getCredentials());
        assertEquals(CONNECTOR_NAME, cloudConnectorModel.getName());
        assertEquals(ORG_ID, cloudConnectorModel.getOrganizationId());
        assertEquals(ORG_NAME, cloudConnectorModel.getOrganizationName());
        assertEquals(CloudConnectorStatus.CONNECTED, cloudConnectorModel.getStatus());
    }

    @Test
    void testGetCloudConnectorWithoutStatusIsCreated() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink(null);
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;

        assertEquals(CloudConnectorStatus.CREATED, cloudConnectorService.getCloudConnector().getStatus());
    }

    @Test
    void testGetCloudConnectorNotConfigured() {
        assertThrows(NotFoundException.class, () -> cloudConnectorService.getCloudConnector());
    }

    @Test
    void testGetCloudConnectorWithoutUser() {
        doReturn(null).when(userManager).getRemoteUserKey();

        assertThrows(BadRequestException.class, () -> cloudConnectorService.getCloudConnector());
        verifyNoInteractions(pluginAccessor);
    }

    @Test
    void testGetCloudConnectorFeatureDisabled() {
        doReturn(false).when(darkFeatureManager).isFeatureEnabledForCurrentUser(UNICONN_FEATURE_KEY);

        assertThrows(BadRequestException.class, () -> cloudConnectorService.getCloudConnector());
        verifyNoInteractions(pluginAccessor);
    }

    @Test
    void testSetCloudConnectorCreatesConnector() {
        final ApplicationLink createdApplicationLink = mockBackboneApplicationLink("CREATED");
        backboneConnectionService.createdApplicationLink = createdApplicationLink;
        final String credentials = credentials(CLIENT_SECRET);

        final CloudConnectorModel cloudConnectorModel = cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials).build());

        assertEquals(credentials, backboneConnectionService.request.clientCredentials);
        assertNull(cloudConnectorModel.getCredentials());
        assertEquals(ORG_ID, cloudConnectorModel.getOrganizationId());
        assertEquals(CloudConnectorStatus.CREATED, cloudConnectorModel.getStatus());
    }

    @Test
    void testSetCloudConnectorWithSameCredentialsIsNoOp() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("CONNECTED");
        doReturn(CLIENT_CONFIGURATION_ID).when(backboneApplicationLink).getClientCredentialsClientConfigurationId();
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        mockClientConfiguration(CLIENT_SECRET);

        final CloudConnectorModel cloudConnectorModel = cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build());

        assertNull(backboneConnectionService.request);
        assertEquals(CloudConnectorStatus.CONNECTED, cloudConnectorModel.getStatus());
    }

    @Test
    void testSetCloudConnectorWithRotatedCredentialsReplacesConnector() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("CONNECTED");
        doReturn(CLIENT_CONFIGURATION_ID).when(backboneApplicationLink).getClientCredentialsClientConfigurationId();
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        mockClientConfiguration(CLIENT_SECRET);
        backboneConnectionService.createdApplicationLink = backboneApplicationLink;
        final String rotatedCredentials = credentials("rotated-client-secret");

        cloudConnectorService.setCloudConnector(CloudConnectorModel.builder().credentials(rotatedCredentials).build());

        assertEquals(rotatedCredentials, backboneConnectionService.request.clientCredentials);
    }

    @Test
    void testSetCloudConnectorWithSameCredentialsOfDisconnectedConnectorRecreatesIt() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("DISCONNECTED");
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        backboneConnectionService.createdApplicationLink = backboneApplicationLink;
        final String credentials = credentials(CLIENT_SECRET);

        cloudConnectorService.setCloudConnector(CloudConnectorModel.builder().credentials(credentials).build());

        assertEquals(credentials, backboneConnectionService.request.clientCredentials);
        verifyNoInteractions(clientConfigStorageService);
    }

    @Test
    void testSetCloudConnectorWithUndecodableCredentialsIsPassedToPlugin() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("CONNECTED");
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        backboneConnectionService.createException = new BackboneConnectionException("Invalid credentials");

        final BadRequestException exception = assertThrows(BadRequestException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials("not base64").build()));

        assertEquals("Invalid credentials", exception.getCause().getMessage());
    }

    @Test
    void testSetCloudConnectorWithoutCredentials() {
        assertThrows(BadRequestException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(" ").build()));
        verifyNoInteractions(pluginAccessor);
    }

    @Test
    void testSetCloudConnectorWithoutUser() {
        doReturn(null).when(userManager).getRemoteUserKey();

        assertThrows(BadRequestException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build()));
        verifyNoInteractions(pluginAccessor);
    }

    @Test
    void testSetCloudConnectorApplinksPluginNotEnabled() {
        doReturn(null).when(pluginAccessor).getEnabledPlugin(APPLINKS_PLUGIN_KEY);

        assertThrows(ServiceUnavailableException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build()));
    }

    @Test
    void testSetCloudConnectorApplinksPluginNotContainerManaged() {
        doReturn(mock(Plugin.class)).when(pluginAccessor).getEnabledPlugin(APPLINKS_PLUGIN_KEY);

        assertThrows(ServiceUnavailableException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build()));
    }

    @Test
    void testSetCloudConnectorBackboneConnectionServiceMissing() {
        doThrow(new IllegalStateException("No bean named 'backboneConnectionService' available"))
                .when(containerAccessor).getBean(BACKBONE_CONNECTION_SERVICE_BEAN);

        assertThrows(ServiceUnavailableException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build()));
    }

    @Test
    void testSetCloudConnectorBackboneConnectionServiceIncompatible() {
        doReturn(new Object()).when(containerAccessor).getBean(BACKBONE_CONNECTION_SERVICE_BEAN);

        assertThrows(ServiceUnavailableException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build()));
    }

    @Test
    void testDeleteCloudConnector() {
        final ApplicationLink backboneApplicationLink = mock(ApplicationLink.class);
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;

        cloudConnectorService.deleteCloudConnector();

        assertEquals(1, backboneConnectionService.disableCount);
    }

    @Test
    void testDeleteCloudConnectorNotConfigured() {
        assertThrows(NotFoundException.class, () -> cloudConnectorService.deleteCloudConnector());
        assertEquals(0, backboneConnectionService.disableCount);
    }

    @Test
    void testDeleteCloudConnectorWithDependentConnections() {
        final ApplicationLink backboneApplicationLink = mock(ApplicationLink.class);
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        backboneConnectionService.disableException = new CannotDisableBackboneException();

        assertThrows(BadRequestException.class, () -> cloudConnectorService.deleteCloudConnector());
    }

    @Test
    void testGetCloudConnectorWithUnknownStatusIsCreated() {
        backboneConnectionService.primaryApplicationLink = mockBackboneApplicationLink("UNKNOWN");

        assertEquals(CloudConnectorStatus.CREATED, cloudConnectorService.getCloudConnector().getStatus());
    }

    @Test
    void testSetCloudConnectorWithSameCredentialsOfExpiredConnectorRecreatesIt() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("EXPIRED");
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        backboneConnectionService.createdApplicationLink = backboneApplicationLink;

        cloudConnectorService.setCloudConnector(CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build());

        assertEquals(credentials(CLIENT_SECRET), backboneConnectionService.request.clientCredentials);
    }

    @Test
    void testSetCloudConnectorForOtherOrganizationReplacesConnector() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("CONNECTED");
        doReturn("other-org-id").when(backboneApplicationLink).getProperty(PROPERTY_ORG_ID);
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        backboneConnectionService.createdApplicationLink = backboneApplicationLink;

        cloudConnectorService.setCloudConnector(CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build());

        assertEquals(credentials(CLIENT_SECRET), backboneConnectionService.request.clientCredentials);
        verifyNoInteractions(clientConfigStorageService);
    }

    @Test
    void testSetCloudConnectorWithoutClientConfigurationReplacesConnector() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("CONNECTED");
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        backboneConnectionService.createdApplicationLink = backboneApplicationLink;

        cloudConnectorService.setCloudConnector(CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build());

        assertEquals(credentials(CLIENT_SECRET), backboneConnectionService.request.clientCredentials);
        verifyNoInteractions(clientConfigStorageService);
    }

    @Test
    void testSetCloudConnectorWithOtherClientIdReplacesConnector() {
        final ApplicationLink backboneApplicationLink = mockBackboneApplicationLink("CONNECTED");
        doReturn(CLIENT_CONFIGURATION_ID).when(backboneApplicationLink).getClientCredentialsClientConfigurationId();
        backboneConnectionService.primaryApplicationLink = backboneApplicationLink;
        backboneConnectionService.createdApplicationLink = backboneApplicationLink;
        final ClientConfigurationEntity clientConfiguration = mock(ClientConfigurationEntity.class);
        doReturn("other-client-id").when(clientConfiguration).getClientId();
        doReturn(Optional.of(clientConfiguration)).when(clientConfigStorageService).getById(CLIENT_CONFIGURATION_ID);

        cloudConnectorService.setCloudConnector(CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build());

        assertEquals(credentials(CLIENT_SECRET), backboneConnectionService.request.clientCredentials);
    }

    @Test
    void testSetCloudConnectorPluginErrorWithoutMessage() {
        backboneConnectionService.createException = new IllegalStateException();

        assertThrows(InternalServerErrorException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build()));
    }

    @Test
    void testSetCloudConnectorBackboneConnectionServiceNull() {
        doReturn(null).when(containerAccessor).getBean(BACKBONE_CONNECTION_SERVICE_BEAN);

        assertThrows(ServiceUnavailableException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build()));
    }

    @Test
    void testSetCloudConnectorBackboneConnectionServiceWithUnsupportedRequestType() {
        doReturn(new UnsupportedRequestBackboneConnectionService()).when(containerAccessor).getBean(BACKBONE_CONNECTION_SERVICE_BEAN);

        assertThrows(ServiceUnavailableException.class, () -> cloudConnectorService.setCloudConnector(
                CloudConnectorModel.builder().credentials(credentials(CLIENT_SECRET)).build()));
    }

    private ApplicationLink mockBackboneApplicationLink(
            final String status) {

        final ApplicationLink applicationLink = mock(ApplicationLink.class);
        lenient().doReturn(CONNECTOR_NAME).when(applicationLink).getName();
        lenient().doReturn(LINK_ID).when(applicationLink).getProperty(PROPERTY_LINK_ID);
        lenient().doReturn(ORG_ID).when(applicationLink).getProperty(PROPERTY_ORG_ID);
        lenient().doReturn(ORG_NAME).when(applicationLink).getProperty(PROPERTY_ORG_NAME);
        lenient().doReturn(status).when(applicationLink).getProperty(PROPERTY_CONNECTION_STATUS);
        return applicationLink;
    }

    private void mockClientConfiguration(
            final String clientSecret) {

        final ClientConfigurationEntity clientConfiguration = mock(ClientConfigurationEntity.class);
        lenient().doReturn(CLIENT_ID).when(clientConfiguration).getClientId();
        lenient().doReturn(clientSecret).when(clientConfiguration).getClientSecret();
        doReturn(Optional.of(clientConfiguration)).when(clientConfigStorageService).getById(CLIENT_CONFIGURATION_ID);
    }

    // same format as generated in Atlassian Administration
    private static String credentials(
            final String clientSecret) {

        final String json = "{"
                + "\"linkId\":\"" + LINK_ID + "\","
                + "\"orgId\":\"" + ORG_ID + "\","
                + "\"orgName\":\"" + ORG_NAME + "\","
                + "\"name\":\"" + CONNECTOR_NAME + "\","
                + "\"baseUrl\":\"https://jira.example.com\","
                + "\"oAuth2Credentials\":{"
                + "\"clientId\":\"" + CLIENT_ID + "\","
                + "\"clientSecret\":\"" + clientSecret + "\","
                + "\"tokenEndpoint\":\"https://auth.atlassian.com/oauth/token\","
                + "\"scopes\":[],"
                + "\"clientExpiryMillis\":1893456000000"
                + "}}";

        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * A backbone connection service whose request type has no String constructor.
     */
    public static class UnsupportedRequestBackboneConnectionService {

        public ApplicationLink getPrimaryBackboneApplink() {
            return null;
        }

        public ApplicationLink createOrReplaceApplink(
                final Object request) {

            return null;
        }
    }

    /**
     * Stands in for the applinks plugin's BackboneConnectionService bean, which
     * the service under test invokes reflectively by method name.
     */
    public static class FakeBackboneConnectionService {

        private ApplicationLink primaryApplicationLink;

        private ApplicationLink createdApplicationLink;

        private RuntimeException createException;

        private CannotDisableBackboneException disableException;

        private Request request;

        private int disableCount;

        public ApplicationLink getPrimaryBackboneApplink() {
            return primaryApplicationLink;
        }

        public ApplicationLink createOrReplaceApplink(
                final Request request) {

            this.request = request;

            if (createException != null) {
                throw createException;
            }

            return createdApplicationLink;
        }

        public boolean disableBackboneConnection() throws CannotDisableBackboneException {
            disableCount++;

            if (disableException != null) {
                throw disableException;
            }

            return true;
        }

        /**
         * Like the plugin's CreateBackboneApplicationLinkRequest record.
         */
        public static class Request {

            private final String clientCredentials;

            public Request(
                    final String clientCredentials) {

                this.clientCredentials = clientCredentials;
            }
        }
    }

}
