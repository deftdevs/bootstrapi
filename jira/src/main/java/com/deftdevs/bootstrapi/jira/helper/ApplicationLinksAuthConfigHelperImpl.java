package com.deftdevs.bootstrapi.jira.helper;

import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.oauth.consumer.ConsumerService;
import com.atlassian.oauth.consumer.ConsumerTokenStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import com.deftdevs.bootstrapi.commons.helper.DefaultApplicationLinksAuthConfigHelperImpl;

public class ApplicationLinksAuthConfigHelperImpl extends DefaultApplicationLinksAuthConfigHelperImpl {

    public ApplicationLinksAuthConfigHelperImpl(
            final AuthenticationConfigurationManager authenticationConfigurationManager,
            final ConsumerService consumerService,
            final ConsumerTokenStore consumerTokenStore,
            final ServiceProviderConsumerStore serviceProviderConsumerStore,
            final ServiceProviderTokenStore serviceProviderTokenStore) {

        super(authenticationConfigurationManager, consumerService, consumerTokenStore, serviceProviderConsumerStore, serviceProviderTokenStore);
    }
}
