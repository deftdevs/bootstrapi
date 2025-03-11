package com.deftdevs.bootstrapi.commons.helper;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.auth.AuthenticationProvider;
import com.atlassian.applinks.api.auth.types.OAuthAuthenticationProvider;
import com.atlassian.applinks.api.auth.types.TwoLeggedOAuthAuthenticationProvider;
import com.atlassian.applinks.api.auth.types.TwoLeggedOAuthWithImpersonationAuthenticationProvider;
import com.atlassian.applinks.internal.common.auth.oauth.ApplinksOAuth;
import com.atlassian.applinks.internal.common.auth.oauth.ConsumerInformationHelper;
import com.atlassian.applinks.internal.common.auth.oauth.util.Consumers;
import com.atlassian.applinks.internal.common.exception.ConsumerInformationUnavailableException;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.internal.status.oauth.OAuthConfigs;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.oauth.Consumer;
import com.atlassian.oauth.consumer.ConsumerService;
import com.atlassian.oauth.consumer.ConsumerToken;
import com.atlassian.oauth.consumer.ConsumerTokenStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import com.atlassian.oauth.serviceprovider.StoreException;
import com.atlassian.sal.api.net.ResponseException;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

public class DefaultApplicationLinksAuthConfigHelperImpl implements ApplicationLinksAuthConfigHelper {

    private static final Logger log = LoggerFactory.getLogger(DefaultApplicationLinksAuthConfigHelperImpl.class);

    private final AuthenticationConfigurationManager authenticationConfigurationManager;

    private final ConsumerService consumerService;

    private final ConsumerTokenStore consumerTokenStore;

    private final ServiceProviderConsumerStore serviceProviderConsumerStore;

    private final ServiceProviderTokenStore serviceProviderTokenStore;

    public DefaultApplicationLinksAuthConfigHelperImpl(
            final AuthenticationConfigurationManager authenticationConfigurationManager,
            final ConsumerService consumerService,
            final ConsumerTokenStore consumerTokenStore,
            final ServiceProviderConsumerStore serviceProviderConsumerStore,
            final ServiceProviderTokenStore serviceProviderTokenStore) {

        this.authenticationConfigurationManager = authenticationConfigurationManager;
        this.consumerService = consumerService;
        this.consumerTokenStore = consumerTokenStore;
        this.serviceProviderConsumerStore = serviceProviderConsumerStore;
        this.serviceProviderTokenStore = serviceProviderTokenStore;
    }

    public OAuthConfig getOutgoingOAuthConfig(
            final ApplicationLink applicationLink) {

        final boolean is3LoConfigured = authenticationConfigurationManager.isConfigured(
                applicationLink.getId(), OAuthAuthenticationProvider.class);
        final boolean is2LoConfigured = authenticationConfigurationManager.isConfigured(
                applicationLink.getId(), TwoLeggedOAuthAuthenticationProvider.class);
        final boolean is2LoIConfigured = authenticationConfigurationManager.isConfigured(
                applicationLink.getId(), TwoLeggedOAuthWithImpersonationAuthenticationProvider.class);

        return OAuthConfig.fromConfig(is3LoConfigured, is2LoConfigured, is2LoIConfigured);
    }

    public void setOutgoingOAuthConfig(
            final ApplicationLink applicationLink,
            final OAuthConfig outgoingOAuthConfig) {

        if (!outgoingOAuthConfig.isEnabled()) {
            removeOutgoingConsumerTokens(applicationLink);
        }

        setProviderState(applicationLink, OAuthAuthenticationProvider.class, outgoingOAuthConfig.isEnabled());
        setProviderState(applicationLink, TwoLeggedOAuthAuthenticationProvider.class, outgoingOAuthConfig.isTwoLoEnabled());
        setProviderState(applicationLink, TwoLeggedOAuthWithImpersonationAuthenticationProvider.class, outgoingOAuthConfig.isTwoLoImpersonationEnabled());
    }

    public OAuthConfig getIncomingOAuthConfig(
            final ApplicationLink applicationLink) {

        return OAuthConfigs.fromConsumer(getIncomingConsumer(applicationLink));
    }

