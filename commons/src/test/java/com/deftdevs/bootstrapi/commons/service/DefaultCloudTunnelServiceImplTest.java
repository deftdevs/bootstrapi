package com.deftdevs.bootstrapi.commons.service;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.spi.link.MutatingApplicationLinkService;
import com.deftdevs.bootstrapi.commons.exception.web.BadRequestException;
import com.deftdevs.bootstrapi.commons.exception.web.InternalServerErrorException;
import com.deftdevs.bootstrapi.commons.exception.web.NotFoundException;
import com.deftdevs.bootstrapi.commons.exception.web.ServiceUnavailableException;
import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel;
import com.deftdevs.bootstrapi.commons.model.CloudTunnelModel.CloudTunnelStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.deftdevs.bootstrapi.commons.service.DefaultCloudTunnelServiceImpl.CLOUD_PRODUCT_TYPE_SECURE_TUNNEL;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudTunnelServiceImpl.PROPERTY_CLOUD_PRODUCT_TYPE_ID;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudTunnelServiceImpl.PROPERTY_UNICONN;
import static com.deftdevs.bootstrapi.commons.service.DefaultCloudTunnelServiceImpl.TUNNEL_SERVICE_CLASS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class DefaultCloudTunnelServiceImplTest {

    private static final String KEY = "tunnel-key";
    private static final String UPSTREAM_URL = "https://jira.example.com";

    @Mock
    private BundleContext bundleContext;

    @Mock
    private ServiceReference<Object> serviceReference;

    @Mock
    private MutatingApplicationLinkService mutatingApplicationLinkService;

    private FakeTunnelService tunnelService;

    private DefaultCloudTunnelServiceImpl cloudTunnelService;

    @BeforeEach
    void setup() {
        tunnelService = new FakeTunnelService();
        cloudTunnelService = new DefaultCloudTunnelServiceImpl(bundleContext, mutatingApplicationLinkService);

        lenient().doReturn(serviceReference).when(bundleContext).getServiceReference(TUNNEL_SERVICE_CLASS);
        lenient().doReturn(tunnelService).when(bundleContext).getService(serviceReference);
        lenient().doReturn(Collections.emptyList()).when(mutatingApplicationLinkService).getApplicationLinks();
    }

    @Test
    void testGetCloudTunnel() {
        tunnelService.tunnel = new FakeTunnel();

        final CloudTunnelModel cloudTunnelModel = cloudTunnelService.getCloudTunnel();

        assertNull(cloudTunnelModel.getKey());
        assertEquals(CloudTunnelModel.EXAMPLE_1, cloudTunnelModel);
        verify(bundleContext).ungetService(serviceReference);
    }

    @Test
    void testGetCloudTunnelWithUnknownStatus() {
        tunnelService.tunnel = new FakeTunnel();
        tunnelService.status = "UNKNOWN";

        assertNull(cloudTunnelService.getCloudTunnel().getStatus());
    }

    @Test
    void testGetCloudTunnelWithoutStatus() {
        tunnelService.tunnel = new FakeTunnel();
        tunnelService.status = null;

        assertNull(cloudTunnelService.getCloudTunnel().getStatus());
    }

    @Test
    void testGetCloudTunnelNotConfigured() {
        assertThrows(NotFoundException.class, () -> cloudTunnelService.getCloudTunnel());
        verify(bundleContext).ungetService(serviceReference);
    }

    @Test
    void testGetCloudTunnelPluginNotInstalled() {
        doReturn(null).when(bundleContext).getServiceReference(TUNNEL_SERVICE_CLASS);

        assertThrows(ServiceUnavailableException.class, () -> cloudTunnelService.getCloudTunnel());
    }

    @Test
    void testGetCloudTunnelServiceUnregistered() {
        doReturn(null).when(bundleContext).getService(serviceReference);

        assertThrows(ServiceUnavailableException.class, () -> cloudTunnelService.getCloudTunnel());
    }

    @Test
    void testGetCloudTunnelServiceIncompatible() {
        doReturn(new Object()).when(bundleContext).getService(serviceReference);

        assertThrows(ServiceUnavailableException.class, () -> cloudTunnelService.getCloudTunnel());
        verify(bundleContext).ungetService(serviceReference);
    }

    @Test
    void testGetCloudTunnelServiceWithUnsupportedReturnType() {
        doReturn(new UnsupportedTunnelService()).when(bundleContext).getService(serviceReference);

        assertThrows(ServiceUnavailableException.class, () -> cloudTunnelService.getCloudTunnel());
    }

    @Test
    void testSetCloudTunnel() {
        final CloudTunnelModel cloudTunnelModel = cloudTunnelService.setCloudTunnel(
                CloudTunnelModel.builder().key(" " + KEY + " ").build());

        assertEquals(List.of(KEY), tunnelService.updateArguments);
        assertEquals(CloudTunnelModel.EXAMPLE_1.getName(), cloudTunnelModel.getName());
        verify(bundleContext).ungetService(serviceReference);
    }

    @Test
    void testSetCloudTunnelWithUpstreamUrl() {
        cloudTunnelService.setCloudTunnel(CloudTunnelModel.builder().key(KEY).upstreamUrl(UPSTREAM_URL).build());

        assertEquals(List.of(KEY, UPSTREAM_URL), tunnelService.updateArguments);
    }

    @Test
    void testSetCloudTunnelWithBlankUpstreamUrlClearsIt() {
        cloudTunnelService.setCloudTunnel(CloudTunnelModel.builder().key(KEY).upstreamUrl(" ").build());

        assertEquals(List.of(KEY), tunnelService.updateArguments);
    }

    @Test
    void testSetCloudTunnelWithoutKey() {
        final CloudTunnelModel cloudTunnelModel = CloudTunnelModel.builder().upstreamUrl(UPSTREAM_URL).build();

        assertThrows(BadRequestException.class, () -> cloudTunnelService.setCloudTunnel(cloudTunnelModel));
        verifyNoInteractions(bundleContext, mutatingApplicationLinkService);
    }

    @Test
    void testSetCloudTunnelWithInvalidKey() {
        final InvalidTunnelConfigurationException invalidKeyException = new InvalidTunnelConfigurationException("Invalid key");
        tunnelService.updateException = invalidKeyException;
        final CloudTunnelModel cloudTunnelModel = CloudTunnelModel.builder().key(KEY).build();

        final BadRequestException exception = assertThrows(BadRequestException.class,
                () -> cloudTunnelService.setCloudTunnel(cloudTunnelModel));
        assertEquals(invalidKeyException.getMessage(), exception.getCause().getMessage());
        verify(bundleContext).ungetService(serviceReference);
    }

    @Test
    void testSetCloudTunnelWithInvalidUpstreamUrl() {
        tunnelService.updateException = new InvalidUpstreamUrlException("Invalid upstream URL");
        final CloudTunnelModel cloudTunnelModel = CloudTunnelModel.builder().key(KEY).upstreamUrl(UPSTREAM_URL).build();

        assertThrows(BadRequestException.class, () -> cloudTunnelService.setCloudTunnel(cloudTunnelModel));
    }

    @Test
    void testSetCloudTunnelWithUnexpectedError() {
        tunnelService.updateException = new IllegalStateException("Unexpected");
        final CloudTunnelModel cloudTunnelModel = CloudTunnelModel.builder().key(KEY).build();

        assertThrows(InternalServerErrorException.class, () -> cloudTunnelService.setCloudTunnel(cloudTunnelModel));
    }

    @Test
    void testSetCloudTunnelNotSaved() {
        tunnelService.saveOnUpdate = false;
        final CloudTunnelModel cloudTunnelModel = CloudTunnelModel.builder().key(KEY).build();

        assertThrows(InternalServerErrorException.class, () -> cloudTunnelService.setCloudTunnel(cloudTunnelModel));
    }

    @Test
    void testSetCloudTunnelManagedByCloudConnection() {
        mockSecureTunnelApplicationLink();
        final CloudTunnelModel cloudTunnelModel = CloudTunnelModel.builder().key(KEY).build();

        assertThrows(BadRequestException.class, () -> cloudTunnelService.setCloudTunnel(cloudTunnelModel));
        verifyNoInteractions(bundleContext);
    }

    @Test
    void testSetCloudTunnelWithOtherCloudConnection() {
        final ApplicationLink applicationLink = mock(ApplicationLink.class);
        doReturn("true").when(applicationLink).getProperty(PROPERTY_UNICONN);
        doReturn("confluence").when(applicationLink).getProperty(PROPERTY_CLOUD_PRODUCT_TYPE_ID);
        doReturn(List.of(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();

        cloudTunnelService.setCloudTunnel(CloudTunnelModel.builder().key(KEY).build());

        assertEquals(List.of(KEY), tunnelService.updateArguments);
    }

    @Test
    void testDeleteCloudTunnel() {
        tunnelService.tunnel = new FakeTunnel();

        cloudTunnelService.deleteCloudTunnel();

        assertNull(tunnelService.tunnel);
        verify(bundleContext).ungetService(serviceReference);
    }

    @Test
    void testDeleteCloudTunnelNotConfigured() {
        assertThrows(NotFoundException.class, () -> cloudTunnelService.deleteCloudTunnel());
        assertEquals(0, tunnelService.deleteCount);
    }

    @Test
    void testDeleteCloudTunnelManagedByCloudConnection() {
        mockSecureTunnelApplicationLink();
        tunnelService.tunnel = new FakeTunnel();

        assertThrows(BadRequestException.class, () -> cloudTunnelService.deleteCloudTunnel());
        assertEquals(0, tunnelService.deleteCount);
        verify(bundleContext, never()).getServiceReference(TUNNEL_SERVICE_CLASS);
    }

    private void mockSecureTunnelApplicationLink() {
        final ApplicationLink applicationLink = mock(ApplicationLink.class);
        doReturn("true").when(applicationLink).getProperty(PROPERTY_UNICONN);
        doReturn(CLOUD_PRODUCT_TYPE_SECURE_TUNNEL).when(applicationLink).getProperty(PROPERTY_CLOUD_PRODUCT_TYPE_ID);
        doReturn(List.of(applicationLink)).when(mutatingApplicationLinkService).getApplicationLinks();
    }

    /**
     * Like the plugin's Tunnel record, matching {@link CloudTunnelModel#EXAMPLE_1}.
     */
    public static class FakeTunnel {

        public String name() {
            return CloudTunnelModel.EXAMPLE_1.getName();
        }

        public String cloudOrgName() {
            return CloudTunnelModel.EXAMPLE_1.getOrganizationName();
        }

        public String cloudPageUrl() {
            return CloudTunnelModel.EXAMPLE_1.getCloudPageUrl();
        }

        public String upstreamUrl() {
            return CloudTunnelModel.EXAMPLE_1.getUpstreamUrl();
        }
    }

    /**
     * Stands in for the plugin's TunnelService, which the service under test
     * looks up by class name and invokes reflectively by method name.
     */
    public static class FakeTunnelService {

        private FakeTunnel tunnel;

        private String status = CloudTunnelStatus.CONNECTED.name();

        private RuntimeException updateException;

        private boolean saveOnUpdate = true;

        private final List<String> updateArguments = new ArrayList<>();

        private int deleteCount;

        public Optional<FakeTunnel> getTunnel() {
            return Optional.ofNullable(tunnel);
        }

        // the plugin returns its TunnelStatus enum
        public Object getTunnelStatus() {
            return status;
        }

        public Optional<FakeTunnel> updateTunnel(
                final String key) {

            return update(key);
        }

        public Optional<FakeTunnel> updateTunnel(
                final String key,
                final String upstreamUrl) {

            return update(key, upstreamUrl);
        }

        public void deleteTunnel() {
            deleteCount++;
            tunnel = null;
        }

        private Optional<FakeTunnel> update(
                final String... arguments) {

            if (updateException != null) {
                throw updateException;
            }

            updateArguments.addAll(List.of(arguments));

            if (saveOnUpdate) {
                tunnel = new FakeTunnel();
            }

            return getTunnel();
        }
    }

    /**
     * A tunnel service whose getTunnel method does not return an Optional.
     */
    public static class UnsupportedTunnelService {

        public Object getTunnel() {
            return new Object();
        }
    }

    // same simple names as the exceptions of the plugin

    static class InvalidTunnelConfigurationException extends RuntimeException {

        InvalidTunnelConfigurationException(final String message) {
            super(message);
        }
    }

    static class InvalidUpstreamUrlException extends RuntimeException {

        InvalidUpstreamUrlException(final String message) {
            super(message);
        }
    }

}
