package com.deftdevs.bootstrapi.confluence.service;

import com.atlassian.applinks.api.ApplicationLink;
import com.atlassian.applinks.api.auth.AuthenticationProvider;
import com.atlassian.applinks.api.auth.types.OAuthAuthenticationProvider;
import com.atlassian.applinks.api.auth.types.TwoLeggedOAuthAuthenticationProvider;
import com.atlassian.applinks.api.auth.types.TwoLeggedOAuthWithImpersonationAuthenticationProvider;
import com.atlassian.applinks.internal.common.auth.oauth.ConsumerInformationHelper;
import com.atlassian.applinks.internal.common.auth.oauth.util.Consumers;
import com.atlassian.applinks.internal.common.exception.ConsumerInformationUnavailableException;
import com.atlassian.applinks.internal.common.exception.ServiceExceptionFactory;
import com.atlassian.applinks.internal.common.permission.Unrestricted;
import com.atlassian.applinks.internal.common.status.oauth.OAuthConfig;
import com.atlassian.applinks.internal.status.oauth.OAuthConfigs;
import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.oauth.Consumer;
import com.atlassian.sal.api.net.ResponseException;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;

@Unrestricted("Consumers of this component need to enforce appropriate permission level")
public class OAuthConfigurator {
    private static final Logger log = LoggerFactory.getLogger(OAuthConfigurator.class);
    private final DefaultConsumerTokenStoreService consumerTokenStoreService;
    private final DefaultServiceProviderStoreService serviceProviderStoreService;
    private final ServiceExceptionFactory serviceExceptionFactory;
    private final AuthenticationConfigurationManager authenticationConfigurationManager;

    public OAuthConfigurator(AuthenticationConfigurationManager authenticationConfigurationManager, DefaultConsumerTokenStoreService consumerTokenStoreService, DefaultServiceProviderStoreService serviceProviderStoreService, ServiceExceptionFactory serviceExceptionFactory) {
        this.consumerTokenStoreService = consumerTokenStoreService;
        this.serviceProviderStoreService = serviceProviderStoreService;
        this.authenticationConfigurationManager = authenticationConfigurationManager;
        this.serviceExceptionFactory = serviceExceptionFactory;
    }

    @Nonnull
    public OAuthConfig getIncomingConfig(@Nonnull ApplicationLink applink) {
        Objects.requireNonNull(applink, "applink");
        return OAuthConfigs.fromConsumer(this.serviceProviderStoreService.getConsumer(applink));
    }

    @Nonnull
    public OAuthConfig getOutgoingConfig(@Nonnull ApplicationLink link) {
        Objects.requireNonNull(link, "link");
        boolean is3LoConfigured = this.authenticationConfigurationManager.isConfigured(link.getId(), OAuthAuthenticationProvider.class);
        boolean is2LoConfigured = this.authenticationConfigurationManager.isConfigured(link.getId(), TwoLeggedOAuthAuthenticationProvider.class);
        boolean is2LoIConfigured = this.authenticationConfigurationManager.isConfigured(link.getId(), TwoLeggedOAuthWithImpersonationAuthenticationProvider.class);
        return OAuthConfig.fromConfig(is3LoConfigured, is2LoConfigured, is2LoIConfigured);
    }

    public void updateOutgoingConfig(@Nonnull ApplicationLink applink, @Nonnull OAuthConfig outgoing) {
        Objects.requireNonNull(applink, "link");
        Objects.requireNonNull(outgoing, "outgoing");
        if (!outgoing.isEnabled()) {
            this.tryRemoveConsumerTokens(applink);
        }

        this.toggleProvider(applink, OAuthAuthenticationProvider.class, outgoing.isEnabled());
        this.toggleProvider(applink, TwoLeggedOAuthAuthenticationProvider.class, outgoing.isTwoLoEnabled());
        this.toggleProvider(applink, TwoLeggedOAuthWithImpersonationAuthenticationProvider.class, outgoing.isTwoLoImpersonationEnabled());
    }

    public void updateIncomingConfig(@Nonnull ApplicationLink applink, @Nonnull OAuthConfig incoming) throws ConsumerInformationUnavailableException {
        Objects.requireNonNull(applink, "applink");
        Objects.requireNonNull(incoming, "incoming");
        if (!incoming.isEnabled()) {
            try {
                this.serviceProviderStoreService.removeConsumer(applink);
            } catch (IllegalStateException e) {
                log.debug("Attempting to remove non-existing consumer for Application Link '{}'", applink);
                log.trace("Stack trace for link '{}'", applink, e);
            }
        } else {
            Consumer updatedConsumer = Consumers.consumerBuilder(this.getOrFetchConsumer(applink)).twoLOAllowed(incoming.isTwoLoEnabled()).twoLOImpersonationAllowed(incoming.isTwoLoImpersonationEnabled()).build();
            this.serviceProviderStoreService.addConsumer(updatedConsumer, applink);
        }

    }

    private void tryRemoveConsumerTokens(@Nonnull ApplicationLink applink) {
        try {
            this.consumerTokenStoreService.removeAllConsumerTokens(applink);
        } catch (IllegalStateException var3) {
        }

    }

    private void toggleProvider(ApplicationLink link, Class<? extends AuthenticationProvider> providerClass, boolean enabled) {
        if (enabled) {
            this.authenticationConfigurationManager.registerProvider(link.getId(), providerClass, Collections.emptyMap());
        } else {
            this.authenticationConfigurationManager.unregisterProvider(link.getId(), providerClass);
        }

    }

    private Consumer getOrFetchConsumer(ApplicationLink link) throws ConsumerInformationUnavailableException {
        Consumer consumer = this.serviceProviderStoreService.getConsumer(link);
        return consumer != null ? consumer : this.fetchConsumerInformation(link);
    }

    @VisibleForTesting
    protected Consumer fetchConsumerInformation(ApplicationLink link) throws ConsumerInformationUnavailableException {
        try {
            return ConsumerInformationHelper.fetchConsumerInformation(link);
        } catch (ResponseException var3) {
            throw (ConsumerInformationUnavailableException)this.serviceExceptionFactory.create(ConsumerInformationUnavailableException.class, new Serializable[]{link.getName()});
        }
    }
}
