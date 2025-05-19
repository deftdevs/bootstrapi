package com.deftdevs.bootstrapi.confluence.config;

import com.deftdevs.bootstrapi.commons.helper.api.ApplicationLinksAuthConfigHelper;
import com.deftdevs.bootstrapi.confluence.helper.ApplicationLinksAuthConfigHelperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelperConfig {

    @Autowired
    private AtlassianConfig atlassianConfig;

    @Bean
    public ApplicationLinksAuthConfigHelper applicationLinksAuthConfigHelper() {
        return new ApplicationLinksAuthConfigHelperImpl(
                atlassianConfig.authenticationConfigurationManager(),
                atlassianConfig.consumerService(),
                atlassianConfig.consumerTokenStore(),
                atlassianConfig.serviceProviderConsumerStore(),
                atlassianConfig.serviceProviderTokenStore());
    }

}
