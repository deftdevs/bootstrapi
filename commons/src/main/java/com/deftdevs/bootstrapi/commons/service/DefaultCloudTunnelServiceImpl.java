package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel;
import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel.CloudTunnelStatus;
import com.deftdevs.bootstrapi.commons.service.api.CloudTunnelService;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * Cloud application tunnel service delegating to the Secure Tunnel Client plugin.
 * <p>
 * The plugin exports its {@code TunnelService} as an OSGi service, but the
 * package of the service is exported for internal access only, so it cannot
 * be imported. This service therefore looks up the OSGi service by its class
 * name and invokes it reflectively. Doing so also keeps BootstrAPI working on
 * instances without the plugin, which is not bundled with every product.
 * <p>
 * Nothing in here contacts the cloud: the plugin stores the tunnel in this
 * instance only, and a tunnel can only be deleted in the cloud.
 */
public class DefaultCloudTunnelServiceImpl implements CloudTunnelService {

    static final String TUNNEL_SERVICE_CLASS = "com.atlassian.tunnel.api.TunnelService";
    static final String METHOD_GET_TUNNEL = "getTunnel";
    static final String METHOD_GET_TUNNEL_STATUS = "getTunnelStatus";
    static final String METHOD_UPDATE_TUNNEL = "updateTunnel";
    static final String METHOD_DELETE_TUNNEL = "deleteTunnel";

    // accessors of the plugin's Tunnel record
    static final String METHOD_NAME = "name";
    static final String METHOD_CLOUD_ORG_NAME = "cloudOrgName";
    static final String METHOD_CLOUD_PAGE_URL = "cloudPageUrl";
    static final String METHOD_UPSTREAM_URL = "upstreamUrl";

    // see applinks' UniconnApplinkProperties and CloudProductConnectionApplicationLinkService
    static final String PROPERTY_UNICONN = "uniconn";
    static final String PROPERTY_CLOUD_PRODUCT_TYPE_ID = "uniconnCloudProductTypeId";
    static final String CLOUD_PRODUCT_TYPE_SECURE_TUNNEL = "secure.tunnel";

    // see the plugin's InvalidTunnelConfigurationException and InvalidUpstreamUrlException
    static final Set<String> INVALID_INPUT_EXCEPTIONS = Set.of(
            "InvalidTunnelConfigurationException",
            "InvalidUpstreamUrlException");

    private static final String NOT_FOUND_MESSAGE = "No cloud application tunnel is configured";

    private final BundleContext bundleContext;

    private final MutatingApplicationLinkService mutatingApplicationLinkService;

    public DefaultCloudTunnelServiceImpl(
            final BundleContext bundleContext,
            final MutatingApplicationLinkService mutatingApplicationLinkService) {

        this.bundleContext = bundleContext;
        this.mutatingApplicationLinkService = mutatingApplicationLinkService;
    }

