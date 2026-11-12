package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.internal.rest.uniconn.exception.CannotDisableBackboneException;
import com.atlassian.oauth2.client.api.storage.config.ClientConfigStorageService;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.module.ContainerManagedPlugin;
import com.atlassian.sal.api.features.DarkFeatureManager;
import com.atlassian.sal.api.user.UserManager;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.CloudConnectorModel;
import com.deftdevs.bootstrapi.commons.model.CloudConnectorModel.CloudConnectorStatus;
import com.deftdevs.bootstrapi.commons.service.api.CloudConnectorService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Cloud connector service delegating to the applinks plugin.
 * <p>
 * The cloud connector is a so-called backbone application link of the applinks
 * plugin's unified connectivity ("uniconn") feature. Creating and disabling it
 * involves decoding and validating the client credentials, storing an OAuth 2.0
 * client configuration and revoking the connection in the cloud. All of this is
 * implemented by the plugin's {@code BackboneConnectionService}, which is neither
 * exported as an OSGi service nor located in an exported package. To behave
 * exactly like the administration UI, this service looks up the plugin's bean and
 * invokes it reflectively.
 */
public class DefaultCloudConnectorServiceImpl implements CloudConnectorService {

    static final String APPLINKS_PLUGIN_KEY = "com.atlassian.applinks.applinks-plugin";
    static final String BACKBONE_CONNECTION_SERVICE_BEAN = "backboneConnectionService";
    static final String METHOD_GET_PRIMARY = "getPrimaryBackboneApplink";
    static final String METHOD_CREATE_OR_REPLACE = "createOrReplaceApplink";
    static final String METHOD_DISABLE = "disableBackboneConnection";

    // see applinks' ApplinksFeatures.UNICONN
    static final String UNICONN_FEATURE_KEY = "applinks.uniconn";

    // see applinks' UniconnApplinkProperties
    static final String PROPERTY_LINK_ID = "uniConnLinkId";
    static final String PROPERTY_ORG_ID = "uniConnOrgId";
    static final String PROPERTY_ORG_NAME = "uniConnOrgName";
    static final String PROPERTY_CONNECTION_STATUS = "uniConnConnectionStatus";

    private static final String NOT_FOUND_MESSAGE = "No cloud connector is configured";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ClientConfigStorageService clientConfigStorageService;

    private final PluginAccessor pluginAccessor;

    private final DarkFeatureManager darkFeatureManager;

    private final UserManager userManager;

    public DefaultCloudConnectorServiceImpl(
            final ClientConfigStorageService clientConfigStorageService,
            final PluginAccessor pluginAccessor,
            final DarkFeatureManager darkFeatureManager,
            final UserManager userManager) {

        this.clientConfigStorageService = clientConfigStorageService;
        this.pluginAccessor = pluginAccessor;
        this.darkFeatureManager = darkFeatureManager;
        this.userManager = userManager;
    }

    @Override
    public CloudConnectorModel getCloudConnector() {
        validateFeatureEnabled();

        final ApplicationLink backboneApplicationLink = getBackboneApplicationLink();
        if (backboneApplicationLink == null) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        return toCloudConnectorModel(backboneApplicationLink);
    }

    @Override
    public CloudConnectorModel setCloudConnector(
            final CloudConnectorModel cloudConnectorModel) {

        validateFeatureEnabled();

        final String credentials = cloudConnectorModel.getCredentials();
        if (StringUtils.isBlank(credentials)) {
            throw new BadRequestException("The client credentials of the cloud connector must be provided");
        }

        // the plugin always replaces the connector (and its OAuth 2.0 client),
        // so re-applying unchanged credentials must not reach it
        final ApplicationLink existingBackboneApplicationLink = getBackboneApplicationLink();
        if (existingBackboneApplicationLink != null && isSameConnection(existingBackboneApplicationLink, credentials)) {
            return toCloudConnectorModel(existingBackboneApplicationLink);
        }

        final Object result = invokeBackboneConnectionService(METHOD_CREATE_OR_REPLACE, credentials);

        return toCloudConnectorModel((ApplicationLink) result);
    }

    @Override
    public void deleteCloudConnector() {
        validateFeatureEnabled();

        if (getBackboneApplicationLink() == null) {
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }

        invokeBackboneConnectionService(METHOD_DISABLE, null);
    }

    // the backbone application type is exported by applinks for internal access only,
    // so it cannot be used to look up the link with the application link service
    private ApplicationLink getBackboneApplicationLink() {
        return (ApplicationLink) invokeBackboneConnectionService(METHOD_GET_PRIMARY, null);
    }

    /**
     * Mirrors the conditions under which the applinks plugin serves its own
     * cloud connection endpoints. The remote user is also required by the plugin
     * when creating the connector. Without one, it would fail only after having
     * deleted the existing connector.
     */
    private void validateFeatureEnabled() {
        if (userManager.getRemoteUserKey() == null) {
            throw new BadRequestException("The cloud connector can only be configured by an authenticated user");
        }

        if (!darkFeatureManager.isFeatureEnabledForCurrentUser(UNICONN_FEATURE_KEY)) {
            throw new BadRequestException(String.format(
                    "Cloud connections are disabled (dark feature '%s')", UNICONN_FEATURE_KEY));
        }
    }