    public void setIncomingOAuthConfig(
            final ApplicationLink applicationLink,
            final OAuthConfig incomingOAuthConfig) throws ConsumerInformationUnavailableException {

        if (!incomingOAuthConfig.isEnabled()) {
            removeIncomingConsumer(applicationLink);
        } else {
            final Consumer consumer = getOrFetchIncomingConsumer(applicationLink);
            final Consumer updatedConsumer = Consumers.consumerBuilder(consumer)
                    .twoLOAllowed(incomingOAuthConfig.isTwoLoEnabled())
                    .twoLOImpersonationAllowed(incomingOAuthConfig.isTwoLoImpersonationEnabled())
                    .build();
            addIncomingConsumer(applicationLink, updatedConsumer);
        }
    }

    void removeOutgoingConsumerTokens(
            final ApplicationLink applicationLink) {

        if (!authenticationConfigurationManager.isConfigured(applicationLink.getId(), OAuthAuthenticationProvider.class)) {
            return;
        }

        final Map<String, String> authenticationConfiguration = authenticationConfigurationManager.getConfiguration(
                applicationLink.getId(),
                OAuthAuthenticationProvider.class);

        if (authenticationConfiguration.containsKey(ApplinksOAuth.AUTH_CONFIG_CONSUMER_KEY_OUTBOUND)) {
            final String outgoingConsumerKey = authenticationConfiguration.get(ApplinksOAuth.AUTH_CONFIG_CONSUMER_KEY_OUTBOUND);
            consumerTokenStore.removeTokensForConsumer(outgoingConsumerKey);
        } else {
            final String outgoingConsumerKey = consumerService.getConsumer().getKey();
            final Map<ConsumerTokenStore.Key, ConsumerToken> outgoingConsumerTokens = consumerTokenStore.getConsumerTokens(outgoingConsumerKey);

            for (ConsumerTokenStore.Key outgoingConsumerToken : outgoingConsumerTokens.keySet()) {
                final Map<String, String> outgoingConsumerTokenProperties = outgoingConsumerTokens.get(outgoingConsumerToken).getProperties();

                if (outgoingConsumerTokenProperties.containsKey("applinks.oauth.applicationLinkId") && outgoingConsumerTokenProperties.get("applinks.oauth.applicationLinkId").equals(applicationLink.getId().get())) {
                    consumerTokenStore.remove(outgoingConsumerToken);
                }
            }
        }
    }

    Consumer getIncomingConsumer(
            final ApplicationLink applicationLink) {

        final String serviceProviderConsumerKey = getIncomingConsumerKey(applicationLink);
        return serviceProviderConsumerKey != null ? serviceProviderConsumerStore.get(serviceProviderConsumerKey) : null;
    }

    String getIncomingConsumerKey(
            final ApplicationLink applicationLink) {

        final Object storedConsumerKey = applicationLink.getProperty(ApplinksOAuth.PROPERTY_INCOMING_CONSUMER_KEY);
        return storedConsumerKey != null ? storedConsumerKey.toString() : null;
    }

    void addIncomingConsumer(
            final ApplicationLink applicationLink,
            final Consumer incomingConsumer) {

        serviceProviderConsumerStore.put(incomingConsumer);
        applicationLink.putProperty(ApplinksOAuth.PROPERTY_INCOMING_CONSUMER_KEY, incomingConsumer.getKey());
    }

    void removeIncomingConsumer(
            final ApplicationLink applicationLink) {

        final String consumerKey = getIncomingConsumerKey(applicationLink);

        if (consumerKey != null) {
            serviceProviderTokenStore.removeByConsumer(consumerKey);

            try {
                serviceProviderConsumerStore.remove(consumerKey);
            } catch (StoreException e) { /* do nothing but try to clean up the rest also */ }

            applicationLink.removeProperty(ApplinksOAuth.PROPERTY_INCOMING_CONSUMER_KEY);
        }
    }

    Consumer getOrFetchIncomingConsumer(
            final ApplicationLink applicationLink) throws ConsumerInformationUnavailableException {

        final Consumer incomingConsumer = getIncomingConsumer(applicationLink);

        if (incomingConsumer != null) {
            return incomingConsumer;
        }

        try {
            return ConsumerInformationHelper.fetchConsumerInformation(applicationLink);
        } catch (ResponseException e) {
            throw new ConsumerInformationUnavailableException(e.getMessage());
        }
    }

    private void setProviderState(
            final ApplicationLink applicationLink,
            final Class<? extends AuthenticationProvider> providerClass,
            final boolean enabled) {

        if (enabled) {
            authenticationConfigurationManager.registerProvider(applicationLink.getId(), providerClass, Collections.emptyMap());
        } else {
            authenticationConfigurationManager.unregisterProvider(applicationLink.getId(), providerClass);
        }
    }
}