    @Override
    public CloudTunnelModel getCloudTunnel() {
        return withTunnelService(tunnelService -> getTunnel(tunnelService)
                .map(tunnel -> toCloudTunnelModel(tunnelService, tunnel))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MESSAGE)));
    }

    @Override
    public CloudTunnelModel setCloudTunnel(
            final CloudTunnelModel cloudTunnelModel) {

        final String key = cloudTunnelModel.getKey();
        if (StringUtils.isBlank(key)) {
            throw new BadRequestException("The key of the cloud application tunnel must be provided");
        }

        validateNotManagedByCloudConnection();

        // the plugin only saves (and reconnects) the tunnel if it has changed,
        // and a missing upstream URL clears an existing one
        final String upstreamUrl = StringUtils.trimToNull(cloudTunnelModel.getUpstreamUrl());

        return withTunnelService(tunnelService -> {
            if (upstreamUrl == null) {
                invokeMethod(tunnelService, METHOD_UPDATE_TUNNEL, key.trim());
            } else {
                invokeMethod(tunnelService, METHOD_UPDATE_TUNNEL, key.trim(), upstreamUrl);
            }

            return getTunnel(tunnelService)
                    .map(tunnel -> toCloudTunnelModel(tunnelService, tunnel))
                    .orElseThrow(() -> new InternalServerErrorException("The cloud application tunnel was not saved"));
        });
    }

    @Override
    public void deleteCloudTunnel() {
        validateNotManagedByCloudConnection();

        withTunnelService(tunnelService -> {
            if (getTunnel(tunnelService).isEmpty()) {
                throw new NotFoundException(NOT_FOUND_MESSAGE);
            }

            return invokeMethod(tunnelService, METHOD_DELETE_TUNNEL);
        });
    }

    /**
     * A tunnel accepted as a cloud product connection belongs to that
     * connection, whose application link the plugin keeps in sync with it.
     * Changing or deleting the tunnel here would leave that connection broken.
     */
    private void validateNotManagedByCloudConnection() {
        final boolean managedByCloudConnection = StreamSupport
                .stream(mutatingApplicationLinkService.getApplicationLinks().spliterator(), false)
                .anyMatch(DefaultCloudTunnelServiceImpl::isSecureTunnelApplicationLink);

        if (managedByCloudConnection) {
            throw new BadRequestException("The cloud application tunnel is managed by a cloud connection"
                    + " and can only be changed or deleted with it");
        }
    }

    private <T> T withTunnelService(
            final Function<Object, T> function) {

        final ServiceReference<?> serviceReference = bundleContext.getServiceReference(TUNNEL_SERVICE_CLASS);
        final Object tunnelService = serviceReference != null ? bundleContext.getService(serviceReference) : null;

        if (tunnelService == null) {
            throw new ServiceUnavailableException(
                    "The cloud application tunnel cannot be configured, the Secure Tunnel Client app is not installed or enabled");
        }

        try {
            return function.apply(tunnelService);
        } finally {
            bundleContext.ungetService(serviceReference);
        }
    }

    private static Optional<?> getTunnel(
            final Object tunnelService) {

        final Object tunnel = invokeMethod(tunnelService, METHOD_GET_TUNNEL);

        if (!(tunnel instanceof Optional)) {
            throw incompatible(METHOD_GET_TUNNEL);
        }

        return (Optional<?>) tunnel;
    }

    private static CloudTunnelModel toCloudTunnelModel(
            final Object tunnelService,
            final Object tunnel) {

        return CloudTunnelModel.builder()
                .name(getStringValue(tunnel, METHOD_NAME))
                .organizationName(getStringValue(tunnel, METHOD_CLOUD_ORG_NAME))
                .cloudPageUrl(getStringValue(tunnel, METHOD_CLOUD_PAGE_URL))
                .upstreamUrl(getStringValue(tunnel, METHOD_UPSTREAM_URL))
                .status(getStatus(tunnelService))
                .build();
    }

    // an unknown status is not returned instead of failing the request
    private static CloudTunnelStatus getStatus(
            final Object tunnelService) {

        final Object status = invokeMethod(tunnelService, METHOD_GET_TUNNEL_STATUS);

        if (status == null) {
            return null;
        }

        try {
            return CloudTunnelStatus.valueOf(status.toString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String getStringValue(
            final Object target,
            final String methodName) {

        final Object value = invokeMethod(target, methodName);
        return value != null ? value.toString() : null;
    }

    private static Object invokeMethod(
            final Object target,
            final String methodName,
            final Object... arguments) {

        final Method method = findMethod(target, methodName, arguments.length);

        try {
            return method.invoke(target, arguments);
        } catch (InvocationTargetException e) {
            throw toWebException(e.getCause());
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new InternalServerErrorException(e);
        }
    }

    private static Method findMethod(
            final Object target,
            final String methodName,
            final int parameterCount) {

        for (Method candidate : target.getClass().getMethods()) {
            if (candidate.getName().equals(methodName) && candidate.getParameterCount() == parameterCount) {
                return candidate;
            }
        }

        throw incompatible(methodName);
    }

    private static ServiceUnavailableException incompatible(
            final String methodName) {

        return new ServiceUnavailableException(String.format(
                "The installed Secure Tunnel Client app does not support configuring the cloud application tunnel"
                        + " (method '%s' not supported)", methodName));
    }

    // the plugin reports an invalid key or upstream URL with these exceptions,
    // which are not referenced at compile time like the service itself
    private static RuntimeException toWebException(
            final Throwable cause) {

        if (cause != null && INVALID_INPUT_EXCEPTIONS.contains(cause.getClass().getSimpleName())
                && StringUtils.isNotBlank(cause.getMessage())) {
            return new BadRequestException(cause.getMessage());
        }

        return new InternalServerErrorException(cause);
    }

    private static boolean isSecureTunnelApplicationLink(
            final ApplicationLink applicationLink) {

        return Boolean.parseBoolean(String.valueOf(applicationLink.getProperty(PROPERTY_UNICONN)))
                && CLOUD_PRODUCT_TYPE_SECURE_TUNNEL.equals(applicationLink.getProperty(PROPERTY_CLOUD_PRODUCT_TYPE_ID));
    }

}