    /**
     * Checks whether the given credentials are the ones the existing connector
     * was created with: same cloud link, same organization and same OAuth 2.0
     * client. A connector that was disabled or has expired is never the same,
     * so that applying its credentials again recreates it.
     */
    boolean isSameConnection(
            final ApplicationLink backboneApplicationLink,
            final String credentials) {

        final CloudConnectorStatus status = getStatus(backboneApplicationLink);
        if (status == CloudConnectorStatus.DISCONNECTED || status == CloudConnectorStatus.EXPIRED) {
            return false;
        }

        final JsonNode decodedCredentials;
        try {
            final byte[] decoded = Base64.getDecoder().decode(credentials.trim());
            decodedCredentials = OBJECT_MAPPER.readTree(new String(decoded, StandardCharsets.UTF_8));
        } catch (Exception e) {
            // invalid credentials are reported by the plugin
            return false;
        }

        if (decodedCredentials == null
                || !decodedCredentials.path("linkId").asText("").equals(getStringProperty(backboneApplicationLink, PROPERTY_LINK_ID))
                || !decodedCredentials.path("orgId").asText("").equals(getStringProperty(backboneApplicationLink, PROPERTY_ORG_ID))) {
            return false;
        }

        final String clientConfigurationId = backboneApplicationLink.getClientCredentialsClientConfigurationId();
        if (StringUtils.isBlank(clientConfigurationId)) {
            return false;
        }

        final JsonNode oAuth2Credentials = decodedCredentials.path("oAuth2Credentials");
        final String clientId = oAuth2Credentials.path("clientId").asText("");
        final String clientSecret = oAuth2Credentials.path("clientSecret").asText("");

        return clientConfigStorageService.getById(clientConfigurationId)
                .map(clientConfiguration -> clientId.equals(clientConfiguration.getClientId())
                        && clientSecret.equals(clientConfiguration.getClientSecret()))
                .orElse(false);
    }

    /**
     * Invokes the public method with the given name of the plugin's
     * BackboneConnectionService. A method taking one parameter receives the
     * given argument wrapped into its parameter type, which is a request type
     * with a single String constructor ({@code CreateBackboneApplicationLinkRequest}).
     * The request type is not referenced at compile time because it is a Java
     * record, which cannot be compiled against with the release this project
     * targets.
     */
    private Object invokeBackboneConnectionService(
            final String methodName,
            final String argument) {

        final Object backboneConnectionService = getBackboneConnectionService();
        final int parameterCount = argument == null ? 0 : 1;

        Method method = null;
        for (Method candidate : backboneConnectionService.getClass().getMethods()) {
            if (candidate.getName().equals(methodName) && candidate.getParameterCount() == parameterCount) {
                method = candidate;
                break;
            }
        }

        if (method == null) {
            throw new ServiceUnavailableException(String.format(
                    "The installed applinks plugin does not support configuring the cloud connector (method '%s' not found)", methodName));
        }

        final Object[] parameters;
        try {
            parameters = argument == null
                    ? new Object[0]
                    : new Object[] { method.getParameterTypes()[0].getConstructor(String.class).newInstance(argument) };
        } catch (ReflectiveOperationException e) {
            throw new ServiceUnavailableException(String.format(
                    "The installed applinks plugin does not support configuring the cloud connector (parameter of method '%s' not supported)", methodName));
        }

        try {
            return method.invoke(backboneConnectionService, parameters);
        } catch (InvocationTargetException e) {
            throw toWebException(e.getCause());
        } catch (IllegalAccessException e) {
            throw new InternalServerErrorException(e);
        }
    }

    private Object getBackboneConnectionService() {
        final Plugin plugin = pluginAccessor.getEnabledPlugin(APPLINKS_PLUGIN_KEY);
        if (!(plugin instanceof ContainerManagedPlugin)) {
            throw new ServiceUnavailableException(String.format(
                    "The applinks plugin '%s' is not enabled", APPLINKS_PLUGIN_KEY));
        }

        final Object backboneConnectionService;
        try {
            backboneConnectionService = ((ContainerManagedPlugin) plugin).getContainerAccessor().getBean(BACKBONE_CONNECTION_SERVICE_BEAN);
        } catch (RuntimeException e) {
            throw new ServiceUnavailableException(String.format(
                    "The installed applinks plugin does not support configuring the cloud connector (%s)", e.getMessage()));
        }

        if (backboneConnectionService == null) {
            throw new ServiceUnavailableException(String.format(
                    "The installed applinks plugin does not support configuring the cloud connector (bean '%s' not found)",
                    BACKBONE_CONNECTION_SERVICE_BEAN));
        }

        return backboneConnectionService;
    }

    private static RuntimeException toWebException(
            final Throwable cause) {

        if (cause instanceof CannotDisableBackboneException) {
            return new BadRequestException("The cloud connector cannot be disabled while"
                    + " cloud product connections depend on it");
        }

        // the plugin reports invalid credentials, a mismatching base URL or
        // organization and failed creations with localized messages
        if (cause != null && StringUtils.isNotBlank(cause.getMessage())) {
            return new BadRequestException(cause.getMessage());
        }

        return new InternalServerErrorException(cause);
    }

    private static CloudConnectorModel toCloudConnectorModel(
            final ApplicationLink backboneApplicationLink) {

        return CloudConnectorModel.builder()
                .name(backboneApplicationLink.getName())
                .organizationId(getStringProperty(backboneApplicationLink, PROPERTY_ORG_ID))
                .organizationName(getStringProperty(backboneApplicationLink, PROPERTY_ORG_NAME))
                .status(getStatus(backboneApplicationLink))
                .build();
    }

    // like the plugin, a missing or unknown status is treated as CREATED
    private static CloudConnectorStatus getStatus(
            final ApplicationLink backboneApplicationLink) {

        final String status = getStringProperty(backboneApplicationLink, PROPERTY_CONNECTION_STATUS);

        try {
            return StringUtils.isBlank(status) ? CloudConnectorStatus.CREATED : CloudConnectorStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            return CloudConnectorStatus.CREATED;
        }
    }

    private static String getStringProperty(
            final ApplicationLink applicationLink,
            final String key) {

        final Object value = applicationLink.getProperty(key);
        return value != null ? value.toString() : null;
    }

}
