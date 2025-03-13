package com.deftdevs.bootstrapi.commons.helper;

import com.atlassian.applinks.api.ApplicationId;
import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.auth.types.OAuthAuthenticationProvider;
import com.atlassian.applinks.api.auth.types.TwoLeggedOAuthAuthenticationProvider;
import com.atlassian.applinks.api.auth.types.TwoLeggedOAuthWithImpersonationAuthenticationProvider;
import com.atlassian.applinks.internal.common.exception.ConsumerInformationUnavailableException;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.oauth.Consumer;
import com.atlassian.oauth.consumer.ConsumerService;
import com.atlassian.oauth.consumer.ConsumerTokenStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationLink;
import com.deftdevs.bootstrapi.commons.types.DefaultApplicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultApplicationLinksAuthConfigHelperTest {

    @Mock
    private AuthenticationConfigurationManager authenticationConfigurationManager;

    @Mock
    private ConsumerService consumerService;

    @Mock
    private ConsumerTokenStore consumerTokenStore;

    @Mock
    private ServiceProviderConsumerStore serviceProviderConsumerStore;

    @Mock
    private ServiceProviderTokenStore serviceProviderTokenStore;

    private DefaultApplicationLinksAuthConfigHelperImpl applicationLinksAuthConfigHelper;

    @BeforeEach
    public void setup() {
        applicationLinksAuthConfigHelper = new DefaultApplicationLinksAuthConfigHelperImpl(
                authenticationConfigurationManager, consumerService, consumerTokenStore, serviceProviderConsumerStore, serviceProviderTokenStore);
    }

    public void setupAuthenticationConfigurationManagerIsConfigured(
            final ApplicationId applicationId,
            final OAuthConfig oAuthConfig) {

        lenient().doReturn(oAuthConfig.isEnabled()).when(authenticationConfigurationManager).isConfigured(applicationId, OAuthAuthenticationProvider.class);
        lenient().doReturn(oAuthConfig.isTwoLoEnabled()).when(authenticationConfigurationManager).isConfigured(applicationId, TwoLeggedOAuthAuthenticationProvider.class);
        lenient().doReturn(oAuthConfig.isTwoLoImpersonationEnabled()).when(authenticationConfigurationManager).isConfigured(applicationId, TwoLeggedOAuthWithImpersonationAuthenticationProvider.class);
    }

    @Test
    void testGetOutgoingOAuthConfigDisabled() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createDefaultOAuthConfig();
        setupAuthenticationConfigurationManagerIsConfigured(applicationLink.getId(), oAuthConfig);

        final OAuthConfig resultOAuthConfig = applicationLinksAuthConfigHelper.getOutgoingOAuthConfig(applicationLink);
        assertEquals(oAuthConfig, resultOAuthConfig);
    }

    @Test
    void testGetOutgoingOAuthConfigOAuth() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createDefaultOAuthConfig();
        setupAuthenticationConfigurationManagerIsConfigured(applicationLink.getId(), oAuthConfig);

        final OAuthConfig resultOAuthConfig = applicationLinksAuthConfigHelper.getOutgoingOAuthConfig(applicationLink);
        assertEquals(oAuthConfig, resultOAuthConfig);
    }

    @Test
    void testGetOutgoingOAuthConfigOAuthWithImpersonation() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createOAuthWithImpersonationConfig();
        setupAuthenticationConfigurationManagerIsConfigured(applicationLink.getId(), oAuthConfig);

        final OAuthConfig resultOAuthConfig = applicationLinksAuthConfigHelper.getOutgoingOAuthConfig(applicationLink);
        assertEquals(oAuthConfig, resultOAuthConfig);
    }

    @Test
    void testSetOutgoingOAuthConfigDisabled() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createDisabledConfig();

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);
        doNothing().when(spyApplicationLinksAuthConfigHelperImpl).removeOutgoingConsumerTokens(applicationLink);

        spyApplicationLinksAuthConfigHelperImpl.setOutgoingOAuthConfig(applicationLink, oAuthConfig);
        verify(spyApplicationLinksAuthConfigHelperImpl).removeOutgoingConsumerTokens(applicationLink);
        verify(authenticationConfigurationManager).unregisterProvider(applicationLink.getId(), OAuthAuthenticationProvider.class);
        verify(authenticationConfigurationManager).unregisterProvider(applicationLink.getId(), TwoLeggedOAuthAuthenticationProvider.class);
        verify(authenticationConfigurationManager).unregisterProvider(applicationLink.getId(), TwoLeggedOAuthWithImpersonationAuthenticationProvider.class);
    }

    @Test
    void testSetOutgoingOAuthConfigOAuth() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createDefaultOAuthConfig();

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);

        spyApplicationLinksAuthConfigHelperImpl.setOutgoingOAuthConfig(applicationLink, oAuthConfig);
        verify(spyApplicationLinksAuthConfigHelperImpl, never()).removeOutgoingConsumerTokens(applicationLink);
        verify(authenticationConfigurationManager).registerProvider(applicationLink.getId(), OAuthAuthenticationProvider.class, Collections.emptyMap());
        verify(authenticationConfigurationManager).registerProvider(applicationLink.getId(), TwoLeggedOAuthAuthenticationProvider.class, Collections.emptyMap());
        verify(authenticationConfigurationManager).unregisterProvider(applicationLink.getId(), TwoLeggedOAuthWithImpersonationAuthenticationProvider.class);
    }

    @Test
    void testSetOutgoingOAuthConfigOAuthWithImpersonation() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createOAuthWithImpersonationConfig();

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);

        spyApplicationLinksAuthConfigHelperImpl.setOutgoingOAuthConfig(applicationLink, oAuthConfig);
        verify(spyApplicationLinksAuthConfigHelperImpl, never()).removeOutgoingConsumerTokens(applicationLink);
        verify(authenticationConfigurationManager).registerProvider(applicationLink.getId(), OAuthAuthenticationProvider.class, Collections.emptyMap());
        verify(authenticationConfigurationManager).registerProvider(applicationLink.getId(), TwoLeggedOAuthAuthenticationProvider.class, Collections.emptyMap());
        verify(authenticationConfigurationManager).registerProvider(applicationLink.getId(), TwoLeggedOAuthWithImpersonationAuthenticationProvider.class, Collections.emptyMap());
    }

    @Test
    void testGetIncomingOAuthConfigDisabled() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createDisabledConfig();
        // consumer key is explicitly null, consumer is implicitly null

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);
        doReturn(null).when(spyApplicationLinksAuthConfigHelperImpl).getIncomingConsumerKey(applicationLink);

        final OAuthConfig resultOAuthConfig = spyApplicationLinksAuthConfigHelperImpl.getIncomingOAuthConfig(applicationLink);
        assertEquals(oAuthConfig, resultOAuthConfig);
    }

    @Test
    void testGetIncomingOAuthConfigOAuth() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createDefaultOAuthConfig();
        final String consumerKey = createConsumerKey();
        final Consumer consumer = createConsumerFromOAuthConfig(consumerKey, oAuthConfig);

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);
        doReturn(consumerKey).when(spyApplicationLinksAuthConfigHelperImpl).getIncomingConsumerKey(applicationLink);
        doReturn(consumer).when(serviceProviderConsumerStore).get(consumerKey);

        final OAuthConfig resultOAuthConfig = spyApplicationLinksAuthConfigHelperImpl.getIncomingOAuthConfig(applicationLink);
        assertEquals(oAuthConfig, resultOAuthConfig);
    }

    @Test
    void testGetIncomingOAuthConfigOAuthWithImpersonation() throws URISyntaxException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createOAuthWithImpersonationConfig();
        final String consumerKey = createConsumerKey();
        final Consumer consumer = createConsumerFromOAuthConfig(consumerKey, oAuthConfig);

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);
        doReturn(consumerKey).when(spyApplicationLinksAuthConfigHelperImpl).getIncomingConsumerKey(applicationLink);
        doReturn(consumer).when(serviceProviderConsumerStore).get(consumerKey);

        final OAuthConfig resultOAuthConfig = spyApplicationLinksAuthConfigHelperImpl.getIncomingOAuthConfig(applicationLink);
        assertEquals(oAuthConfig, resultOAuthConfig);
    }

    @Test
    void testSetIncomingOAuthConfigDisabled() throws URISyntaxException, ConsumerInformationUnavailableException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createDisabledConfig();

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);
        doNothing().when(spyApplicationLinksAuthConfigHelperImpl).removeIncomingConsumer(applicationLink);

        spyApplicationLinksAuthConfigHelperImpl.setIncomingOAuthConfig(applicationLink, oAuthConfig);
        verify(spyApplicationLinksAuthConfigHelperImpl).removeIncomingConsumer(applicationLink);
        verify(spyApplicationLinksAuthConfigHelperImpl, never()).addIncomingConsumer(any(ApplicationLink.class), any(Consumer.class));
    }

    @Test
    void testSetIncomingOAuthConfigOAuth() throws URISyntaxException, ConsumerInformationUnavailableException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createDefaultOAuthConfig();
        final String consumerKey = createConsumerKey();
        final Consumer consumer = createConsumerFromOAuthConfig(consumerKey, OAuthConfig.createDisabledConfig());

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);
        doReturn(consumer).when(spyApplicationLinksAuthConfigHelperImpl).getOrFetchIncomingConsumer(applicationLink);
        doNothing().when(spyApplicationLinksAuthConfigHelperImpl).addIncomingConsumer(any(ApplicationLink.class), any(Consumer.class));

        spyApplicationLinksAuthConfigHelperImpl.setIncomingOAuthConfig(applicationLink, oAuthConfig);
        verify(spyApplicationLinksAuthConfigHelperImpl, never()).removeIncomingConsumer(any(ApplicationLink.class));
        verify(spyApplicationLinksAuthConfigHelperImpl).addIncomingConsumer(any(ApplicationLink.class), any(Consumer.class));
    }

    @Test
    void testSetIncomingOAuthConfigOAuthWithImpersonation() throws URISyntaxException, ConsumerInformationUnavailableException {
        final ApplicationLink applicationLink = createApplicationLink();
        final OAuthConfig oAuthConfig = OAuthConfig.createOAuthWithImpersonationConfig();
        final String consumerKey = createConsumerKey();
        final Consumer consumer = createConsumerFromOAuthConfig(consumerKey, OAuthConfig.createDisabledConfig());

        final DefaultApplicationLinksAuthConfigHelperImpl spyApplicationLinksAuthConfigHelperImpl = spy(applicationLinksAuthConfigHelper);
        doReturn(consumer).when(spyApplicationLinksAuthConfigHelperImpl).getOrFetchIncomingConsumer(applicationLink);
        doNothing().when(spyApplicationLinksAuthConfigHelperImpl).addIncomingConsumer(any(ApplicationLink.class), any(Consumer.class));

        spyApplicationLinksAuthConfigHelperImpl.setIncomingOAuthConfig(applicationLink, oAuthConfig);
        verify(spyApplicationLinksAuthConfigHelperImpl, never()).removeIncomingConsumer(any(ApplicationLink.class));
        verify(spyApplicationLinksAuthConfigHelperImpl).addIncomingConsumer(any(ApplicationLink.class), any(Consumer.class));
    }

    private ApplicationLink createApplicationLink() throws URISyntaxException {
        final ApplicationId applicationId = new ApplicationId(UUID.randomUUID().toString());
        final URI uri = new URI("http://localhost");
        return new DefaultApplicationLink(applicationId, new DefaultApplicationType(), "test", uri, uri, false, false);
    }

    private Consumer createConsumerFromOAuthConfig(
            final String consumerKey,
            final OAuthConfig oAuthConfig) {

        return Consumer.key(consumerKey)
                .name("consumer")
                .twoLOAllowed(oAuthConfig.isTwoLoEnabled())
                .twoLOImpersonationAllowed(oAuthConfig.isTwoLoImpersonationEnabled())
                .signatureMethod(Consumer.SignatureMethod.HMAC_SHA1)
                .build();
    }

    private String createConsumerKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
