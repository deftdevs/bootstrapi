package com.deftdevs.bootstrapi.jira.helper;

import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.oauth.consumer.ConsumerService;
import com.atlassian.oauth.consumer.ConsumerTokenStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.deftdevs.bootstrapi.commons.helper.DefaultApplicationLinksAuthConfigHelperImpl;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLinksAuthConfigHelperImpl extends DefaultApplicationLinksAuthConfigHelperImpl {

    public ApplicationLinksAuthConfigHelperImpl(
            @ComponentImport final AuthenticationConfigurationManager authenticationConfigurationManager,
            @ComponentImport final ConsumerService consumerService,
            @ComponentImport final ConsumerTokenStore consumerTokenStore,
            @ComponentImport final ServiceProviderConsumerStore serviceProviderConsumerStore,
            @ComponentImport final ServiceProviderTokenStore serviceProviderTokenStore) {

        super(authenticationConfigurationManager, consumerService, consumerTokenStore, serviceProviderConsumerStore, serviceProviderTokenStore);
    }
}
