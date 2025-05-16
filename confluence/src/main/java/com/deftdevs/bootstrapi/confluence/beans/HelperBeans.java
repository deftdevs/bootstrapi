package com.deftdevs.bootstrapi.confluence.beans;

import com.atlassian.applinks.spi.auth.AuthenticationConfigurationManager;
import com.atlassian.oauth.consumer.ConsumerService;
import com.atlassian.oauth.consumer.ConsumerTokenStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderConsumerStore;
import com.atlassian.oauth.serviceprovider.ServiceProviderTokenStore;
import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.confluence.helper.ApplicationLinksAuthConfigHelperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        AtlassianBeans.class,
})
public class HelperBeans {

    @Bean
    public ApplicationLinksAuthConfigHelper applicationLinksAuthConfigHelper(
            final AuthenticationConfigurationManager authenticationConfigurationManager,
            final ConsumerService consumerService,
            final ConsumerTokenStore consumerTokenStore,
            final ServiceProviderConsumerStore serviceProviderConsumerStore,
            final ServiceProviderTokenStore serviceProviderTokenStore) {

        return new ApplicationLinksAuthConfigHelperImpl(
                authenticationConfigurationManager,
                consumerService,
                consumerTokenStore,
                serviceProviderConsumerStore,
                serviceProviderTokenStore);
    }

}
